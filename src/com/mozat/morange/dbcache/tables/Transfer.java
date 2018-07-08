/*
* @Brief: Java Persistence Class (JPC) for Database Table.
*
* @Class: Transfer
* @Namespace: com.mozat.morange.dbcache.tables
* @Table: TRANSFER
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

public final class Transfer extends TableBase implements java.io.Serializable{
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
    private static final String _updateSQL = "UPDATE TRANSFER SET `FROM_ID` = ?, `FROM_NAME` = ?, `AMOUNT` = ?, `FEE` = ?, `TO_ID` = ?, `TO_NAME` = ?, `RECORD_TIME` = ? WHERE `ID` = ?;";
    private static final String _querySQL = "SELECT * FROM TRANSFER WHERE `ID` = ?;";
    private static final String _createSQL = "INSERT INTO TRANSFER (`FROM_ID`, `FROM_NAME`, `AMOUNT`, `FEE`, `TO_ID`, `TO_NAME`, `RECORD_TIME`) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String _deleteSQL = "DELETE FROM TRANSFER WHERE `ID` = ?;";

    // Field Map
    private static HashMap<String, Integer> AttrFieldMap;
    private static HashMap<String, Integer> getFieldMap(){
        if (AttrFieldMap == null){
            AttrFieldMap = new HashMap<String, Integer>();
            AttrFieldMap.put("ID", 1);
            AttrFieldMap.put("FROM_ID", 2);
            AttrFieldMap.put("FROM_NAME", 3);
            AttrFieldMap.put("AMOUNT", 4);
            AttrFieldMap.put("FEE", 5);
            AttrFieldMap.put("TO_ID", 6);
            AttrFieldMap.put("TO_NAME", 7);
            AttrFieldMap.put("RECORD_TIME", 8);
        }

        return AttrFieldMap;
    }

    // Attributes Wrapper for Query
    public static final TableBase.Attribute<Integer> AttrID = new TableBase.Attribute<Integer>("ID", Transfer.class, 1);
    public static final TableBase.AttributeS AttrFROM_ID = new TableBase.AttributeS("FROM_ID", Transfer.class, 2);
    public static final TableBase.AttributeS AttrFROM_NAME = new TableBase.AttributeS("FROM_NAME", Transfer.class, 3);
    public static final TableBase.Attribute<Integer> AttrAMOUNT = new TableBase.Attribute<Integer>("AMOUNT", Transfer.class, 4);
    public static final TableBase.Attribute<Integer> AttrFEE = new TableBase.Attribute<Integer>("FEE", Transfer.class, 5);
    public static final TableBase.AttributeS AttrTO_ID = new TableBase.AttributeS("TO_ID", Transfer.class, 6);
    public static final TableBase.AttributeS AttrTO_NAME = new TableBase.AttributeS("TO_NAME", Transfer.class, 7);
    public static final TableBase.Attribute<java.util.Date> AttrRECORD_TIME = new TableBase.Attribute<java.util.Date>("RECORD_TIME", Transfer.class, 8);

    public static TableBase.AttributeValue<?>[] valueList(TableBase.AttributeValue<?>...valList){
        return valList;
    }

    // Member Fields.
    public int ID = 0;
    public String FROM_ID = "";
    public String FROM_NAME = "";
    public int AMOUNT = 0;
    public int FEE = 0;
    public String TO_ID = "";
    public String TO_NAME = "";
    public java.util.Date RECORD_TIME = new java.util.Date(0);

    // DON'T Invoke This Method Manually.
    @Override
    public HashMap<String, Integer> _getFieldMap(){
        return Transfer.getFieldMap();
    }

    // DON'T Invoke This Method Manually.
    @Override
    public void _setValue(int __fidx, Object __val){
        switch (__fidx){
        case 1: {this.ID = (Integer)__val; break;}
        case 2: {this.FROM_ID = (String)__val; break;}
        case 3: {this.FROM_NAME = (String)__val; break;}
        case 4: {this.AMOUNT = (Integer)__val; break;}
        case 5: {this.FEE = (Integer)__val; break;}
        case 6: {this.TO_ID = (String)__val; break;}
        case 7: {this.TO_NAME = (String)__val; break;}
        case 8: {this.RECORD_TIME = (java.util.Date)__val; break;}
        }
    }

    // DON'T Invoke This Method Manually.
    @Override
    @SuppressWarnings("rawtypes")
    public Comparable _getValue(int __fidx){
        switch (__fidx){
        case 1: {return this.ID;}
        case 2: {return this.FROM_ID;}
        case 3: {return this.FROM_NAME;}
        case 4: {return this.AMOUNT;}
        case 5: {return this.FEE;}
        case 6: {return this.TO_ID;}
        case 7: {return this.TO_NAME;}
        case 8: {return this.RECORD_TIME;}
        }
        return null;
    }

    /**
    * Default constructor for JavaBean convention
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public Transfer(){}

    /**
    * Constructor for creating a new instance.
    * Arguments are index keys
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public Transfer(int ID){
        this.ID = ID;
    }

    @Override
    public String _getCacheKey(){
        return _genKey(ID);
    }

    public static ArrayList<Transfer> getAllObjects(){
        ArrayList<Object> __tmp = _tableCache.loadAll();

        ArrayList<Transfer> __res = new ArrayList<Transfer>();
        for (Object __obj: __tmp){
            __res.add((Transfer)__obj);
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
            FROM_ID, FROM_NAME, AMOUNT, FEE, 
            TO_ID, TO_NAME, RECORD_TIME, ID);

        return (__res > 0);
    }

    /**
    * update JPO in batch.
    * @return num of records successfully updated.
    */
    public static int updateBatch(Transfer[] objList){
        Object[][] argv = new Object[objList.length][];

        for (int idx = 0; idx < objList.length; idx++){
            Transfer o = objList[idx];
            Object[] args = new Object[]{
                o.FROM_ID, o.FROM_NAME, o.AMOUNT, o.FEE, 
                o.TO_ID, o.TO_NAME, o.RECORD_TIME, o.ID};

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
    public static int removeBatch(Transfer[] objList){
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
    public static Transfer create(){

        Transfer __newObj = new Transfer();

        return (Transfer) _tableCache.create(__newObj, _createSQL, 
            __newObj.FROM_ID, __newObj.FROM_NAME, __newObj.AMOUNT, __newObj.FEE, 
            __newObj.TO_ID, __newObj.TO_NAME, __newObj.RECORD_TIME);
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
    public static Transfer create(
            TableBase.AttributeValue<?> attrVal,
            TableBase.AttributeValue<?>... attrVals){

        Transfer __newObj = new Transfer();

        if (null != attrVal.getVal()){
            __newObj._setValue(attrVal.getFidx(), attrVal.getVal());
        }

        for (TableBase.AttributeValue<?> attrV: attrVals){
            if (null != attrV.getVal()){
                __newObj._setValue(attrV.getFidx(), attrV.getVal());
            }
        }

        return (Transfer) _tableCache.create(__newObj, _createSQL, 
            __newObj.FROM_ID, __newObj.FROM_NAME, __newObj.AMOUNT, __newObj.FEE, 
            __newObj.TO_ID, __newObj.TO_NAME, __newObj.RECORD_TIME);
    }

    /**
    * find a JPO by indexes.
    * @return the found JPO or null
    */
    public static Transfer getOne(int ID){
        return (Transfer) _tableCache.getOne(_genKey(ID), _querySQL, ID);
    }

    /**
    * find a JPO by arbitrary query criteria.
    * @return the found JPO or null
    */
    public static Transfer getOneByCriteria(QueryCriteria ...criteria){
        return (Transfer)_tableCache.getOneByCriteria(criteria);
    }

    /**
    * find all JPOs those match the query criteria list.
    * @return a list of JPO found.
    */
    public static ArrayList<Transfer> getManyByCriteria(QueryCriteria ...criteria){
        ArrayList<Object> __tmp = _tableCache.getManyByCriteria(criteria);

        ArrayList<Transfer> __res = new ArrayList<Transfer>();
        for (Object __obj: __tmp){
            __res.add((Transfer)__obj);
        }

        return __res;
    }

    /**
    * find a JPO by arbitrary SQL statement.
    * @return return the found JPO or null.
    */
    public static Transfer getOneBySQL(String sql, Object ...args){
        return (Transfer)_tableCache.getOneBySQL(sql, args);
    }

    /**
    * find all JPOs by arbitrary SQL statement.
    * @return a list of JPO found.
    */
    public static ArrayList<Transfer> getManyBySQL(String sql, Object ...args){
        ArrayList<Object> __tmp = _tableCache.getManyBySQL(sql, args);

        ArrayList<Transfer> __res = new ArrayList<Transfer>();
        for (Object __obj: __tmp){
            __res.add((Transfer)__obj);
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
