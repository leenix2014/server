/*
* @Brief: Java Persistence Class (JPC) for Database Table.
*
* @Class: PrizeRecord
* @Namespace: com.mozat.morange.dbcache.tables
* @Table: PRIZE_RECORD
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

public final class PrizeRecord extends TableBase implements java.io.Serializable{
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
    private static final String _updateSQL = "UPDATE PRIZE_RECORD SET `USER_ID` = ?, `USER_NAME` = ?, `DRAW_TIME` = ?, `PRIZE_ID` = ?, `PRIZE_NAME` = ?, `DRAW_COUNT` = ? WHERE `ID` = ?;";
    private static final String _querySQL = "SELECT * FROM PRIZE_RECORD WHERE `ID` = ?;";
    private static final String _createSQL = "INSERT INTO PRIZE_RECORD (`USER_ID`, `USER_NAME`, `DRAW_TIME`, `PRIZE_ID`, `PRIZE_NAME`, `DRAW_COUNT`) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String _deleteSQL = "DELETE FROM PRIZE_RECORD WHERE `ID` = ?;";

    // Field Map
    private static HashMap<String, Integer> AttrFieldMap;
    private static HashMap<String, Integer> getFieldMap(){
        if (AttrFieldMap == null){
            AttrFieldMap = new HashMap<String, Integer>();
            AttrFieldMap.put("ID", 1);
            AttrFieldMap.put("USER_ID", 2);
            AttrFieldMap.put("USER_NAME", 3);
            AttrFieldMap.put("DRAW_TIME", 4);
            AttrFieldMap.put("PRIZE_ID", 5);
            AttrFieldMap.put("PRIZE_NAME", 6);
            AttrFieldMap.put("DRAW_COUNT", 7);
        }

        return AttrFieldMap;
    }

    // Attributes Wrapper for Query
    public static final TableBase.Attribute<Integer> AttrID = new TableBase.Attribute<Integer>("ID", PrizeRecord.class, 1);
    public static final TableBase.AttributeS AttrUSER_ID = new TableBase.AttributeS("USER_ID", PrizeRecord.class, 2);
    public static final TableBase.AttributeS AttrUSER_NAME = new TableBase.AttributeS("USER_NAME", PrizeRecord.class, 3);
    public static final TableBase.Attribute<java.util.Date> AttrDRAW_TIME = new TableBase.Attribute<java.util.Date>("DRAW_TIME", PrizeRecord.class, 4);
    public static final TableBase.Attribute<Integer> AttrPRIZE_ID = new TableBase.Attribute<Integer>("PRIZE_ID", PrizeRecord.class, 5);
    public static final TableBase.AttributeS AttrPRIZE_NAME = new TableBase.AttributeS("PRIZE_NAME", PrizeRecord.class, 6);
    public static final TableBase.Attribute<Integer> AttrDRAW_COUNT = new TableBase.Attribute<Integer>("DRAW_COUNT", PrizeRecord.class, 7);

    public static TableBase.AttributeValue<?>[] valueList(TableBase.AttributeValue<?>...valList){
        return valList;
    }

    // Member Fields.
    public int ID = 0;
    public String USER_ID = "";
    public String USER_NAME = "";
    public java.util.Date DRAW_TIME = new java.util.Date(0);
    public int PRIZE_ID = 0;
    public String PRIZE_NAME = "";
    public int DRAW_COUNT = 0;

    // DON'T Invoke This Method Manually.
    @Override
    public HashMap<String, Integer> _getFieldMap(){
        return PrizeRecord.getFieldMap();
    }

    // DON'T Invoke This Method Manually.
    @Override
    public void _setValue(int __fidx, Object __val){
        switch (__fidx){
        case 1: {this.ID = (Integer)__val; break;}
        case 2: {this.USER_ID = (String)__val; break;}
        case 3: {this.USER_NAME = (String)__val; break;}
        case 4: {this.DRAW_TIME = (java.util.Date)__val; break;}
        case 5: {this.PRIZE_ID = (Integer)__val; break;}
        case 6: {this.PRIZE_NAME = (String)__val; break;}
        case 7: {this.DRAW_COUNT = (Integer)__val; break;}
        }
    }

    // DON'T Invoke This Method Manually.
    @Override
    @SuppressWarnings("rawtypes")
    public Comparable _getValue(int __fidx){
        switch (__fidx){
        case 1: {return this.ID;}
        case 2: {return this.USER_ID;}
        case 3: {return this.USER_NAME;}
        case 4: {return this.DRAW_TIME;}
        case 5: {return this.PRIZE_ID;}
        case 6: {return this.PRIZE_NAME;}
        case 7: {return this.DRAW_COUNT;}
        }
        return null;
    }

    /**
    * Default constructor for JavaBean convention
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public PrizeRecord(){}

    /**
    * Constructor for creating a new instance.
    * Arguments are index keys
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public PrizeRecord(int ID){
        this.ID = ID;
    }

    @Override
    public String _getCacheKey(){
        return _genKey(ID);
    }

    public static ArrayList<PrizeRecord> getAllObjects(){
        ArrayList<Object> __tmp = _tableCache.loadAll();

        ArrayList<PrizeRecord> __res = new ArrayList<PrizeRecord>();
        for (Object __obj: __tmp){
            __res.add((PrizeRecord)__obj);
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
            USER_ID, USER_NAME, DRAW_TIME, PRIZE_ID, 
            PRIZE_NAME, DRAW_COUNT, ID);

        return (__res > 0);
    }

    /**
    * update JPO in batch.
    * @return num of records successfully updated.
    */
    public static int updateBatch(PrizeRecord[] objList){
        Object[][] argv = new Object[objList.length][];

        for (int idx = 0; idx < objList.length; idx++){
            PrizeRecord o = objList[idx];
            Object[] args = new Object[]{
                o.USER_ID, o.USER_NAME, o.DRAW_TIME, o.PRIZE_ID, 
                o.PRIZE_NAME, o.DRAW_COUNT, o.ID};

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
    public static int removeBatch(PrizeRecord[] objList){
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
    public static PrizeRecord create(){

        PrizeRecord __newObj = new PrizeRecord();

        return (PrizeRecord) _tableCache.create(__newObj, _createSQL, 
            __newObj.USER_ID, __newObj.USER_NAME, __newObj.DRAW_TIME, __newObj.PRIZE_ID, 
            __newObj.PRIZE_NAME, __newObj.DRAW_COUNT);
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
    public static PrizeRecord create(
            TableBase.AttributeValue<?> attrVal,
            TableBase.AttributeValue<?>... attrVals){

        PrizeRecord __newObj = new PrizeRecord();

        if (null != attrVal.getVal()){
            __newObj._setValue(attrVal.getFidx(), attrVal.getVal());
        }

        for (TableBase.AttributeValue<?> attrV: attrVals){
            if (null != attrV.getVal()){
                __newObj._setValue(attrV.getFidx(), attrV.getVal());
            }
        }

        return (PrizeRecord) _tableCache.create(__newObj, _createSQL, 
            __newObj.USER_ID, __newObj.USER_NAME, __newObj.DRAW_TIME, __newObj.PRIZE_ID, 
            __newObj.PRIZE_NAME, __newObj.DRAW_COUNT);
    }

    /**
    * find a JPO by indexes.
    * @return the found JPO or null
    */
    public static PrizeRecord getOne(int ID){
        return (PrizeRecord) _tableCache.getOne(_genKey(ID), _querySQL, ID);
    }

    /**
    * find a JPO by arbitrary query criteria.
    * @return the found JPO or null
    */
    public static PrizeRecord getOneByCriteria(QueryCriteria ...criteria){
        return (PrizeRecord)_tableCache.getOneByCriteria(criteria);
    }

    /**
    * find all JPOs those match the query criteria list.
    * @return a list of JPO found.
    */
    public static ArrayList<PrizeRecord> getManyByCriteria(QueryCriteria ...criteria){
        ArrayList<Object> __tmp = _tableCache.getManyByCriteria(criteria);

        ArrayList<PrizeRecord> __res = new ArrayList<PrizeRecord>();
        for (Object __obj: __tmp){
            __res.add((PrizeRecord)__obj);
        }

        return __res;
    }

    /**
    * find a JPO by arbitrary SQL statement.
    * @return return the found JPO or null.
    */
    public static PrizeRecord getOneBySQL(String sql, Object ...args){
        return (PrizeRecord)_tableCache.getOneBySQL(sql, args);
    }

    /**
    * find all JPOs by arbitrary SQL statement.
    * @return a list of JPO found.
    */
    public static ArrayList<PrizeRecord> getManyBySQL(String sql, Object ...args){
        ArrayList<Object> __tmp = _tableCache.getManyBySQL(sql, args);

        ArrayList<PrizeRecord> __res = new ArrayList<PrizeRecord>();
        for (Object __obj: __tmp){
            __res.add((PrizeRecord)__obj);
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
