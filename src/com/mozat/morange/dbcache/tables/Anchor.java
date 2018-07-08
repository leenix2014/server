/*
* @Brief: Java Persistence Class (JPC) for Database Table.
*
* @Class: Anchor
* @Namespace: com.mozat.morange.dbcache.tables
* @Table: ANCHOR
* @Primary Key: ID
* @Identity: ID
* @Unique Index: [u'ANCHOR_ID']
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

public final class Anchor extends TableBase implements java.io.Serializable{
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
    private static final String _updateSQL = "UPDATE ANCHOR SET `ANCHOR_ID` = ?, `ANCHOR_NAME` = ?, `ANCHOR_TYPE` = ?, `PWD` = ?, `CHANNEL` = ?, `TITLE` = ?, `COST` = ?, `ENCRYPTED` = ?, `ROOM_PWD` = ?, `ADMIN_IDS` = ?, `HISTORY_CUBE` = ?, `CREATE_TIME` = ? WHERE `ID` = ?;";
    private static final String _querySQL = "SELECT * FROM ANCHOR WHERE `ANCHOR_ID` = ?;";
    private static final String _createSQL = "INSERT INTO ANCHOR (`ANCHOR_ID`, `ANCHOR_NAME`, `ANCHOR_TYPE`, `PWD`, `CHANNEL`, `TITLE`, `COST`, `ENCRYPTED`, `ROOM_PWD`, `ADMIN_IDS`, `HISTORY_CUBE`, `CREATE_TIME`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String _deleteSQL = "DELETE FROM ANCHOR WHERE `ID` = ?;";

    // Field Map
    private static HashMap<String, Integer> AttrFieldMap;
    private static HashMap<String, Integer> getFieldMap(){
        if (AttrFieldMap == null){
            AttrFieldMap = new HashMap<String, Integer>();
            AttrFieldMap.put("ID", 1);
            AttrFieldMap.put("ANCHOR_ID", 2);
            AttrFieldMap.put("ANCHOR_NAME", 3);
            AttrFieldMap.put("ANCHOR_TYPE", 4);
            AttrFieldMap.put("PWD", 5);
            AttrFieldMap.put("CHANNEL", 6);
            AttrFieldMap.put("TITLE", 7);
            AttrFieldMap.put("COST", 8);
            AttrFieldMap.put("ENCRYPTED", 9);
            AttrFieldMap.put("ROOM_PWD", 10);
            AttrFieldMap.put("ADMIN_IDS", 11);
            AttrFieldMap.put("HISTORY_CUBE", 12);
            AttrFieldMap.put("CREATE_TIME", 13);
        }

        return AttrFieldMap;
    }

    // Attributes Wrapper for Query
    public static final TableBase.Attribute<Integer> AttrID = new TableBase.Attribute<Integer>("ID", Anchor.class, 1);
    public static final TableBase.Attribute<Integer> AttrANCHOR_ID = new TableBase.Attribute<Integer>("ANCHOR_ID", Anchor.class, 2);
    public static final TableBase.AttributeS AttrANCHOR_NAME = new TableBase.AttributeS("ANCHOR_NAME", Anchor.class, 3);
    public static final TableBase.AttributeS AttrANCHOR_TYPE = new TableBase.AttributeS("ANCHOR_TYPE", Anchor.class, 4);
    public static final TableBase.AttributeS AttrPWD = new TableBase.AttributeS("PWD", Anchor.class, 5);
    public static final TableBase.AttributeS AttrCHANNEL = new TableBase.AttributeS("CHANNEL", Anchor.class, 6);
    public static final TableBase.AttributeS AttrTITLE = new TableBase.AttributeS("TITLE", Anchor.class, 7);
    public static final TableBase.Attribute<Integer> AttrCOST = new TableBase.Attribute<Integer>("COST", Anchor.class, 8);
    public static final TableBase.Attribute<Boolean> AttrENCRYPTED = new TableBase.Attribute<Boolean>("ENCRYPTED", Anchor.class, 9);
    public static final TableBase.AttributeS AttrROOM_PWD = new TableBase.AttributeS("ROOM_PWD", Anchor.class, 10);
    public static final TableBase.AttributeS AttrADMIN_IDS = new TableBase.AttributeS("ADMIN_IDS", Anchor.class, 11);
    public static final TableBase.Attribute<Integer> AttrHISTORY_CUBE = new TableBase.Attribute<Integer>("HISTORY_CUBE", Anchor.class, 12);
    public static final TableBase.Attribute<java.util.Date> AttrCREATE_TIME = new TableBase.Attribute<java.util.Date>("CREATE_TIME", Anchor.class, 13);

    public static TableBase.AttributeValue<?>[] valueList(TableBase.AttributeValue<?>...valList){
        return valList;
    }

    // Member Fields.
    public int ID = 0;
    public int ANCHOR_ID = 0;
    public String ANCHOR_NAME = "";
    public String ANCHOR_TYPE = "";
    public String PWD = "";
    public String CHANNEL = "";
    public String TITLE = "";
    public int COST = 0;
    public boolean ENCRYPTED = false;
    public String ROOM_PWD = "";
    public String ADMIN_IDS = "";
    public int HISTORY_CUBE = 0;
    public java.util.Date CREATE_TIME = new java.util.Date(0);

    // DON'T Invoke This Method Manually.
    @Override
    public HashMap<String, Integer> _getFieldMap(){
        return Anchor.getFieldMap();
    }

    // DON'T Invoke This Method Manually.
    @Override
    public void _setValue(int __fidx, Object __val){
        switch (__fidx){
        case 1: {this.ID = (Integer)__val; break;}
        case 2: {this.ANCHOR_ID = (Integer)__val; break;}
        case 3: {this.ANCHOR_NAME = (String)__val; break;}
        case 4: {this.ANCHOR_TYPE = (String)__val; break;}
        case 5: {this.PWD = (String)__val; break;}
        case 6: {this.CHANNEL = (String)__val; break;}
        case 7: {this.TITLE = (String)__val; break;}
        case 8: {this.COST = (Integer)__val; break;}
        case 9: {this.ENCRYPTED = (Boolean)__val; break;}
        case 10: {this.ROOM_PWD = (String)__val; break;}
        case 11: {this.ADMIN_IDS = (String)__val; break;}
        case 12: {this.HISTORY_CUBE = (Integer)__val; break;}
        case 13: {this.CREATE_TIME = (java.util.Date)__val; break;}
        }
    }

    // DON'T Invoke This Method Manually.
    @Override
    @SuppressWarnings("rawtypes")
    public Comparable _getValue(int __fidx){
        switch (__fidx){
        case 1: {return this.ID;}
        case 2: {return this.ANCHOR_ID;}
        case 3: {return this.ANCHOR_NAME;}
        case 4: {return this.ANCHOR_TYPE;}
        case 5: {return this.PWD;}
        case 6: {return this.CHANNEL;}
        case 7: {return this.TITLE;}
        case 8: {return this.COST;}
        case 9: {return this.ENCRYPTED;}
        case 10: {return this.ROOM_PWD;}
        case 11: {return this.ADMIN_IDS;}
        case 12: {return this.HISTORY_CUBE;}
        case 13: {return this.CREATE_TIME;}
        }
        return null;
    }

    /**
    * Default constructor for JavaBean convention
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public Anchor(){}

    /**
    * Constructor for creating a new instance.
    * Arguments are index keys
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public Anchor(int ANCHOR_ID){
        this.ANCHOR_ID = ANCHOR_ID;
    }

    @Override
    public String _getCacheKey(){
        return _genKey(ANCHOR_ID);
    }

    public static ArrayList<Anchor> getAllObjects(){
        ArrayList<Object> __tmp = _tableCache.loadAll();

        ArrayList<Anchor> __res = new ArrayList<Anchor>();
        for (Object __obj: __tmp){
            __res.add((Anchor)__obj);
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
            ANCHOR_ID, ANCHOR_NAME, ANCHOR_TYPE, PWD, 
            CHANNEL, TITLE, COST, ENCRYPTED, 
            ROOM_PWD, ADMIN_IDS, HISTORY_CUBE, CREATE_TIME, 
            ID);

        return (__res > 0);
    }

    /**
    * update JPO in batch.
    * @return num of records successfully updated.
    */
    public static int updateBatch(Anchor[] objList){
        Object[][] argv = new Object[objList.length][];

        for (int idx = 0; idx < objList.length; idx++){
            Anchor o = objList[idx];
            Object[] args = new Object[]{
                o.ANCHOR_ID, o.ANCHOR_NAME, o.ANCHOR_TYPE, o.PWD, 
                o.CHANNEL, o.TITLE, o.COST, o.ENCRYPTED, 
                o.ROOM_PWD, o.ADMIN_IDS, o.HISTORY_CUBE, o.CREATE_TIME, 
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
    public static int removeBatch(Anchor[] objList){
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
    public static Anchor create(int ANCHOR_ID){

        Anchor __newObj = new Anchor(ANCHOR_ID);

        return (Anchor) _tableCache.create(__newObj, _createSQL, 
            __newObj.ANCHOR_ID, __newObj.ANCHOR_NAME, __newObj.ANCHOR_TYPE, __newObj.PWD, 
            __newObj.CHANNEL, __newObj.TITLE, __newObj.COST, __newObj.ENCRYPTED, 
            __newObj.ROOM_PWD, __newObj.ADMIN_IDS, __newObj.HISTORY_CUBE, __newObj.CREATE_TIME);
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
    public static Anchor create(int ANCHOR_ID, 
            TableBase.AttributeValue<?> attrVal,
            TableBase.AttributeValue<?>... attrVals){

        Anchor __newObj = new Anchor(ANCHOR_ID);

        if (null != attrVal.getVal()){
            __newObj._setValue(attrVal.getFidx(), attrVal.getVal());
        }

        for (TableBase.AttributeValue<?> attrV: attrVals){
            if (null != attrV.getVal()){
                __newObj._setValue(attrV.getFidx(), attrV.getVal());
            }
        }

        return (Anchor) _tableCache.create(__newObj, _createSQL, 
            __newObj.ANCHOR_ID, __newObj.ANCHOR_NAME, __newObj.ANCHOR_TYPE, __newObj.PWD, 
            __newObj.CHANNEL, __newObj.TITLE, __newObj.COST, __newObj.ENCRYPTED, 
            __newObj.ROOM_PWD, __newObj.ADMIN_IDS, __newObj.HISTORY_CUBE, __newObj.CREATE_TIME);
    }

    /**
    * find a JPO by indexes.
    * @return the found JPO or null
    */
    public static Anchor getOne(int ANCHOR_ID){
        return (Anchor) _tableCache.getOne(_genKey(ANCHOR_ID), _querySQL, ANCHOR_ID);
    }

    /**
    * find a JPO by arbitrary query criteria.
    * @return the found JPO or null
    */
    public static Anchor getOneByCriteria(QueryCriteria ...criteria){
        return (Anchor)_tableCache.getOneByCriteria(criteria);
    }

    /**
    * find all JPOs those match the query criteria list.
    * @return a list of JPO found.
    */
    public static ArrayList<Anchor> getManyByCriteria(QueryCriteria ...criteria){
        ArrayList<Object> __tmp = _tableCache.getManyByCriteria(criteria);

        ArrayList<Anchor> __res = new ArrayList<Anchor>();
        for (Object __obj: __tmp){
            __res.add((Anchor)__obj);
        }

        return __res;
    }

    /**
    * find a JPO by arbitrary SQL statement.
    * @return return the found JPO or null.
    */
    public static Anchor getOneBySQL(String sql, Object ...args){
        return (Anchor)_tableCache.getOneBySQL(sql, args);
    }

    /**
    * find all JPOs by arbitrary SQL statement.
    * @return a list of JPO found.
    */
    public static ArrayList<Anchor> getManyBySQL(String sql, Object ...args){
        ArrayList<Object> __tmp = _tableCache.getManyBySQL(sql, args);

        ArrayList<Anchor> __res = new ArrayList<Anchor>();
        for (Object __obj: __tmp){
            __res.add((Anchor)__obj);
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
