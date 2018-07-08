package com.mozat.morange.dbcache.core;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mozat.morange.log.DebugLog;

public class SqlDbConfig {
	public static final String NODE_URL = "url";
	public static final String NODE_DBNAME = "dbname";
	public static final String NODE_USERNAME = "username";
	public static final String NODE_PASSWORD = "password";
	public static final String NODE_DBDRIVER = "driver";
	
	// private static final Logger logger = Logger.getLogger(SqlDbConfig.class);
	
	public String url;
	public String dbname;
	public String username;
	public String password;
	public String dbdriver;
	
	private SqlDbConfig(){
	}
	
	public static SqlDbConfig create(Node xmlNode){
		SqlDbConfig config = new SqlDbConfig();
		
		NodeList nodeList = xmlNode.getChildNodes();
		for (int index = 0; index <nodeList.getLength(); ++index){
			Node attr = nodeList.item(index);
			
			if (attr == null || attr.getNodeType() != Node.ELEMENT_NODE){
				continue;
			}
			
			String nodeName = attr.getNodeName();
			String nodeValue = attr.getTextContent();
			
			if (nodeName.startsWith("#")){
				continue;
			}
			
			if (nodeName.equals(NODE_URL)){
				config.url = nodeValue;
			}
			else if (nodeName.equals(NODE_DBNAME)){
				config.dbname = nodeValue;
			}
			else if (nodeName.equals(NODE_USERNAME)){
				config.username = nodeValue;
			}
			else if (nodeName.equals(NODE_PASSWORD)){
				config.password = nodeValue;
			}
			else if (nodeName.equals(NODE_DBDRIVER)){
				config.dbdriver = nodeValue;
			}
			else{
				DebugLog.info("Unhandled xml node: " + nodeName);
			}
		}
		
		return config;
	}
}
