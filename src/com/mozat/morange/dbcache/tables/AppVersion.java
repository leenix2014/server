/*
* @Brief: Java Persistence Class (JPC) for Database Table.
*
* @Class: AppVersion
* @Namespace: com.mozat.morange.dbcache.tables
* @Table: APP_VERSION
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

public final class AppVersion extends TableBase implements java.io.Serializable{
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
    private static final String _updateSQL = "UPDATE APP_VERSION SET `APP_VER` = ?, `PLATFORM` = ?, `LATEST` = ?, `MUST_UPDATE` = ?, `DOWNLOAD_URL` = ?, `CREATE_TIME` = ?, `UPDATE_TIME` = ? WHERE `ID` = ?;";
    private static final String _querySQL = "SELECT * FROM APP_VERSION WHERE `ID` = ?;";
    private static final String _createSQL = "INSERT INTO APP_VERSION (`APP_VER`, `PLATFORM`, `LATEST`, `MUST_UPDATE`, `DOWNLOAD_URL`, `CREATE_TIME`, `UPDATE_TIME`) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String _deleteSQL = "DELETE FROM APP_VERSION WHERE `ID` = ?;";

    // Field Map
    private static HashMap<String, Integer> AttrFieldMap;
    private static HashMap<String, Integer> getFieldMap(){
        if (AttrFieldMap == null){
            AttrFieldMap = new HashMap<String, Integer>();
            AttrFieldMap.put("ID", 1);
            AttrFieldMap.put("APP_VER", 2);
            AttrFieldMap.put("PLATFORM", 3);
            AttrFieldMap.put("LATEST", 4);
            AttrFieldMap.put("MUST_UPDATE", 5);
            AttrFieldMap.put("DOWNLOAD_URL", 6);
            AttrFieldMap.put("CREATE_TIME", 7);
            AttrFieldMap.put("UPDATE_TIME", 8);
        }

        return AttrFieldMap;
    }

    // Attributes Wrapper for Query
    public static final TableBase.Attribute<Integer> AttrID = new TableBase.Attribute<Integer>("ID", AppVersion.class, 1);
    public static final TableBase.AttributeS AttrAPP_VER = new TableBase.AttributeS("APP_VER", AppVersion.class, 2);
    public static final TableBase.AttributeS AttrPLATFORM = new TableBase.AttributeS("PLATFORM", AppVersion.class, 3);
    public static final TableBase.Attribute<Boolean> AttrLATEST = new TableBase.Attribute<Boolean>("LATEST", AppVersion.class, 4);
    public static final TableBase.Attribute<Boolean> AttrMUST_UPDATE = new TableBase.Attribute<Boolean>("MUST_UPDATE", AppVersion.class, 5);
    public static final TableBase.AttributeS AttrDOWNLOAD_URL = new TableBase.AttributeS("DOWNLOAD_URL", AppVersion.class, 6);
    public static final TableBase.Attribute<java.util.Date> AttrCREATE_TIME = new TableBase.Attribute<java.util.Date>("CREATE_TIME", AppVersion.class, 7);
    public static final TableBase.Attribute<java.util.Date> AttrUPDATE_TIME = new TableBase.Attribute<java.util.Date>("UPDATE_TIME", AppVersion.class, 8);

    public static TableBase.AttributeValue<?>[] valueList(TableBase.AttributeValue<?>...valList){
        return valList;
    }

    // Member Fields.
    public int ID = 0;
    public String APP_VER = "";
    public String PLATFORM = "";
    public boolean LATEST = false;
    public boolean MUST_UPDATE = false;
    public String DOWNLOAD_URL = "";
    public java.util.Date CREATE_TIME = new java.util.Date(0);
    public java.util.Date UPDATE_TIME = new java.util.Date(0);

    // DON'T Invoke This Method Manually.
    @Override
    public HashMap<String, Integer> _getFieldMap(){
        return AppVersion.getFieldMap();
    }

    // DON'T Invoke This Method Manually.
    @Override
    public void _setValue(int __fidx, Object __val){
        switch (__fidx){
        case 1: {this.ID = (Integer)__val; break;}
        case 2: {this.APP_VER = (String)__val; break;}
        case 3: {this.PLATFORM = (String)__val; break;}
        case 4: {this.LATEST = (Boolean)__val; break;}
        case 5: {this.MUST_UPDATE = (Boolean)__val; break;}
        case 6: {this.DOWNLOAD_URL = (String)__val; break;}
        case 7: {this.CREATE_TIME = (java.util.Date)__val; break;}
        case 8: {this.UPDATE_TIME = (java.util.Date)__val; break;}
        }
    }

    // DON'T Invoke This Method Manually.
    @Override
    @SuppressWarnings("rawtypes")
    public Comparable _getValue(int __fidx){
        switch (__fidx){
        case 1: {return this.ID;}
        case 2: {return this.APP_VER;}
        case 3: {return this.PLATFORM;}
        case 4: {return this.LATEST;}
        case 5: {return this.MUST_UPDATE;}
        case 6: {return this.DOWNLOAD_URL;}
        case 7: {return this.CREATE_TIME;}
        case 8: {return this.UPDATE_TIME;}
        }
        return null;
    }

    /**
    * Default constructor for JavaBean convention
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public AppVersion(){}

    /**
    * Constructor for creating a new instance.
    * Arguments are index keys
    * DON'T manually use constructor. Please use <code>create<code> instead.
    */
    public AppVersion(int ID){
        this.ID = ID;
    }

    @Override
    public String _getCacheKey(){
        return _genKey(ID);
    }

    public static ArrayList<AppVersion> getAllObjects(){
        ArrayList<Object> __tmp = _tableCache.loadAll();

        ArrayList<AppVersion> __res = new ArrayList<AppVersion>();
        for (Object __obj: __tmp){
            __res.add((AppVersion)__obj);
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
            APP_VER, PLATFORM, LATEST, MUST_UPDATE, 
            DOWNLOAD_URL, CREATE_TIME, UPDATE_TIME, ID);

        return (__res > 0);
    }

    /**
    * update JPO in batch.
    * @return num of records successfully updated.
    */
    public static int updateBatch(AppVersion[] objList){
        Object[][] argv = new Object[objList.length][];

        for (int idx = 0; idx < objList.length; idx++){
            AppVersion o = objList[idx];
            Object[] args = new Object[]{
                o.APP_VER, o.PLATFORM, o.LATEST, o.MUST_UPDATE, 
                o.DOWNLOAD_URL, o.CREATE_TIME, o.UPDATE_TIME, o.ID};

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
    public static int removeBatch(AppVersion[] objList){
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
    public static AppVersion create(){

        AppVersion __newObj = new AppVersion();

        return (AppVersion) _tableCache.create(__newObj, _createSQL, 
            __newObj.APP_VER, __newObj.PLATFORM, __newObj.LATEST, __newObj.MUST_UPDATE, 
            __newObj.DOWNLOAD_URL, __newObj.CREATE_TIME, __newObj.UPDATE_TIME);
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
    public static AppVersion create(
            TableBase.AttributeValue<?> attrVal,
            TableBase.AttributeValue<?>... attrVals){

        AppVersion __newObj = new AppVersion();

        if (null != attrVal.getVal()){
            __newObj._setValue(attrVal.getFidx(), attrVal.getVal());
        }

        for (TableBase.AttributeValue<?> attrV: attrVals){
            if (null != attrV.getVal()){
                __newObj._setValue(attrV.getFidx(), attrV.getVal());
            }
        }

        return (AppVersion) _tableCache.create(__newObj, _createSQL, 
            __newObj.APP_VER, __newObj.PLATFORM, __newObj.LATEST, __newObj.MUST_UPDATE, 
            __newObj.DOWNLOAD_URL, __newObj.CREATE_TIME, __newObj.UPDATE_TIME);
    }

    /**
    * find a JPO by indexes.
    * @return the found JPO or null
    */
    public static AppVersion getOne(int ID){
        return (AppVersion) _tableCache.getOne(_genKey(ID), _querySQL, ID);
    }

    /**
    * find a JPO by arbitrary query criteria.
    * @return the found JPO or null
    */
    public static AppVersion getOneByCriteria(QueryCriteria ...criteria){
        return (AppVersion)_tableCache.getOneByCriteria(criteria);
    }

    /**
    * find all JPOs those match the query criteria list.
    * @return a list of JPO found.
    */
    public static ArrayList<AppVersion> getManyByCriteria(QueryCriteria ...criteria){
        ArrayList<Object> __tmp = _tableCache.getManyByCriteria(criteria);

        ArrayList<AppVersion> __res = new ArrayList<AppVersion>();
        for (Object __obj: __tmp){
            __res.add((AppVersion)__obj);
        }

        return __res;
    }

    /**
    * find a JPO by arbitrary SQL statement.
    * @return return the found JPO or null.
    */
    public static AppVersion getOneBySQL(String sql, Object ...args){
        return (AppVersion)_tableCache.getOneBySQL(sql, args);
    }

    /**
    * find all JPOs by arbitrary SQL statement.
    * @return a list of JPO found.
    */
    public static ArrayList<AppVersion> getManyBySQL(String sql, Object ...args){
        ArrayList<Object> __tmp = _tableCache.getManyBySQL(sql, args);

        ArrayList<AppVersion> __res = new ArrayList<AppVersion>();
        for (Object __obj: __tmp){
            __res.add((AppVersion)__obj);
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
