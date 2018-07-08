/*
* @Brief: Java Persistence Class (JPC) for Database Table.
*
* @Class: TTaskConfig
* @Namespace: com.mozat.morange.dbcache.tables
* @Table: TASK_CONFIG
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

public final class TTaskConfig extends TableBase implements java.io.Serializable{
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
    private static final String _updateSQL = "UPDATE TASK_CONFIG SET `TASK_ID` = ?, `TYPE` = ?, `FINISH_COUNT` = ?, `ROOM_CARD` = ?, `DESC_ZH` = ?, `DESC_EN` = ?, `SHOW_ORDER` = ? WHERE `ID` = ?;";
    private static final String _querySQL = "SELECT * FROM TASK_CONFIG WHERE `ID` = ?;";
    private static final String _createSQL = "INSERT INTO TASK_CONFIG (`TASK_ID`, `TYPE`, `FINISH_COUNT`, `ROOM_CARD`, `DESC_ZH`, `DESC_EN`, `SHOW_ORDER`) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String _deleteSQL = "DELETE FROM TASK_CONFIG WHERE `ID` = ?;";

    // Field Map
    private static HashMap<String, Integer> AttrFieldMap;
    private static HashMap<String, Integer> getFieldMap(){
        if (AttrFieldMap == null){
            AttrFieldMap = new HashMap<String, Integer>();
            AttrFieldMap.put("ID", 1);
            AttrFieldMap.put("TASK_ID", 2);
            AttrFieldMap.put("TYPE", 3);
            AttrFieldMap.put("FINISH_COUNT", 4);
            AttrFieldMap.put("ROOM_CARD", 5);
            AttrFieldMap.put("DESC_ZH", 6);
            AttrFieldMap.put("DESC_EN", 7);
            AttrFieldMap.put("SHOW_ORDER", 8);
        }

        return AttrFieldMap;
    }

    // Attributes Wrapper for Query
    public static final TableBase.Attribute<Integer> AttrID = new TableBase.Attribute<Integer>("ID", TTaskConfig.class, 1);
    public static final TableBase.Attribute<Integer> AttrTASK_ID = new TableBase.Attribute<Integer>("TASK_ID", TTaskConfig.class, 2);
    public static final TableBase.Attribute<Integer> AttrTYPE = new TableBase.Attribute<Integer>("TYPE", TTaskConfig.class, 3);
    public static final TableBase.Attribute<Integer> AttrFINISH_COUNT = new TableBase.Attribute<Integer>("FINISH_COUNT", TTaskConfig.class, 4);
    public static final TableBase.Attribute<Integer> AttrROOM_CARD = new TableBase.Attribute<Integer>("ROOM_CARD", TTaskConfig.class, 5);
    public static final TableBase.AttributeS AttrDESC_ZH = new TableBase.AttributeS("DESC_ZH", TTaskConfig.class, 6);
    public static final TableBase.AttributeS AttrDESC_EN = new TableBase.AttributeS("DESC_EN", TTaskConfig.class, 7);
    public static final TableBase.Attribute<Integer> AttrSHOW_ORDER = new TableBase.Attribute<Integer>("SHOW_ORDER", TTaskConfig.class, 8);

    public static TableBase.AttributeValue<?>[] valueList(TableBase.AttributeValue<?>...valList){
        return valList;
    }

    // Member Fields.
    public int ID = 0;
    public int TASK_ID = 0;
    public int TYPE = 0;
    public int FINISH_COUNT = 0;
    public int ROOM_CARD = 0;
    public String DESC_ZH = "";
    public String DESC_EN = "";
    public int SHOW_ORDER = 0;

    // DON'T Invoke This Method Manually.
    @Override
    public HashMap<String, Integer> _getFieldMap(){
        return TTaskConfig.getFieldMap();
    }

    // DON'T Invoke This Method Manually.
    @Override
    public void _setValue(int __fidx, Object __val){
        switch (__fidx){
        case 1: {this.ID = (Integer)__val; break;}
        case 2: {this.TASK_ID = (Integer)__val; break;}
        case 3: {this.TYPE = (Integer)__val; break;}
        case 4: {this.FINISH_COUNT = (Integer)__val; break;}
        case 5: {this.ROOM_CARD = (Integer)__val; break;}
        case 6: {this.DESC_ZH = (String)__val; break;}
        case 7: {this.DESC_EN = (String)__val; break;}
        case 8: {this.SHOW_ORDER = (Integer)__val; break;}
        }
    }

    // DON'T Invoke This Method Manually.
    @Override
    @SuppressWarnings("rawtypes")
    public Comparable _getValue(int __fidx){
        switch (__fidx){
        case 1: {return this.ID;}
        case 2: {return this.TASK_ID;}
        case 3: {return this.TYPE;}
        case 4: {return this.FINISH_COUNT;}
        case 5: {return this.ROOM_CARD;}
        case 6: {return this.DESC_ZH;}
        case 7: {return this.DESC_EN;}
        case 8: {return this.SHOW_ORDER;}
        }
        return null;
    }

    /**
    * Default constructor for JavaBean convention
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public TTaskConfig(){}

    /**
    * Constructor for creating a new instance.
    * Arguments are index keys
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public TTaskConfig(int ID){
        this.ID = ID;
    }

    @Override
    public String _getCacheKey(){
        return _genKey(ID);
    }

    public static ArrayList<TTaskConfig> getAllObjects(){
        ArrayList<Object> __tmp = _tableCache.loadAll();

        ArrayList<TTaskConfig> __res = new ArrayList<TTaskConfig>();
        for (Object __obj: __tmp){
            __res.add((TTaskConfig)__obj);
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
            TASK_ID, TYPE, FINISH_COUNT, ROOM_CARD, 
            DESC_ZH, DESC_EN, SHOW_ORDER, ID);

        return (__res > 0);
    }

    /**
    * update JPO in batch.
    * @return num of records successfully updated.
    */
    public static int updateBatch(TTaskConfig[] objList){
        Object[][] argv = new Object[objList.length][];

        for (int idx = 0; idx < objList.length; idx++){
            TTaskConfig o = objList[idx];
            Object[] args = new Object[]{
                o.TASK_ID, o.TYPE, o.FINISH_COUNT, o.ROOM_CARD, 
                o.DESC_ZH, o.DESC_EN, o.SHOW_ORDER, o.ID};

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
    public static int removeBatch(TTaskConfig[] objList){
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
    public static TTaskConfig create(){

        TTaskConfig __newObj = new TTaskConfig();

        return (TTaskConfig) _tableCache.create(__newObj, _createSQL, 
            __newObj.TASK_ID, __newObj.TYPE, __newObj.FINISH_COUNT, __newObj.ROOM_CARD, 
            __newObj.DESC_ZH, __newObj.DESC_EN, __newObj.SHOW_ORDER);
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
    public static TTaskConfig create(
            TableBase.AttributeValue<?> attrVal,
            TableBase.AttributeValue<?>... attrVals){

        TTaskConfig __newObj = new TTaskConfig();

        if (null != attrVal.getVal()){
            __newObj._setValue(attrVal.getFidx(), attrVal.getVal());
        }

        for (TableBase.AttributeValue<?> attrV: attrVals){
            if (null != attrV.getVal()){
                __newObj._setValue(attrV.getFidx(), attrV.getVal());
            }
        }

        return (TTaskConfig) _tableCache.create(__newObj, _createSQL, 
            __newObj.TASK_ID, __newObj.TYPE, __newObj.FINISH_COUNT, __newObj.ROOM_CARD, 
            __newObj.DESC_ZH, __newObj.DESC_EN, __newObj.SHOW_ORDER);
    }

    /**
    * find a JPO by indexes.
    * @return the found JPO or null
    */
    public static TTaskConfig getOne(int ID){
        return (TTaskConfig) _tableCache.getOne(_genKey(ID), _querySQL, ID);
    }

    /**
    * find a JPO by arbitrary query criteria.
    * @return the found JPO or null
    */
    public static TTaskConfig getOneByCriteria(QueryCriteria ...criteria){
        return (TTaskConfig)_tableCache.getOneByCriteria(criteria);
    }

    /**
    * find all JPOs those match the query criteria list.
    * @return a list of JPO found.
    */
    public static ArrayList<TTaskConfig> getManyByCriteria(QueryCriteria ...criteria){
        ArrayList<Object> __tmp = _tableCache.getManyByCriteria(criteria);

        ArrayList<TTaskConfig> __res = new ArrayList<TTaskConfig>();
        for (Object __obj: __tmp){
            __res.add((TTaskConfig)__obj);
        }

        return __res;
    }

    /**
    * find a JPO by arbitrary SQL statement.
    * @return return the found JPO or null.
    */
    public static TTaskConfig getOneBySQL(String sql, Object ...args){
        return (TTaskConfig)_tableCache.getOneBySQL(sql, args);
    }

    /**
    * find all JPOs by arbitrary SQL statement.
    * @return a list of JPO found.
    */
    public static ArrayList<TTaskConfig> getManyBySQL(String sql, Object ...args){
        ArrayList<Object> __tmp = _tableCache.getManyBySQL(sql, args);

        ArrayList<TTaskConfig> __res = new ArrayList<TTaskConfig>();
        for (Object __obj: __tmp){
            __res.add((TTaskConfig)__obj);
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
