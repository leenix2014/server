package com.mozat.morange.dbcache.core;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mozat.morange.log.DebugLog;


public class SqlDbDefaultCacheConfig {
	public static final String NODE_DEFAULT_SETTINGS = "defualt-settings";
	public static final String NODE_NAMESPACE = "namespace";
	public static final String NODE_CATEGORY = "category";
	public static final String NODE_MAX_ELEMTS_IN_MEMORY = "maxElementsInMemory";
	public static final String NOED_ETERNAL = "eternal";
	public static final String NODE_OVERFLOW_TO_DISK = "overflowToDisk";
	public static final String NODE_TIME_TO_IDLE_SECONDS = "timeToIdleSeconds";
	public static final String NODE_TIME_TO_LIVE_SECONDS = "timeToLiveSeconds";
	public static final String NODE_DISK_PERSISTENT = "diskPersistent";
	public static final String NODE_DISK_EXPIRY_THREAD_INTERVAL_SECONDS = "diskExpiryThreadIntervalSeconds";
	public static final String NODE_MEMORY_STORE_EVICTION_POLICY = "memoryStoreEvictionPolicy";
	
	private static final String NODE_TABLE_CONFIG_URLS = "table-config-urls";
	private static final String NODE_URL = "url";
	
	// private static final Logger logger = Logger.getLogger(SqlDbDefaultCacheConfig.class);
	
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
	
	public ArrayList<String> tableCacheUrlList = new ArrayList<String>();
	
	private SqlDbDefaultCacheConfig(){
	}
	
	public static SqlDbDefaultCacheConfig create(Node xmlNode){
		SqlDbDefaultCacheConfig config = new SqlDbDefaultCacheConfig();
		
		NodeList nodeList = xmlNode.getChildNodes();
		for (int index = 0; index < nodeList.getLength(); ++index){
            Node attr = nodeList.item(index);
			if (attr == null || attr.getNodeType() != Node.ELEMENT_NODE){
				continue;
			}
			
			String nodeName = attr.getNodeName();
			if (nodeName.startsWith("#")){
				continue;
			}
			
			if (nodeName.equals(NODE_DEFAULT_SETTINGS)){
				config.initDefaultSettings(attr);
			}
			else if (nodeName.equals(NODE_TABLE_CONFIG_URLS)){
				config.initTableConfigUrls(attr);
			}
			else{
				DebugLog.info("Unhandled XML Node: " + nodeName);
			}
		}
		
		return config;
	}
	
	private void initDefaultSettings(Node xmlNode){
		NodeList nodeList = xmlNode.getChildNodes();
		
		for (int index = 0; index < nodeList.getLength(); ++index){
            Node attr = nodeList.item(index);
            
			if (attr == null || attr.getNodeType() != Node.ELEMENT_NODE){
				continue;
			}
			
			String nodeName = attr.getNodeName();
			String nodeValue = attr.getTextContent();
			if (nodeName.equals(NODE_NAMESPACE)){
				this.namespace = nodeValue;
			}
			else if (nodeName.equals(NODE_CATEGORY)){
				if (nodeValue.equals("full") || nodeValue.equals("lazy") || nodeValue.equals("none")){
					this.category = nodeValue;
				}
				else{
					this.category = "lazy";
					
					DebugLog.info("Unexpected cache Category: " + nodeValue);
				}
			}
			else if (nodeName.equals(NODE_MAX_ELEMTS_IN_MEMORY)){
				this.maxElementInMenory = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals(NOED_ETERNAL)){
				this.eternal = toBoolean(nodeValue);
			}
			else if (nodeName.equals(NODE_OVERFLOW_TO_DISK)){
				this.overflowToDisk = toBoolean(nodeValue);
			}
			else if (nodeName.equals(NODE_TIME_TO_IDLE_SECONDS)){
				this.timeToIdleSeconds = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals(NODE_TIME_TO_LIVE_SECONDS)){
				this.timeToLiveSeconds = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals(NODE_DISK_PERSISTENT)){
				this.diskPersistent = toBoolean(nodeValue);
			}
			else if (nodeName.equals(NODE_DISK_EXPIRY_THREAD_INTERVAL_SECONDS)){
				this.diskExpiryThreadIntervalSeconds = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals(NODE_MEMORY_STORE_EVICTION_POLICY)){
				this.memoryStoreEvictionPolicy = nodeValue;
			}
			else{
				DebugLog.info("Unhandled XML Node: " + nodeName);
			}
		}	
	}
	
	private void initTableConfigUrls(Node xmlNode){
		NodeList nodeList = xmlNode.getChildNodes();
		
		for (int index = 0; index < nodeList.getLength(); ++index){
            Node attr = nodeList.item(index);
            
			if (attr == null || attr.getNodeType() != Node.ELEMENT_NODE){
				continue;
			}
			
			String nodeName = attr.getNodeName();
			if (!nodeName.equals(NODE_URL)){
				DebugLog.info("Unhandled XML Node: table-config-urls/" + nodeName);
				continue;
			}
			
			this.tableCacheUrlList.add(attr.getTextContent());
		}
	}
	
	public static boolean toBoolean(String s){
		if (s.toLowerCase().equals("true")){
			return true;
		}
		
		return false;
	}
}
