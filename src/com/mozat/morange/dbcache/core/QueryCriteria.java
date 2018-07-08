package com.mozat.morange.dbcache.core;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.log.DebugLog;

public class QueryCriteria {
	public static final int EQ = 0;
	public static final int NE = 1;
	public static final int LT = 2;
	public static final int LE = 3;
	public static final int GT = 4;
	public static final int GE = 5;
	public static final int LIKE = 10;
	
	private static final int AND = 100;
	private static final int OR = 101;
	
	private static final Logger logger = LoggerFactory.getLogger(QueryCriteria.class);
	
	private int _cmpType;
	private String _fieldName;
	private int _fidx;
	private Comparable _input;
	private QueryCriteria[] _compositeQuery;
	
	// for test only
	public QueryCriteria[] getCompositeQuery(){
		return _compositeQuery;
	}
	
	public QueryCriteria(String fieldName, int fidx, int cmpType, Comparable input){
		_fieldName = fieldName;
		_fidx = fidx;
		_cmpType = cmpType;
		_input = input;
	}
	
	private QueryCriteria(int cmpType, QueryCriteria[] compositeQuery){
		_cmpType = cmpType;
		_compositeQuery = compositeQuery;
	}
	
	public static QueryCriteria and(QueryCriteria ...args){
		return new QueryCriteria(AND, args);
	}
	
	public static QueryCriteria or(QueryCriteria ...args){
		return new QueryCriteria(OR, args);
	}
	
	public static String getQuerySQL(String tableName, QueryCriteria[] criteriaList, ArrayList<Object> outArgs){
		String leads = "select * from " + tableName + " where ";
		String conds = toAndSQL(criteriaList, outArgs);
		
		String sql = leads + conds + ";";
		
		return sql;
	}
	
	public static String getDeleteSQL(String tableName, QueryCriteria[] criteriaList, ArrayList<Object> outArgs){
		String leads = "delete from " + tableName + " where ";
		String conds = toAndSQL(criteriaList, outArgs);
		
		String sql = leads + conds + ";";
		
		return sql;
	}
	
	public static boolean fulfills(QueryCriteria[] criteriaList, TableBase obj){
		return andMatches(criteriaList, obj);
	}
	
	private static boolean andMatches(QueryCriteria[] criteriaList, TableBase obj){
		if (criteriaList == null){
			logger.error("[QueryCriteria.andMatches] null criteriaList");
			return false;
		}
		
		for (QueryCriteria qc: criteriaList){
			if (qc == null){
				return false;
			}
			
			switch (qc._cmpType){
			case AND:{
				if (!andMatches(qc._compositeQuery, obj)){
					return false;
				}
			}
			break;
			case OR:{
				if (!orMathces(qc._compositeQuery, obj)){
					return false;
				}
			}
			break;
			default:{
				if (!qc.valueMatches(obj)){
					return false;
				}
			}
			break;
			}
		}
		return true;
	}
	
	private static boolean orMathces(QueryCriteria[] criteriaList, TableBase obj){
		if (criteriaList == null){
			logger.error("[QueryCriteria.orMathces] null criteriaList");
			return false;
		}
		
		for (QueryCriteria qc: criteriaList){
			if (qc == null){
				return false;
			}
			
			switch (qc._cmpType){
			case AND:{
				if (andMatches(qc._compositeQuery, obj)){
					return true;
				}
			}
			break;
			case OR:{
				if (orMathces(qc._compositeQuery, obj)){
					return true;
				}
			}
			break;
			default:{
				if (qc.valueMatches(obj)){
					return true;
				}
			}
			break;
			}
		}
		
		return false;
	}
	
	private boolean valueMatches(TableBase obj){
		try{
			//Class<?> clazz = obj.getClass();
			//Field field = clazz.getDeclaredField(_fieldName);
			//Comparable value = (Comparable)_field.get(obj);
		
			if (_input == null){
				return false;
			}
			
			Comparable value = obj._getValue(this._fidx);
			if (null == value){
				DebugLog.info("[QueryCriteria.valueMatches] Field " + 
						this._fieldName + "(" + this._fidx + ") not found.");
				return false;
			}
			
			// like. For string Attributes only...
			if (_cmpType == LIKE){
				if (!_input.getClass().equals(String.class)){
					return false;
				}
				
				String left = (String)value;
				String right = (String)_input;
				
				if (left.indexOf(right) < 0){
					return false;
				}
				
				return true;
			}
			
			// logic operations
			int cmpRes = value.compareTo(_input);
			switch (_cmpType){
			case EQ:
				if (cmpRes == 0){return true;}
				break;
			case NE:
				if (cmpRes != 0){return true;}
				break;
			case LT:
				if (cmpRes < 0){return true;}
				break;
			case LE:
				if (cmpRes <= 0){return true;}
				break;
			case GT:
				if (cmpRes > 0){return true;}
				break;
			case GE:
				if (cmpRes >= 0){return true;}
				break;
			}
		}
		catch (Exception ex){
			logger.error("[Compare Error] field: " + _fieldName + ", input: " + _input + ", ex: ", ex);
			return false;
		}
		
		return false;
	}
	
	public String toCondSQL(ArrayList<Object> outArgs){
		String oper = "";
		
		switch (_cmpType){
		case EQ:
			oper = "=";
			break;
		case NE:
			oper = "!=";
			break;
		case LT:
			oper = "<";
			break;
		case LE:
			oper = "<=";
			break;
		case GT:
			oper = ">";
			break;
		case GE:
			oper = ">=";
			break;
		case LIKE:
			oper = "LIKE";
		}
		
		if (oper.isEmpty()){
			return "";
		}
		
		String res = "";
		if (_cmpType != LIKE){
			outArgs.add(_input);
			
			//res = "([" + _fieldName + "] " + oper + " ?)";//SQLServer
			res = "(`" + _fieldName + "`" + oper + " ?)";   //MySQL
		}
		else{
			String word = _input.toString().replaceAll("%", "[%]").replaceAll("_", "[_]");
			//res = "([" +  _fieldName + "] " + oper + "'%" + word + "%')";//SQLServer
			res = "(`" +  _fieldName + "`" + oper + "'%" + word + "%')";   //MySQL
			
		}
		
		return res;
	}
	
	public static String toAndSQL(QueryCriteria[] criteriaList, ArrayList<Object> outArgs){
		String sql = "";
		for (QueryCriteria qc: criteriaList){
			if (sql.length() > 0){
				sql += "AND";
			}
			switch (qc._cmpType){
			case AND:{
				sql += toAndSQL(qc._compositeQuery, outArgs);
			}
			break;
			case OR:{
				sql += toOrSQL(qc._compositeQuery, outArgs);
			}
			break;
			default:{
				sql += qc.toCondSQL(outArgs);
			}
			break;
			}
		}
		
		if (criteriaList.length > 1){
			sql = "(" + sql + ")";
		}
		
		return sql;
	}
	
	public static String toOrSQL(QueryCriteria[] criteriaList, ArrayList<Object> outArgs){
		String sql = "";
		for (QueryCriteria qc: criteriaList){
			if (sql.length() > 0){
				sql += "OR";
			}
			switch (qc._cmpType){
			case AND:{
				sql += toAndSQL(qc._compositeQuery, outArgs);
			}
			break;
			case OR:{
				sql += toOrSQL(qc._compositeQuery, outArgs);
			}
			break;
			default:{
				sql += qc.toCondSQL(outArgs);
			}
			break;
			}
		}
		
		if (criteriaList.length > 1){
			sql = "(" + sql + ")";
		}
		
		return sql;
	}
}
