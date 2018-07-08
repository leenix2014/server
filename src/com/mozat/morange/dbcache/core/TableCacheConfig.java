package com.mozat.morange.dbcache.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mozat.morange.log.DebugLog;


public class TableCacheConfig {
	public static final String NODE_ROOT = "tables";
	public static final String NODE_TABLE = "table";
	
	public static final String NODE_NAME = "name";
	public static final String NODE_CLASS = "class";
	public static final String NODE_ID = "id";
	public static final String NODE_INDEX = "index";
	
	private static final Logger logger = LoggerFactory.getLogger(TableCacheConfig.class);
	
	// table info
	public String tableName;
	public String javaClass;
	public String idColumn;
	public String[] indexList;
	public boolean isPkInIndex = false;
	
	// cache related
	public String namespace;
	public String category;
	public int maxElementInMenory;
	public boolean eternal;
	public boolean overflowToDisk;
	public int timeToIdleSeconds;
	public int timeToLiveSeconds;
	public boolean diskPersistent;
	public int diskExpiryThreadIntervalSeconds;
	public String memoryStoreEvictionPolicy;
	
	private TableCacheConfig(SqlDbDefaultCacheConfig defaultSetting){
		namespace = defaultSetting.namespace;
		category = defaultSetting.category;
		maxElementInMenory = defaultSetting.maxElementInMenory;
		eternal = defaultSetting.eternal;
		overflowToDisk = defaultSetting.overflowToDisk;
		timeToIdleSeconds = defaultSetting.timeToIdleSeconds;
		timeToLiveSeconds = defaultSetting.timeToLiveSeconds;
		diskPersistent = defaultSetting.diskPersistent;
		diskExpiryThreadIntervalSeconds = defaultSetting.diskExpiryThreadIntervalSeconds;
		memoryStoreEvictionPolicy = defaultSetting.memoryStoreEvictionPolicy;
	}
	
	private void init(Node xmlNode) throws Exception{
		NamedNodeMap attrMap = xmlNode.getAttributes();
		for (int index = 0; index < attrMap.getLength(); ++index){
			Node tag = attrMap.item(index);
			String attrName = tag.getNodeName();
			String attrValue = tag.getNodeValue();
			
			if (attrName.startsWith("#")){
				continue;
			}
			
			if (attrName.equals(NODE_NAME)){
				this.tableName = attrValue;
			}
			else if (attrName.equals(NODE_CLASS)){
				this.javaClass = attrValue;
				if (!namespace.isEmpty() && (this.javaClass.indexOf('.') < 0)){
					this.javaClass = namespace + "." + this.javaClass;
				}
			}
			else if (attrName.equals(NODE_ID)){
				this.idColumn = attrValue;
			}
			else if (attrName.equals(NODE_INDEX)){
				this.indexList = attrValue.split("\\+");
			}
			else if (attrName.equals(SqlDbDefaultCacheConfig.NODE_NAMESPACE)){
				this.namespace = attrValue;
			}
			else if (attrName.equals(SqlDbDefaultCacheConfig.NODE_CATEGORY)){
				if (attrValue.equals("full") || attrValue.equals("lazy") || attrValue.equals("none")){
					this.category = attrValue;
				}
				else{
					this.category = "lazy";
					
					logger.error("Unexpected cache Category: " + attrValue);
				}
			}
			else if (attrName.equals(SqlDbDefaultCacheConfig.NODE_MAX_ELEMTS_IN_MEMORY)){
				this.maxElementInMenory = Integer.parseInt(attrValue);
			}
			else if (attrName.equals(SqlDbDefaultCacheConfig.NOED_ETERNAL)){
				this.eternal = SqlDbDefaultCacheConfig.toBoolean(attrValue);
			}
			else if (attrName.equals(SqlDbDefaultCacheConfig.NODE_OVERFLOW_TO_DISK)){
				this.overflowToDisk = SqlDbDefaultCacheConfig.toBoolean(attrValue);
			}
			else if (attrName.equals(SqlDbDefaultCacheConfig.NODE_TIME_TO_IDLE_SECONDS)){
				this.timeToIdleSeconds = Integer.parseInt(attrValue);
			}
			else if (attrName.equals(SqlDbDefaultCacheConfig.NODE_TIME_TO_LIVE_SECONDS)){
				this.timeToLiveSeconds = Integer.parseInt(attrValue);
			}
			else if (attrName.equals(SqlDbDefaultCacheConfig.NODE_DISK_PERSISTENT)){
				this.diskPersistent = SqlDbDefaultCacheConfig.toBoolean(attrValue);
			}
			else if (attrName.equals(SqlDbDefaultCacheConfig.NODE_DISK_EXPIRY_THREAD_INTERVAL_SECONDS)){
				this.diskExpiryThreadIntervalSeconds = Integer.parseInt(attrValue);
			}
			else if (attrName.equals(SqlDbDefaultCacheConfig.NODE_MEMORY_STORE_EVICTION_POLICY)){
				this.memoryStoreEvictionPolicy = attrValue;
			}
			else{
				logger.error("Unhandled XML Attribute: " + attrName);
			}
		}
		
		if (this.tableName == null){
			throw new Exception("[TableCacheConfig.init] table name not specified.");
		}
		
		if (this.idColumn == null){
			throw new Exception("[TableCacheConfig.init] id not specified.");
		}
		
		if (this.indexList == null || this.indexList.length == 0){
			throw new Exception("[TableCacheConfig.init] indexes not specified.");
		}
		
		if (this.javaClass == null){
			this.javaClass = "T" + this.tableName.substring(0, 1).toUpperCase();
			this.javaClass += this.tableName.substring(1);
			if (!namespace.isEmpty() && (this.javaClass.indexOf('.') < 0)){
				this.javaClass = namespace + "." + this.javaClass;
			}
		}
		
		for (String idx: indexList){
			if (idx.equals(idColumn)){
				this.isPkInIndex = true;
			}
		}
	}
	
	public boolean isFullCache(){
		return category.equals("full");
	}
	public boolean isLazyCache(){
		return category.equals("lazy");
	}
	public boolean isNoneCache(){
		return category.equals("none");
	}
	
	public static ArrayList<TableCacheConfig> create(SqlDbDefaultCacheConfig defaultCacheConfig) throws Exception{
		ArrayList<TableCacheConfig> tableCacheConfigList = new ArrayList<TableCacheConfig>();
		
		for (int index = 0; index < defaultCacheConfig.tableCacheUrlList.size(); ++index){
			String cacheConfigFile = defaultCacheConfig.tableCacheUrlList.get(index);
			
			TableCacheConfig.create(defaultCacheConfig, cacheConfigFile, tableCacheConfigList);
		}
		
		return tableCacheConfigList;
	}
	
	private static void create(SqlDbDefaultCacheConfig defaultCacheConfig, 
			String cacheConfigFile, 
			ArrayList<TableCacheConfig> tableCacheConfigList) throws Exception
	{
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();  
		
		InputStream xmlInputStream = new FileInputStream(cacheConfigFile);
		DocumentBuilder builder = builderFactory.newDocumentBuilder(); 
		Document xmlDoc = builder.parse(xmlInputStream);
		
		Element root = xmlDoc.getDocumentElement();
		if (null != root){
			String rootName = root.getNodeName();
			if (!rootName.equals(NODE_ROOT)){
				logger.error("Loading xml error: root node is not <tables>");
				return;
			}
			
			NodeList nodeList = root.getChildNodes();
			for (int index = 0; index < nodeList.getLength(); ++index){
				Node tableNode = nodeList.item(index);
				String nodeName = tableNode.getNodeName();
				if (nodeName.startsWith("#")){
					continue;
				}
				
				if (nodeName.equals(NODE_TABLE)){
					TableCacheConfig cacheConfig = new TableCacheConfig(defaultCacheConfig);
					cacheConfig.init(tableNode);
					tableCacheConfigList.add(cacheConfig);
				}
				else{
					DebugLog.info("Unexpected Node: " + nodeName);
				}
			}
		}
	}
}
