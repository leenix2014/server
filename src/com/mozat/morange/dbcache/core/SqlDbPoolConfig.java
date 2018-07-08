package com.mozat.morange.dbcache.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SqlDbPoolConfig {
	public static final String NODE_PARTITION_COUNT = "partitionCount";
	public static final String NODE_MAX_CONNECTION_PER_PARTITION = "maxConnectionsPerPartition";
	public static final String NODE_MIN_CONNECTION_PER_PARTITION = "minConnectionsPerPartition";
	public static final String NODE_CONNETION_TIMEOUT = "connectionTimeout";
	public static final String NODE_ACQUIRE_INCREMENT = "acquireIncrement";
	public static final String NODE_IDEL_CONNECTION_TEST_PERIED_IN_SECONDS = "idleConnectionTestPeriodInSeconds";
	
	private static final Logger logger = LoggerFactory.getLogger(SqlDbPoolConfig.class);
	
	public int partitionCount = 2;
	public int maxConnectionPerPartition = 5;
	public int minConnectionPerPartition = 1;
	public int connectionTimeout = 60;
	public int acquireIncrement = 1;
	public int idleConnectionTestPeriodInSeconds = 60;
	
	private SqlDbPoolConfig(){
	}
	
	public static SqlDbPoolConfig create(Node xmlNode){
		SqlDbPoolConfig config = new SqlDbPoolConfig();
		
		NodeList nodeList = xmlNode.getChildNodes();
		for (int index = 0; index < nodeList.getLength(); ++index){
			Node attr = nodeList.item(index);
			if (attr == null || attr.getNodeType() != Node.ELEMENT_NODE){
				continue;
			}
			
			String nodeName = attr.getNodeName();
			String nodeValue = attr.getTextContent();
			
			if (nodeName.startsWith("#")){
				continue;
			}
			
			if (nodeName.equals(NODE_PARTITION_COUNT)){
				if (nodeValue.equals("auto")){
					//config.partitionCount = Runtime.getRuntime().availableProcessors();
				}
				else{
					config.partitionCount = Integer.parseInt(nodeValue);
				}
			}
			else if (nodeName.equals(NODE_MAX_CONNECTION_PER_PARTITION)){
				config.maxConnectionPerPartition = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals(NODE_MIN_CONNECTION_PER_PARTITION)){
				config.minConnectionPerPartition = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals(NODE_CONNETION_TIMEOUT)){
				config.connectionTimeout = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals(NODE_ACQUIRE_INCREMENT)){
				config.connectionTimeout = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals(NODE_IDEL_CONNECTION_TEST_PERIED_IN_SECONDS)){
				config.idleConnectionTestPeriodInSeconds = Integer.parseInt(nodeValue);
			}
			else{
				logger.error("Unhandled xml node: " + nodeName);
			}
		}
		
		return config;
	}
}
