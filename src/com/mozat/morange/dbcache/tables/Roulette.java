/*
* @Brief: Java Persistence Class (JPC) for Database Table.
*
* @Class: Roulette
* @Namespace: com.mozat.morange.dbcache.tables
* @Table: ROULETTE
* @Primary Key: ID
* @Identity: ID
* @Unique Index: [u'ID']
*
* @Caution: This file is Auto Generated.
*     DO NOT modify this file EVEN IF you know what you are doing.
*/

package com.mozat.morange.dbcache.tables;

import java.util.ArrayList;
import java.util.HashMap;
import com.mozat.morange.dbcache.core.TableBase;
import com.mozat.morange.dbcache.core.TableCache;
import com.mozat.morange.dbcache.core.QueryCriteria;

public final class Roulette extends TableBase implements java.io.Serializable{
    // Serializable ID
    private static final long serialVersionUID = 1L;

    private static TableCache _tableCache;
    private static String _tableName;

    private static boolean _isIdAutoGenerated = true;

    /**
    * This method is used by the engine.<br>
    * DON'T use this method under any circumstances.
    */
    @Override
    public TableCache _getTableCache(){return _tableCache;}

    /**
    * Never manually run SQL statements using TableCache Object.<br>
    * Think twice before hand-writing ANY SQL statements, and use it carefully.
    */
    @Deprecated
    public static TableCache __getTableCache(){return _tableCache;}

    @Override
    public String _getTableName(){return _tableName;}

    public static String __getTableName(){return _tableName;}

    public static void setCacheAndName(TableCache cache, String name){
        _tableCache = cache;
        _tableName = name;
    }

    @Override
    public Long _getIdValue(){
        Integer _idVal = this.ID;
        return _idVal.longValue();
    }

    @Override
    public void _setIdValue(Long _val){
        this.ID = _val.intValue();
    }

    @Override
    public boolean _isIdAutoGenerated(){
        return _isIdAutoGenerated;
    }


    // SQL templates
    private static final String _updateSQL = "UPDATE ROULETTE SET `ROOM_ID` = ?, `BEGIN_BET_TIME` = ?, `END_BET_TIME` = ?, `BET_TOTAL` = ?, `REWARD_TIME` = ?, `RESULT` = ?, `REWARD_TOTAL` = ? WHERE `ID` = ?;";
    private static final String _querySQL = "SELECT * FROM ROULETTE WHERE `ID` = ?;";
    private static final String _createSQL = "INSERT INTO ROULETTE (`ROOM_ID`, `BEGIN_BET_TIME`, `END_BET_TIME`, `BET_TOTAL`, `REWARD_TIME`, `RESULT`, `REWARD_TOTAL`) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String _deleteSQL = "DELETE FROM ROULETTE WHERE `ID` = ?;";

    // Field Map
    private static HashMap<String, Integer> AttrFieldMap;
    private static HashMap<String, Integer> getFieldMap(){
        if (AttrFieldMap == null){
            AttrFieldMap = new HashMap<String, Integer>();
            AttrFieldMap.put("ID", 1);
            AttrFieldMap.put("ROOM_ID", 2);
            AttrFieldMap.put("BEGIN_BET_TIME", 3);
            AttrFieldMap.put("END_BET_TIME", 4);
            AttrFieldMap.put("BET_TOTAL", 5);
            AttrFieldMap.put("REWARD_TIME", 6);
            AttrFieldMap.put("RESULT", 7);
            AttrFieldMap.put("REWARD_TOTAL", 8);
        }

        return AttrFieldMap;
    }

    // Attributes Wrapper for Query
    public static final TableBase.Attribute<Integer> AttrID = new TableBase.Attribute<Integer>("ID", Roulette.class, 1);
    public static final TableBase.AttributeS AttrROOM_ID = new TableBase.AttributeS("ROOM_ID", Roulette.class, 2);
    public static final TableBase.Attribute<java.util.Date> AttrBEGIN_BET_TIME = new TableBase.Attribute<java.util.Date>("BEGIN_BET_TIME", Roulette.class, 3);
    public static final TableBase.Attribute<java.util.Date> AttrEND_BET_TIME = new TableBase.Attribute<java.util.Date>("END_BET_TIME", Roulette.class, 4);
    public static final TableBase.Attribute<Integer> AttrBET_TOTAL = new TableBase.Attribute<Integer>("BET_TOTAL", Roulette.class, 5);
    public static final TableBase.Attribute<java.util.Date> AttrREWARD_TIME = new TableBase.Attribute<java.util.Date>("REWARD_TIME", Roulette.class, 6);
    public static final TableBase.Attribute<Integer> AttrRESULT = new TableBase.Attribute<Integer>("RESULT", Roulette.class, 7);
    public static final TableBase.Attribute<Integer> AttrREWARD_TOTAL = new TableBase.Attribute<Integer>("REWARD_TOTAL", Roulette.class, 8);

    public static TableBase.AttributeValue<?>[] valueList(TableBase.AttributeValue<?>...valList){
        return valList;
    }

    // Member Fields.
    public int ID = 0;
    public String ROOM_ID = "";
    public java.util.Date BEGIN_BET_TIME = new java.util.Date(0);
    public java.util.Date END_BET_TIME = new java.util.Date(0);
    public int BET_TOTAL = 0;
    public java.util.Date REWARD_TIME = new java.util.Date(0);
    public int RESULT = 0;
    public int REWARD_TOTAL = 0;

    // DON'T Invoke This Method Manually.
    @Override
    public HashMap<String, Integer> _getFieldMap(){
        return Roulette.getFieldMap();
    }

    // DON'T Invoke This Method Manually.
    @Override
    public void _setValue(int __fidx, Object __val){
        switch (__fidx){
        case 1: {this.ID = (Integer)__val; break;}
        case 2: {this.ROOM_ID = (String)__val; break;}
        case 3: {this.BEGIN_BET_TIME = (java.util.Date)__val; break;}
        case 4: {this.END_BET_TIME = (java.util.Date)__val; break;}
        case 5: {this.BET_TOTAL = (Integer)__val; break;}
        case 6: {this.REWARD_TIME = (java.util.Date)__val; break;}
        case 7: {this.RESULT = (Integer)__val; break;}
        case 8: {this.REWARD_TOTAL = (Integer)__val; break;}
        }
    }

    // DON'T Invoke This Method Manually.
    @Override
    @SuppressWarnings("rawtypes")
    public Comparable _getValue(int __fidx){
        switch (__fidx){
        case 1: {return this.ID;}
        case 2: {return this.ROOM_ID;}
        case 3: {return this.BEGIN_BET_TIME;}
        case 4: {return this.END_BET_TIME;}
        case 5: {return this.BET_TOTAL;}
        case 6: {return this.REWARD_TIME;}
        case 7: {return this.RESULT;}
        case 8: {return this.REWARD_TOTAL;}
        }
        return null;
    }

    /**
    * Default constructor for JavaBean convention
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public Roulette(){}

    /**
    * Constructor for creating a new instance.
    * Arguments are index keys
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public Roulette(int ID){
        this.ID = ID;
    }

    @Override
    public String _getCacheKey(){
        return _genKey(ID);
    }

    public static ArrayList<Roulette> getAllObjects(){
        ArrayList<Object> __tmp = _tableCache.loadAll();

        ArrayList<Roulette> __res = new ArrayList<Roulette>();
        for (Object __obj: __tmp){
            __res.add((Roulette)__obj);
        }

        return __res;
    }

    /**
    * update an JPO to database.
    * @return return true if updated successfully, otherwise return false
    */
    @Override
    public boolean update(){
        int __res = _tableCache.update(this, _updateSQL, 
            ROOM_ID, BEGIN_BET_TIME, END_BET_TIME, BET_TOTAL, 
            REWARD_TIME, RESULT, REWARD_TOTAL, ID);

        return (__res > 0);
    }

    /**
    * update JPO in batch.
    * @return num of records successfully updated.
    */
    public static int updateBatch(Roulette[] objList){
        Object[][] argv = new Object[objList.length][];

        for (int idx = 0; idx < objList.length; idx++){
            Roulette o = objList[idx];
            Object[] args = new Object[]{
                o.ROOM_ID, o.BEGIN_BET_TIME, o.END_BET_TIME, o.BET_TOTAL, 
                o.REWARD_TIME, o.RESULT, o.REWARD_TOTAL, o.ID};

            argv[idx] = args;
        }

        return _tableCache.updateBatch(objList, _updateSQL, argv);
    }

    /**
    * update JPO by QueryCriteria.
    * @return num of records successfully updated.
    */
    public static int updateByCriteria(TableBase.AttributeValue<?>[] valueList, QueryCriteria ...qcList){
        return _tableCache.updateByCriteria(valueList, qcList);
    }

    /**
    * remove an JPO from database and cache.
    * @return true if successfully removed, otherwise return false
    */
    @Override
    public boolean remove(){
        int __res = _tableCache.remove(this, _deleteSQL, ID);
        return (__res > 0);
    }

    /**
    * remove JPOs in batch from database and cache.
    * @return num of records successfully removed.
    */
    public static int removeBatch(Roulette[] objList){
        Object argv[] = new Object[objList.length];

        for (int idx = 0; idx < objList.length; idx++){
            argv[idx] = objList[idx].ID;
        }

        int res = _tableCache.removeBatch(objList, _deleteSQL, argv);
        return res;
    }

    /**
    * remove JPOs from database and cache by criteria.
    * @return num of records removed.
    */
    public static int removeByCriteria(QueryCriteria qc, QueryCriteria ...moreQcs){
        return _tableCache.removeByCriteria(qc, moreQcs);
    }

    /**
    * remove all JPOs from database and cache.
    * CAUTION: this operation is irrevocable.
    * @return num of records removed.
    */
    public static int __removeAll(){
        return _tableCache.removeAll();
    }

    /**
    * create a new JPO.<br>
    * If the a record with the same index in the cache is found,<br>
    * the cached object is return, and no DB record is created.<br>
    * Otherwise a new record is created and return, and a DB record is created.
    * @return an JPO if successfully created, null otherwise.
    */
    public static Roulette create(){

        Roulette __newObj = new Roulette();

        return (Roulette) _tableCache.create(__newObj, _createSQL, 
            __newObj.ROOM_ID, __newObj.BEGIN_BET_TIME, __newObj.END_BET_TIME, __newObj.BET_TOTAL, 
            __newObj.REWARD_TIME, __newObj.RESULT, __newObj.REWARD_TOTAL);
    }

    /**
    * Create a new JPO with initial values.<br>
    * Non-auto-incremental index is required<br>
    * If a field is set for more than one time<br>,
    * subsequent set will surpass preceding set.<br>
    * If the a record with the same index in the cache is found,<br>
    * the cached object is return, and no DB record is created.<br>
    * Otherwise a new record is created and return, and a DB record is created.
    * @return an JPO if successfully created, null otherwise.
    */
    public static Roulette create(
            TableBase.AttributeValue<?> attrVal,
            TableBase.AttributeValue<?>... attrVals){

        Roulette __newObj = new Roulette();

        if (null != attrVal.getVal()){
            __newObj._setValue(attrVal.getFidx(), attrVal.getVal());
        }

        for (TableBase.AttributeValue<?> attrV: attrVals){
            if (null != attrV.getVal()){
                __newObj._setValue(attrV.getFidx(), attrV.getVal());
            }
        }

        return (Roulette) _tableCache.create(__newObj, _createSQL, 
            __newObj.ROOM_ID, __newObj.BEGIN_BET_TIME, __newObj.END_BET_TIME, __newObj.BET_TOTAL, 
            __newObj.REWARD_TIME, __newObj.RESULT, __newObj.REWARD_TOTAL);
    }

    /**
    * find a JPO by indexes.
    * @return the found JPO or null
    */
    public static Roulette getOne(int ID){
        return (Roulette) _tableCache.getOne(_genKey(ID), _querySQL, ID);
    }

    /**
    * find a JPO by arbitrary query criteria.
    * @return the found JPO or null
    */
    public static Roulette getOneByCriteria(QueryCriteria ...criteria){
        return (Roulette)_tableCache.getOneByCriteria(criteria);
    }

    /**
    * find all JPOs those match the query criteria list.
    * @return a list of JPO found.
    */
    public static ArrayList<Roulette> getManyByCriteria(QueryCriteria ...criteria){
        ArrayList<Object> __tmp = _tableCache.getManyByCriteria(criteria);

        ArrayList<Roulette> __res = new ArrayList<Roulette>();
        for (Object __obj: __tmp){
            __res.add((Roulette)__obj);
        }

        return __res;
    }

    /**
    * find a JPO by arbitrary SQL statement.
    * @return return the found JPO or null.
    */
    public static Roulette getOneBySQL(String sql, Object ...args){
        return (Roulette)_tableCache.getOneBySQL(sql, args);
    }

    /**
    * find all JPOs by arbitrary SQL statement.
    * @return a list of JPO found.
    */
    public static ArrayList<Roulette> getManyBySQL(String sql, Object ...args){
        ArrayList<Object> __tmp = _tableCache.getManyBySQL(sql, args);

        ArrayList<Roulette> __res = new ArrayList<Roulette>();
        for (Object __obj: __tmp){
            __res.add((Roulette)__obj);
        }

        return __res;
    }

    /**
    * query objects from database by specified class Type
    * @return a list of OBJECT found.
    */
    public static <T> ArrayList<T> getManyByArbitarySQL(Class<T> clazz, String sql, Object[] args){
        return _tableCache._queryByArbitarySQL(clazz, sql, args);
    }

}
