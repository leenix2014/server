/*
* @Brief: Java Persistence Class (JPC) for Database Table.
*
* @Class: BaccaratRoom
* @Namespace: com.mozat.morange.dbcache.tables
* @Table: BACCARAT_ROOM
* @Primary Key: ID
* @Identity: ID
* @Unique Index: [u'ROOM_ID']
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

public final class BaccaratRoom extends TableBase implements java.io.Serializable{
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
    private static final String _updateSQL = "UPDATE BACCARAT_ROOM SET `ROOM_ID` = ?, `CHANNEL` = ?, `MIN_COIN` = ?, `TAKE_SEAT_COIN` = ?, `MIN_BET` = ?, `MAX_BET` = ?, `MAX_RED` = ?, `TIE_PAIR_MIN_BET` = ?, `TIE_PAIR_MAX_BET` = ?, `DRAW_PERCENT` = ?, `BET_TIME` = ?, `OVER_TIME` = ?, `ADMIN_IDS` = ?, `CREATE_TIME` = ?, `DELETED` = ?, `DELETE_TIME` = ? WHERE `ID` = ?;";
    private static final String _querySQL = "SELECT * FROM BACCARAT_ROOM WHERE `ROOM_ID` = ?;";
    private static final String _createSQL = "INSERT INTO BACCARAT_ROOM (`ROOM_ID`, `CHANNEL`, `MIN_COIN`, `TAKE_SEAT_COIN`, `MIN_BET`, `MAX_BET`, `MAX_RED`, `TIE_PAIR_MIN_BET`, `TIE_PAIR_MAX_BET`, `DRAW_PERCENT`, `BET_TIME`, `OVER_TIME`, `ADMIN_IDS`, `CREATE_TIME`, `DELETED`, `DELETE_TIME`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String _deleteSQL = "DELETE FROM BACCARAT_ROOM WHERE `ID` = ?;";

    // Field Map
    private static HashMap<String, Integer> AttrFieldMap;
    private static HashMap<String, Integer> getFieldMap(){
        if (AttrFieldMap == null){
            AttrFieldMap = new HashMap<String, Integer>();
            AttrFieldMap.put("ID", 1);
            AttrFieldMap.put("ROOM_ID", 2);
            AttrFieldMap.put("CHANNEL", 3);
            AttrFieldMap.put("MIN_COIN", 4);
            AttrFieldMap.put("TAKE_SEAT_COIN", 5);
            AttrFieldMap.put("MIN_BET", 6);
            AttrFieldMap.put("MAX_BET", 7);
            AttrFieldMap.put("MAX_RED", 8);
            AttrFieldMap.put("TIE_PAIR_MIN_BET", 9);
            AttrFieldMap.put("TIE_PAIR_MAX_BET", 10);
            AttrFieldMap.put("DRAW_PERCENT", 11);
            AttrFieldMap.put("BET_TIME", 12);
            AttrFieldMap.put("OVER_TIME", 13);
            AttrFieldMap.put("ADMIN_IDS", 14);
            AttrFieldMap.put("CREATE_TIME", 15);
            AttrFieldMap.put("DELETED", 16);
            AttrFieldMap.put("DELETE_TIME", 17);
        }

        return AttrFieldMap;
    }

    // Attributes Wrapper for Query
    public static final TableBase.Attribute<Integer> AttrID = new TableBase.Attribute<Integer>("ID", BaccaratRoom.class, 1);
    public static final TableBase.AttributeS AttrROOM_ID = new TableBase.AttributeS("ROOM_ID", BaccaratRoom.class, 2);
    public static final TableBase.AttributeS AttrCHANNEL = new TableBase.AttributeS("CHANNEL", BaccaratRoom.class, 3);
    public static final TableBase.Attribute<Integer> AttrMIN_COIN = new TableBase.Attribute<Integer>("MIN_COIN", BaccaratRoom.class, 4);
    public static final TableBase.Attribute<Integer> AttrTAKE_SEAT_COIN = new TableBase.Attribute<Integer>("TAKE_SEAT_COIN", BaccaratRoom.class, 5);
    public static final TableBase.Attribute<Integer> AttrMIN_BET = new TableBase.Attribute<Integer>("MIN_BET", BaccaratRoom.class, 6);
    public static final TableBase.Attribute<Integer> AttrMAX_BET = new TableBase.Attribute<Integer>("MAX_BET", BaccaratRoom.class, 7);
    public static final TableBase.Attribute<Integer> AttrMAX_RED = new TableBase.Attribute<Integer>("MAX_RED", BaccaratRoom.class, 8);
    public static final TableBase.Attribute<Integer> AttrTIE_PAIR_MIN_BET = new TableBase.Attribute<Integer>("TIE_PAIR_MIN_BET", BaccaratRoom.class, 9);
    public static final TableBase.Attribute<Integer> AttrTIE_PAIR_MAX_BET = new TableBase.Attribute<Integer>("TIE_PAIR_MAX_BET", BaccaratRoom.class, 10);
    public static final TableBase.Attribute<Integer> AttrDRAW_PERCENT = new TableBase.Attribute<Integer>("DRAW_PERCENT", BaccaratRoom.class, 11);
    public static final TableBase.Attribute<Integer> AttrBET_TIME = new TableBase.Attribute<Integer>("BET_TIME", BaccaratRoom.class, 12);
    public static final TableBase.Attribute<Integer> AttrOVER_TIME = new TableBase.Attribute<Integer>("OVER_TIME", BaccaratRoom.class, 13);
    public static final TableBase.AttributeS AttrADMIN_IDS = new TableBase.AttributeS("ADMIN_IDS", BaccaratRoom.class, 14);
    public static final TableBase.Attribute<java.util.Date> AttrCREATE_TIME = new TableBase.Attribute<java.util.Date>("CREATE_TIME", BaccaratRoom.class, 15);
    public static final TableBase.Attribute<Boolean> AttrDELETED = new TableBase.Attribute<Boolean>("DELETED", BaccaratRoom.class, 16);
    public static final TableBase.Attribute<java.util.Date> AttrDELETE_TIME = new TableBase.Attribute<java.util.Date>("DELETE_TIME", BaccaratRoom.class, 17);

    public static TableBase.AttributeValue<?>[] valueList(TableBase.AttributeValue<?>...valList){
        return valList;
    }

    // Member Fields.
    public int ID = 0;
    public String ROOM_ID = "";
    public String CHANNEL = "";
    public int MIN_COIN = 0;
    public int TAKE_SEAT_COIN = 0;
    public int MIN_BET = 0;
    public int MAX_BET = 0;
    public int MAX_RED = 0;
    public int TIE_PAIR_MIN_BET = 0;
    public int TIE_PAIR_MAX_BET = 0;
    public int DRAW_PERCENT = 0;
    public int BET_TIME = 0;
    public int OVER_TIME = 0;
    public String ADMIN_IDS = "";
    public java.util.Date CREATE_TIME = new java.util.Date(0);
    public boolean DELETED = false;
    public java.util.Date DELETE_TIME = new java.util.Date(0);

    // DON'T Invoke This Method Manually.
    @Override
    public HashMap<String, Integer> _getFieldMap(){
        return BaccaratRoom.getFieldMap();
    }

    // DON'T Invoke This Method Manually.
    @Override
    public void _setValue(int __fidx, Object __val){
        switch (__fidx){
        case 1: {this.ID = (Integer)__val; break;}
        case 2: {this.ROOM_ID = (String)__val; break;}
        case 3: {this.CHANNEL = (String)__val; break;}
        case 4: {this.MIN_COIN = (Integer)__val; break;}
        case 5: {this.TAKE_SEAT_COIN = (Integer)__val; break;}
        case 6: {this.MIN_BET = (Integer)__val; break;}
        case 7: {this.MAX_BET = (Integer)__val; break;}
        case 8: {this.MAX_RED = (Integer)__val; break;}
        case 9: {this.TIE_PAIR_MIN_BET = (Integer)__val; break;}
        case 10: {this.TIE_PAIR_MAX_BET = (Integer)__val; break;}
        case 11: {this.DRAW_PERCENT = (Integer)__val; break;}
        case 12: {this.BET_TIME = (Integer)__val; break;}
        case 13: {this.OVER_TIME = (Integer)__val; break;}
        case 14: {this.ADMIN_IDS = (String)__val; break;}
        case 15: {this.CREATE_TIME = (java.util.Date)__val; break;}
        case 16: {this.DELETED = (Boolean)__val; break;}
        case 17: {this.DELETE_TIME = (java.util.Date)__val; break;}
        }
    }

    // DON'T Invoke This Method Manually.
    @Override
    @SuppressWarnings("rawtypes")
    public Comparable _getValue(int __fidx){
        switch (__fidx){
        case 1: {return this.ID;}
        case 2: {return this.ROOM_ID;}
        case 3: {return this.CHANNEL;}
        case 4: {return this.MIN_COIN;}
        case 5: {return this.TAKE_SEAT_COIN;}
        case 6: {return this.MIN_BET;}
        case 7: {return this.MAX_BET;}
        case 8: {return this.MAX_RED;}
        case 9: {return this.TIE_PAIR_MIN_BET;}
        case 10: {return this.TIE_PAIR_MAX_BET;}
        case 11: {return this.DRAW_PERCENT;}
        case 12: {return this.BET_TIME;}
        case 13: {return this.OVER_TIME;}
        case 14: {return this.ADMIN_IDS;}
        case 15: {return this.CREATE_TIME;}
        case 16: {return this.DELETED;}
        case 17: {return this.DELETE_TIME;}
        }
        return null;
    }

    /**
    * Default constructor for JavaBean convention
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public BaccaratRoom(){}

    /**
    * Constructor for creating a new instance.
    * Arguments are index keys
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public BaccaratRoom(String ROOM_ID){
        this.ROOM_ID = ROOM_ID;
    }

    @Override
    public String _getCacheKey(){
        return _genKey(ROOM_ID);
    }

    public static ArrayList<BaccaratRoom> getAllObjects(){
        ArrayList<Object> __tmp = _tableCache.loadAll();

        ArrayList<BaccaratRoom> __res = new ArrayList<BaccaratRoom>();
        for (Object __obj: __tmp){
            __res.add((BaccaratRoom)__obj);
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
            ROOM_ID, CHANNEL, MIN_COIN, TAKE_SEAT_COIN, 
            MIN_BET, MAX_BET, MAX_RED, TIE_PAIR_MIN_BET, 
            TIE_PAIR_MAX_BET, DRAW_PERCENT, BET_TIME, OVER_TIME, 
            ADMIN_IDS, CREATE_TIME, DELETED, DELETE_TIME, 
            ID);

        return (__res > 0);
    }

    /**
    * update JPO in batch.
    * @return num of records successfully updated.
    */
    public static int updateBatch(BaccaratRoom[] objList){
        Object[][] argv = new Object[objList.length][];

        for (int idx = 0; idx < objList.length; idx++){
            BaccaratRoom o = objList[idx];
            Object[] args = new Object[]{
                o.ROOM_ID, o.CHANNEL, o.MIN_COIN, o.TAKE_SEAT_COIN, 
                o.MIN_BET, o.MAX_BET, o.MAX_RED, o.TIE_PAIR_MIN_BET, 
                o.TIE_PAIR_MAX_BET, o.DRAW_PERCENT, o.BET_TIME, o.OVER_TIME, 
                o.ADMIN_IDS, o.CREATE_TIME, o.DELETED, o.DELETE_TIME, 
                o.ID};

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
    public static int removeBatch(BaccaratRoom[] objList){
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
    public static BaccaratRoom create(String ROOM_ID){

        BaccaratRoom __newObj = new BaccaratRoom(ROOM_ID);

        return (BaccaratRoom) _tableCache.create(__newObj, _createSQL, 
            __newObj.ROOM_ID, __newObj.CHANNEL, __newObj.MIN_COIN, __newObj.TAKE_SEAT_COIN, 
            __newObj.MIN_BET, __newObj.MAX_BET, __newObj.MAX_RED, __newObj.TIE_PAIR_MIN_BET, 
            __newObj.TIE_PAIR_MAX_BET, __newObj.DRAW_PERCENT, __newObj.BET_TIME, __newObj.OVER_TIME, 
            __newObj.ADMIN_IDS, __newObj.CREATE_TIME, __newObj.DELETED, __newObj.DELETE_TIME);
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
    public static BaccaratRoom create(String ROOM_ID, 
            TableBase.AttributeValue<?> attrVal,
            TableBase.AttributeValue<?>... attrVals){

        BaccaratRoom __newObj = new BaccaratRoom(ROOM_ID);

        if (null != attrVal.getVal()){
            __newObj._setValue(attrVal.getFidx(), attrVal.getVal());
        }

        for (TableBase.AttributeValue<?> attrV: attrVals){
            if (null != attrV.getVal()){
                __newObj._setValue(attrV.getFidx(), attrV.getVal());
            }
        }

        return (BaccaratRoom) _tableCache.create(__newObj, _createSQL, 
            __newObj.ROOM_ID, __newObj.CHANNEL, __newObj.MIN_COIN, __newObj.TAKE_SEAT_COIN, 
            __newObj.MIN_BET, __newObj.MAX_BET, __newObj.MAX_RED, __newObj.TIE_PAIR_MIN_BET, 
            __newObj.TIE_PAIR_MAX_BET, __newObj.DRAW_PERCENT, __newObj.BET_TIME, __newObj.OVER_TIME, 
            __newObj.ADMIN_IDS, __newObj.CREATE_TIME, __newObj.DELETED, __newObj.DELETE_TIME);
    }

    /**
    * find a JPO by indexes.
    * @return the found JPO or null
    */
    public static BaccaratRoom getOne(String ROOM_ID){
        return (BaccaratRoom) _tableCache.getOne(_genKey(ROOM_ID), _querySQL, ROOM_ID);
    }

    /**
    * find a JPO by arbitrary query criteria.
    * @return the found JPO or null
    */
    public static BaccaratRoom getOneByCriteria(QueryCriteria ...criteria){
        return (BaccaratRoom)_tableCache.getOneByCriteria(criteria);
    }

    /**
    * find all JPOs those match the query criteria list.
    * @return a list of JPO found.
    */
    public static ArrayList<BaccaratRoom> getManyByCriteria(QueryCriteria ...criteria){
        ArrayList<Object> __tmp = _tableCache.getManyByCriteria(criteria);

        ArrayList<BaccaratRoom> __res = new ArrayList<BaccaratRoom>();
        for (Object __obj: __tmp){
            __res.add((BaccaratRoom)__obj);
        }

        return __res;
    }

    /**
    * find a JPO by arbitrary SQL statement.
    * @return return the found JPO or null.
    */
    public static BaccaratRoom getOneBySQL(String sql, Object ...args){
        return (BaccaratRoom)_tableCache.getOneBySQL(sql, args);
    }

    /**
    * find all JPOs by arbitrary SQL statement.
    * @return a list of JPO found.
    */
    public static ArrayList<BaccaratRoom> getManyBySQL(String sql, Object ...args){
        ArrayList<Object> __tmp = _tableCache.getManyBySQL(sql, args);

        ArrayList<BaccaratRoom> __res = new ArrayList<BaccaratRoom>();
        for (Object __obj: __tmp){
            __res.add((BaccaratRoom)__obj);
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
