package com.mozat.morange.dbcache;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.mozat.morange.dbcache.core.SqlDbConfig;
import com.mozat.morange.dbcache.core.SqlDbDefaultCacheConfig;
import com.mozat.morange.dbcache.core.SqlDbPoolConfig;
import com.mozat.morange.dbcache.core.TableCache;
import com.mozat.morange.dbcache.core.TableCacheConfig;
import com.mozat.morange.log.DebugLog;
import com.mozat.morange.util.EhcacheUtil;

import net.sf.ehcache.CacheManager;

public class SQLServerCacheManager {	
	private static HashMap<String, SQLServerCacheManager> _managerMap;
	static {
		_managerMap = new HashMap<String, SQLServerCacheManager>();
	}
	
	private static final Logger logger = LoggerFactory.getLogger(SQLServerCacheManager.class);
	
	boolean _isInitiated = false;
	private CacheManager _ehCacheManager;
	private HashMap<String, TableCache> _cacheMap = new HashMap<String, TableCache>();
	private BoneCP _connectionPool = null;
	
	private String _configFilePath;
	private String _name;
	
	private SqlDbConfig _dbConfig;
	private SqlDbDefaultCacheConfig _dbDefaultCacheConfig;
	private SqlDbPoolConfig _dbPoolConfig;
	private ArrayList<TableCacheConfig> _tableCacheConfigList;
	
	public SQLServerCacheManager(String configPath, String name){
		_configFilePath = configPath;
		_name = name;
		_isInitiated = false;
		
		// global EHCache manager
		_ehCacheManager = EhcacheUtil.getManger();
	}
	
	public static SQLServerCacheManager create(String configPath){
		String name = configPath;
		
		return SQLServerCacheManager.create(configPath, name);
	}
	
	private static SQLServerCacheManager create(String configPath, String name){
		SQLServerCacheManager manager = _managerMap.get(name);
		if (null != manager){
			return manager;
		}
		
		manager = new SQLServerCacheManager(configPath, name);
		_managerMap.put(name, manager);
		
		return manager;
	}
	
	public static void shutdown(){
		for (Map.Entry<String, SQLServerCacheManager> entry: _managerMap.entrySet()){
			entry.getValue().stop();
		}
	}
	
	public CacheManager getEhCacheManager(){
		return _ehCacheManager;
	}
	
	public String getName(){
		return _name;
	}
	
	public java.sql.Connection getConnection() throws SQLException{
		return _connectionPool.getConnection();
	}
	
	public static final String NODE_MOZAT_DB = "mozat-db";
	public static final String NODE_DB_CONFIG = "db";
	public static final String NODE_CONNETION_POOL = "connection-pool";
	public static final String NODE_CACHE_CONFIG = "cache-config";
	public boolean init() throws Exception{
		if (_isInitiated){
			throw new Exception("SQLServerCacheManager " + _name + " has been initiated");
		}
		
		if (!loadXml()){
			return false;
		}
		
		if (!initDbConnection()){
			return false;
		}
		
		if (!initDbCaches()){
			return false;
		}
		
		if (!proloadData()){
			return false;
		}
		
		return true;
	}
	
	private void stop(){
		if (_connectionPool != null){
			_connectionPool.shutdown();
		}
	}
	
	private boolean loadXml() throws Exception{
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();  
		InputStream xmlInputStream = new FileInputStream(_configFilePath);
		DocumentBuilder builder = builderFactory.newDocumentBuilder(); 
		Document xmlDoc = builder.parse(xmlInputStream);
		
		Element root = xmlDoc.getDocumentElement();
		String rootName = root.getNodeName();
		if (!rootName.equals(NODE_MOZAT_DB)){
			logger.error("XML Root Node Must be " + NODE_MOZAT_DB);
			return false;
		}
		
		NodeList nodeList = root.getChildNodes();
		for (int index = 0; index < nodeList.getLength(); ++index){
			Node configNode = nodeList.item(index);
			String nodeName = configNode.getNodeName();
			
			if (nodeName.startsWith("#")){
				continue;
			}
			
			if (nodeName.equals(NODE_DB_CONFIG)){
				_dbConfig = SqlDbConfig.create(configNode);
			}
			else if (nodeName.equals(NODE_CONNETION_POOL)){
				_dbPoolConfig = SqlDbPoolConfig.create(configNode);
			}
			else if (nodeName.equals(NODE_CACHE_CONFIG)){
				_dbDefaultCacheConfig = SqlDbDefaultCacheConfig.create(configNode);
			}
			else{
				logger.error("Unexpected XML Node: " + nodeName);
			}
		}
		
		boolean res = true;
		if (_dbConfig == null){
			logger.error("db config not specified");
			res = false;
		}
		if (_dbPoolConfig == null){
			logger.error("db connection pool config not specified");
			res = false;
		}
		if (_dbDefaultCacheConfig == null){
			logger.error("table cache config not specified");
			res = false;
		}
		else{
			_tableCacheConfigList = TableCacheConfig.create(_dbDefaultCacheConfig);
		}
		
		return res;
	}
	
	private boolean initDbConnection() throws Exception{
		Class.forName(_dbConfig.dbdriver);

		BoneCPConfig bcpConfig = new BoneCPConfig();
		// db attrs
		//bcpConfig.setJdbcUrl(_dbConfig.url + ";databaseName=" + _dbConfig.dbname);
		bcpConfig.setJdbcUrl(_dbConfig.url);
		bcpConfig.setUser(_dbConfig.username);
		bcpConfig.setPassword(_dbConfig.password);
		
		
		//pool attrs
		bcpConfig.setPartitionCount(_dbPoolConfig.partitionCount);
		bcpConfig.setMaxConnectionsPerPartition(_dbPoolConfig.maxConnectionPerPartition);
		bcpConfig.setMinConnectionsPerPartition(_dbPoolConfig.minConnectionPerPartition);
		bcpConfig.setConnectionTimeout(_dbPoolConfig.connectionTimeout, TimeUnit.SECONDS);
		bcpConfig.setAcquireIncrement(_dbPoolConfig.acquireIncrement);
		bcpConfig.setIdleConnectionTestPeriod(_dbPoolConfig.idleConnectionTestPeriodInSeconds, TimeUnit.SECONDS);
		bcpConfig.setConnectionTimeoutInMs(30000);
		
		_connectionPool = new BoneCP(bcpConfig);
		
		return true;
	}
	
	private boolean initDbCaches() throws Exception{
		for (TableCacheConfig tbCacheConfig: _tableCacheConfigList){
			if (_cacheMap.containsKey(tbCacheConfig.javaClass)){
				logger.error("Duplicated Table: " + tbCacheConfig.javaClass);
				return false;
			}
			
			TableCache tbCache = new TableCache(this, tbCacheConfig);
			if (tbCache.init()){
				_cacheMap.put(tbCacheConfig.javaClass, tbCache);
			}
			else{
				logger.error("Init Table Cache of table " + tbCacheConfig.javaClass + " failed");
				return false;
			}
		}
		return true;
	}
	
	private boolean proloadData(){
		for (Entry<String, TableCache> entry: _cacheMap.entrySet()){
			if (!entry.getValue().preload()){
				return false;
			}
		}
		return true;
	}
	
	private void _logCacheStatus(){
		String log = "[SQLServerCacheManager] DBUrl: " + _dbConfig.url +
				", Database: " + _dbConfig.dbname + 
				", ManagedTableNum: " + _cacheMap.size() +
				", TotalCreatedConnections: " + _connectionPool.getTotalCreatedConnections() +
				", TotalFreeConnections: " + _connectionPool.getTotalFree() +
				", TotalLeasedConnections: " + _connectionPool.getTotalLeased();
		//logger.warn(log);
		DebugLog.info(log);
		
		for (Map.Entry<String, TableCache> entry: _cacheMap.entrySet()){
			entry.getValue().logCacheStatus();
		}
	}
	
	public static void logCacheStatus(){
		try {
			for (Map.Entry<String, SQLServerCacheManager> entry : _managerMap.entrySet()) {
				entry.getValue()._logCacheStatus();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public static void main(String[] args) throws Exception{
		final CacheManager ehCacheManager = CacheManager.create("config_runtime/ehcache.xml");
		//final CacheManager ehCacheManager2 = CacheManager.create("config_runtime/ehcache.xml");
		
		//if (ehCacheManager == ehCacheManager2){
		//	System.out.println("Ehxxxxxxxxxxx");
		//}
		
		SQLServerCacheManager manager = SQLServerCacheManager.create("config_deploy/user_db_cache_config_test.xml");
		
		if (!manager.init()){
			System.out.println("Init sql server cache manager failed");
		}
		
		// todo: test codes below
		
		// shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				SQLServerCacheManager.shutdown();
				ehCacheManager.shutdown();
			}
		});
	}
}

