package com.mozat.morange.dbcache.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.SQLServerCacheManager;
import com.mozat.morange.log.DebugLog;
import com.mozat.morange.log.TraceLog;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReadWriteLock;
//import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TableCache {
    private static final Logger logger = LoggerFactory.getLogger(TableCache.class);
    private static final Logger handleTime = LoggerFactory.getLogger("handleTime");
    private static int BAD_RET = -1;

    private static AtomicInteger GlobalSQLErrorNum = new AtomicInteger(0);
    private static AtomicInteger GlobalExeSQLNum = new AtomicInteger(0);

    private AtomicInteger LocalSQLErrorNum = new AtomicInteger(0);
    private AtomicInteger LocalExeSQLNum = new AtomicInteger(0);
    private AtomicInteger LocalQueryOperNum = new AtomicInteger(0);
    private AtomicInteger LocalQueryHitCacheOperNum = new AtomicInteger(0);
    private AtomicInteger CreatedObjFoundInCache = new AtomicInteger(0);
    private AtomicInteger CreatedObjFoundByWeakRef = new AtomicInteger(0);

    private void increSQLErrorNum() {
        GlobalSQLErrorNum.incrementAndGet();
        LocalSQLErrorNum.incrementAndGet();
    }

    private void increExeSQLNum() {
        GlobalExeSQLNum.incrementAndGet();
        LocalExeSQLNum.incrementAndGet();
    }

    private SQLServerCacheManager _cacheManager;
    private TableCacheConfig _cacheConfig;
    private Class<?> _tableClass;
    private Cache _ehCache = null;

    private final Lock _readLock;
    private final Lock _writeLock;
    private final WeakValueHashMap _refObjMap;
    private HashSet<String> _columnSet;

    public TableCache(SQLServerCacheManager cacheManager, TableCacheConfig cacheConfig) {
        _cacheManager = cacheManager;
        _cacheConfig = cacheConfig;
        _refObjMap = new WeakValueHashMap();
        _columnSet = new HashSet<String>();

        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        _readLock = readWriteLock.readLock();
        _writeLock = readWriteLock.writeLock();
    }

    public boolean preload() {
        if (!_cacheConfig.isFullCache()) {
            return true;
        }

        long startMills = System.currentTimeMillis();
        ArrayList<TableBase> allObjects = runQuerySQL(_tableClass, "select * from " + _cacheConfig.tableName + ";", new Object[]{});

        for (TableBase tb : allObjects) {
            addObjectToCache(tb._getCacheKey(), tb);
        }

        long endMills = System.currentTimeMillis();
        DebugLog.info("[TableCache] Loading " + _cacheConfig.tableName +
                ". RecordNum: " + _ehCache.getSize() + ", timeUsed: " + (endMills - startMills));

        return true;
    }

    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> loadAll() {
        ArrayList<T> res = new ArrayList<T>();

        // full cache
        if (_cacheConfig.isFullCache()) {
            this._readLock.lock();
            long start = System.currentTimeMillis();
            try {
                for (Object obj : _refObjMap.values()) {
                    res.add((T) obj);
                }
            } finally {
                this._readLock.unlock();
            }
            long elapsed = System.currentTimeMillis() - start;
            if (elapsed > 50) {
                handleTime.info("[TableCache.loadAll], time=" + elapsed + ",tname=" + Thread.currentThread().getName());
            }
            return res;
        }

        // lazy or none cache
        return this.getManyBySQL("select * from " + _cacheConfig.tableName + ";", new Object[]{});
    }

    public int update(TableBase obj, String sql, Object... args) {
        int res = this.runUpdateSQL(sql, args);
        if (res > 0) {
            this.addObjectToCache(obj._getCacheKey(), obj);
        } else {
            logger.error("[TableCache.update]. tableName: " + _cacheConfig.tableName
                    + ". Not Updated: " + obj.toString() + ", SQL: " + sql);
        }

        return res;
    }

    public int updateBatch(TableBase[] objList, String sql, Object[][] argv) {
        int res = this.runUpdateSQLBatch(sql, argv);
        if (res != objList.length) {
            logger.error("[TableCache.updateBatch] Only " + res + " of " + objList.length + " updated. tableName:"
                    + _cacheConfig.tableName);
        }

        if (res > 0) {
            for (TableBase obj : objList) {
                this.addObjectToCache(obj._getCacheKey(), obj);
            }
        }

        return res;
    }

    public int updateByCriteria(TableBase.AttributeValue<?>[] valueList, QueryCriteria[] qcList) {
        if ((valueList.length == 0) || (qcList.length == 0)) {
            return 0;
        }

        ArrayList<Object> outArgs = new ArrayList<Object>();
        String setSQL = " SET ";
        Set<Integer> keySet = new HashSet<Integer>();
        List<TableBase.AttributeValue<?>> uniqueValueList = new LinkedList<TableBase.AttributeValue<?>>();

        boolean isFirst = true;
        for (TableBase.AttributeValue<?> attrVal : valueList) {
            if (keySet.contains(attrVal.getFidx())) {
                continue;
            }

            keySet.add(attrVal.getFidx());
            uniqueValueList.add(attrVal);

            if (isFirst) {
                isFirst = false;
            } else {
                setSQL += ", ";
            }
            //setSQL += "[" + attrVal.getName() + "] = ?"; //SQL Server
            setSQL += "`" + attrVal.getName() + "` = ?";   //MySQL
            outArgs.add(attrVal.getVal());
        }

        String sql = "UPDATE " + _cacheConfig.tableName + setSQL + " WHERE ";
        String querySQL = QueryCriteria.toAndSQL(qcList, outArgs);
        sql += querySQL + ";";

        int res = this.runUpdateSQL(sql, outArgs.toArray());

        if (res > 0) {
            ArrayList<TableBase> found = this._getManyByCriteriaFromCache(qcList);
            for (TableBase tb : found) {
                for (TableBase.AttributeValue<?> val : uniqueValueList) {
                    tb._setValue(val.getFidx(), val.getVal());
                }
            }
        }

        return res;
    }

    public int remove(TableBase obj, String sql, Object primaryKey) {
        int res = this.runUpdateSQL(sql, new Object[]{primaryKey});
        if (res > 0) {
            this.removeObjectFromCache(obj._getCacheKey());
        }

        return res;
    }

    public int removeByCriteria(QueryCriteria qc, QueryCriteria... qcs) {
        QueryCriteria[] qcList = new QueryCriteria[qcs.length + 1];
        qcList[0] = qc;
        System.arraycopy(qcs, 0, qcList, 1, qcs.length);

        ArrayList<Object> outArgs = new ArrayList<Object>();
        String sql = QueryCriteria.getDeleteSQL(this._cacheConfig.tableName, qcList, outArgs);

        int res = this.runUpdateSQL(sql, outArgs.toArray());

        if (res > 0) {
            this._removeByCriteriaFromCache(qcList);
        }

        return res;
    }

    public int removeBatch(TableBase[] objList, String sql, Object[] pkList) {
        Object[][] argv = new Object[pkList.length][];
        for (int idx = 0; idx < pkList.length; ++idx) {
            Object[] args = new Object[]{pkList[idx]};
            argv[idx] = args;
        }

        int res = this.runUpdateSQLBatch(sql, argv);
        if (res != objList.length) {
            logger.error("[TableCache.removeBatch] Only " + res + " of " + objList.length + " removed.");
        }

        if (res > 0) {
            for (TableBase obj : objList) {
                this.removeObjectFromCache(obj._getCacheKey());
            }
        }

        return res;
    }

    public int removeAll() {
        String sql = "DELETE FROM " + _cacheConfig.tableName + ";";
        int res = this.runUpdateSQL(sql, new Object[]{});

        if (res > 0) {
            this.clearCache();
        }

        return res;
    }

    public Object create(TableBase newObj, String sql, Object... args) {
        Object found = this.getObjectFromCache(newObj._getCacheKey(), false);
        if (found != null) {
            TraceLog.info("[TableCache.create] Object Found in Cache. key=" + newObj._getCacheKey() + ", table=" + _cacheConfig.tableName);
            return found;
        }

        Long res = this.runCreateSQL(sql, args);
        if (res < 0) {
            return null;
        }

        if (newObj._isIdAutoGenerated()) {
            if (res == 0) {
                return null;
            }

            newObj._setIdValue(res);

			/*try{
                Field idField = newObj.getClass().getDeclaredField(this._cacheConfig.idColumn);
                Class<?> idFieldType = idField.getType();
                if (idFieldType.equals(int.class) || idFieldType.equals(Integer.class)) {
                    idField.set(newObj, res.intValue());
                } else if (idFieldType.equals(short.class) || idFieldType.equals(Short.class)) {
                    idField.set(newObj, res.shortValue());
                } else {
                    idField.set(newObj, res);
                }
            } catch (Exception ex) {
                logger.error("[TableCache.create Error]. SQL: " + sql, ex);
                return null;
			}*/
        }

        return this.addObjectToCache(newObj._getCacheKey(), newObj);

        // return newObj;
    }

    public Object getOne(String key, String sql, Object... args) {
        Object obj = this.getObjectFromCache(key, true);
        if (null != obj) {
            return obj;
        }

        if (_cacheConfig.isFullCache()) {
            return null;
        }

        ArrayList<Object> results = this.runQuerySQL(_tableClass, sql, args);
        obj = (results.size() > 0 ? results.get(0) : null);
        if (null != obj) {
            return this.addObjectToCache(key, obj);
        }

        return null;
    }

    public Object getOneByCriteria(QueryCriteria[] criteria) {
        LocalQueryOperNum.incrementAndGet();

        Object cachedObj = this._getOneByCriteriaFromCache(criteria);
        if ((null != cachedObj) || _cacheConfig.isFullCache()) {
            LocalQueryHitCacheOperNum.incrementAndGet();

            return cachedObj;
        }

        ArrayList<Object> outArgs = new ArrayList<Object>();
        String sql = QueryCriteria.getQuerySQL(this._cacheConfig.tableName, criteria, outArgs);

        return getOneBySQL(sql, outArgs.toArray());
    }

    public <T> ArrayList<T> getManyByCriteria(QueryCriteria[] criteria) {
        LocalQueryOperNum.incrementAndGet();

        if (_cacheConfig.isFullCache()) {
            LocalQueryHitCacheOperNum.incrementAndGet();

            return this._getManyByCriteriaFromCache(criteria);
        }

        ArrayList<Object> outArgs = new ArrayList<Object>();
        String sql = QueryCriteria.getQuerySQL(this._cacheConfig.tableName, criteria, outArgs);

        return getManyBySQL(sql, outArgs.toArray());
    }

    public Object getOneBySQL(String sql, Object[] args) {
        ArrayList<TableBase> results = this.runQuerySQL(_tableClass, sql, args);
        TableBase obj = results.size() > 0 ? results.get(0) : null;

//        if (obj != null) {
//            Object cacheObj = this.getObjectFromCache(obj._getCacheKey(), false);
//            if (null != cacheObj) {
//                return cacheObj;
//            } else {
//                return this.addObjectToCache(obj._getCacheKey(), obj);
//                //return obj;
//            }
//        }
        
        //change by gaopeidian 2017-06-17
        if (obj != null) {
            removeObjectFromCache(obj._getCacheKey());
            return this.addObjectToCache(obj._getCacheKey(), obj);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> getManyBySQL(String sql, Object[] args) {
        ArrayList<T> res = new ArrayList<T>();

        ArrayList<TableBase> results = this.runQuerySQL(_tableClass, sql, args);

        for (TableBase tb : results) {
            /**Object cachedObj = this.getObjectFromCache(tb._getCacheKey(), false);
            if (null != cachedObj) {
                res.add((T) cachedObj);
            } else {*/
                Object newObj = this.addObjectToCache(tb._getCacheKey(), tb);
                res.add((T) newObj);
            //}
        }

        return res;
    }

    private int _removeByCriteriaFromCache(QueryCriteria[] criteria) {
        if (_ehCache == null) {
            return 0;
        }

        this._writeLock.lock();
        try {
            Set<String> keySet = new HashSet<String>();

            for (Object key : _refObjMap.keySet()) {
                Object obj = _refObjMap.get(key);
                if (obj == null) {
                    continue;
                }
                if (QueryCriteria.fulfills(criteria, (TableBase) obj)) {
                    keySet.add((String) key);
                }
            }

            for (String key : keySet) {
                this.removeObjectFromCache(key);
            }

            return keySet.size();
        } finally {
            this._writeLock.unlock();
        }
    }

    private Object _getOneByCriteriaFromCache(QueryCriteria[] criteria) {
        if (_ehCache == null) {
            return null;
        }

        this._readLock.lock();
        long start = System.currentTimeMillis();
        try {
            for (Object key : _refObjMap.keySet()) {
                Object obj = _refObjMap.get(key);
                if (obj == null) {
                    continue;
                }
                if (QueryCriteria.fulfills(criteria, (TableBase) obj)) {
                    return obj;
                }
            }
        } finally {
            this._readLock.unlock();
        }
        long elapsed = System.currentTimeMillis() - start;
        if (elapsed > 50) {
            handleTime.info("[TableCache._getOneByCriteriaFromCache], tableName=" + this._cacheConfig.tableName + ",time=" + elapsed + ",tname=" + Thread.currentThread().getName());
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> ArrayList<T> _getManyByCriteriaFromCache(QueryCriteria[] criteria) {
        ArrayList<T> res = new ArrayList<T>();

        if (null == _ehCache) {
            return res;
        }

        this._readLock.lock();
        long start = System.currentTimeMillis();
        try {
            for (Object key : _refObjMap.keySet()) {
                Object obj = _refObjMap.get(key);
                if (obj == null) {
                    continue;
                }
                if (QueryCriteria.fulfills(criteria, (TableBase) obj)) {
                    res.add((T) obj);
                }
            }
        } finally {
            this._readLock.unlock();
        }
        long elapsed = System.currentTimeMillis() - start;
        if (elapsed > 100) {
            handleTime.info("[TableCache._getManyByCriteriaFromCache], tableName=" + this._cacheConfig.tableName + ",time=" + elapsed + ",tname=" + Thread.currentThread().getName());
        }

        return res;
    }

    public boolean init() throws Exception {
        _tableClass = Class.forName(_cacheConfig.javaClass);

        if (!validateTable()) {
            return false;
        }

        if (!initEhCache()) {
            return false;
        }

        return true;
    }

    private boolean validateTable() throws Exception {
        _columnSet = this.getColumnSet();

        HashSet<String> mismatchFields = new HashSet<String>();

        // class fields
        Field[] fields = _tableClass.getFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            String fieldName = field.getName();
            if (fieldName.startsWith("_")) {
                continue;
            }

            if (!_columnSet.contains(fieldName)) {
                mismatchFields.add(fieldName);
            }
        }

        if (mismatchFields.size() > 0) {
            logger.error("Fields " + mismatchFields + " of " + _cacheConfig.javaClass
                    + " are not columns of table " + _cacheConfig.tableName);

            return false;
        }

        Field tableNameField = _tableClass.getDeclaredField("_tableName");
        Field tableCacheField = _tableClass.getDeclaredField("_tableCache");
        tableNameField.setAccessible(true);
        tableCacheField.setAccessible(true);
        tableNameField.set(null, _cacheConfig.tableName);
        tableCacheField.set(null, this);

        return true;
    }

    private boolean initEhCache() throws Exception {
        int maxElementInMemory = _cacheConfig.maxElementInMenory;
        boolean eternal = _cacheConfig.eternal;
        if (_cacheConfig.isFullCache()) {
            maxElementInMemory = 10000000;
            eternal = true;
        }

        CacheConfiguration ehCacheConfig = new CacheConfiguration(_cacheConfig.javaClass, maxElementInMemory);
        ehCacheConfig.eternal(eternal);

        //ehCacheConfig.overflowToDisk(_cacheConfig.overflowToDisk);
        if (!eternal) {
            ehCacheConfig.timeToIdleSeconds(_cacheConfig.timeToIdleSeconds);
            ehCacheConfig.timeToLiveSeconds(_cacheConfig.timeToLiveSeconds);
        }
        //ehCacheConfig.diskPersistent(_cacheConfig.diskPersistent);
        ehCacheConfig.diskExpiryThreadIntervalSeconds(_cacheConfig.diskExpiryThreadIntervalSeconds);
        ehCacheConfig.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU);
        //ehCacheConfig.persistence(new PersistenceConfiguration().strategy(Strategy.LOCALTEMPSWAP));

        if (!_cacheConfig.isNoneCache()) {
            _ehCache = new Cache(ehCacheConfig);
            _cacheManager.getEhCacheManager().addCache(_ehCache);
        }

        return true;
    }

    private Object getObjectFromCache(String key, boolean logHits) {
        if (logHits) {
            LocalQueryOperNum.incrementAndGet();
        }

        this._readLock.lock();
        try {
            // referenced ?
            Object res = _refObjMap.get(key);
            if (null != res) {
                if (logHits) {
                    LocalQueryHitCacheOperNum.incrementAndGet();
                } else {
                    CreatedObjFoundByWeakRef.incrementAndGet();
                }
                /*
                TableBase tb = (TableBase) res;
                if (tb._isIdAutoGenerated() && key.equals("0")) {
                    throw new IllegalArgumentException("putting cache key 0, tableName=" + tb._getTableName());
                }

                if (null != _ehCache && !_ehCache.isKeyInCache(key)) {
                    _ehCache.put(new Element(key, res));
                }
                */
                return res;
            }

            // Or cached ?
            if (_ehCache != null) {
                Element elem = _ehCache.get(key);
                if (null != elem) {
                    if (logHits) {
                        LocalQueryHitCacheOperNum.incrementAndGet();
                    } else {
                        CreatedObjFoundInCache.incrementAndGet();
                    }

                    return elem.getObjectValue();
                }
            }
        } finally {
            this._readLock.unlock();
        }

        return null;
    }

    private Object addObjectToCache(String key, Object obj) {
        this._writeLock.lock();
        try {
            Object refObj = _refObjMap.get(key);
//            if (refObj != null) {
//                obj = refObj;
//            }

            if (obj == null) {
                throw new IllegalArgumentException("obj is null, key=" + key);
            }
            TableBase tb = (TableBase) obj;
            /* 有可能因为某个SQL语句的查询不带主键导致key为默认值 */
            if (tb._isIdAutoGenerated() && key.equals("0")) {
                throw new IllegalArgumentException("putting cache key 0, tableName=" + tb._getTableName());
            }

            if (!this._cacheConfig.isNoneCache()) {
                _ehCache.put(new Element(key, obj));
            }

            if (refObj == null) {
                _refObjMap.put(key, obj);
            }

            return obj;
        } finally {
            this._writeLock.unlock();
        }
    }

    private void removeObjectFromCache(String key) {
        this._writeLock.lock();
        try {
            if (!this._cacheConfig.isNoneCache()) {
                _ehCache.remove(key);
            }
            _refObjMap.remove(key);
        } finally {
            this._writeLock.unlock();
        }
    }

    private void clearCache() {
        this._writeLock.lock();
        try {
            if (null != _ehCache) {
                _ehCache.removeAll();
            }

            _refObjMap.clear();
        } finally {
            this._writeLock.unlock();
        }
    }

    public int runUpdateSQL(String sql, Object[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;
        long start = Long.MAX_VALUE;
        try {
            conn = _cacheManager.getConnection();
            start = System.currentTimeMillis();
            stmt = conn.prepareStatement(sql);
            parseSQLArgs(stmt, args);

            int res = stmt.executeUpdate();

            if (res <= 0) {
                DebugLog.info("[TableCache.runUpdateSQL] Non-effect update SQL: " + sql);
            }

            return res;
        } catch (Exception ex) {
            increSQLErrorNum();
            logger.error("[TableCache.runUpdateSQL] Error SQL: " + sql, ex);
            return BAD_RET;
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            if (elapsed > 50) {
                handleTime.info("[TableCache.runUpdateSQL], sql=" + sql + ",time=" + elapsed + ",tname=" + Thread.currentThread().getName());
            }
            increExeSQLNum();
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                    logger.error("[TableCache.runUpdateSQL] Error SQL: " + sql, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    logger.error("[TableCache.runUpdateSQL] Error SQL: " + sql, ex);
                }
            }
        }
    }

    public int runUpdateSQLBatch(String sql, Object[][] argv) {
        Connection conn = null;
        PreparedStatement stmt = null;
        long start = Long.MAX_VALUE;
        try {
            conn = _cacheManager.getConnection();
            start = System.currentTimeMillis();
            stmt = conn.prepareStatement(sql);
            for (Object[] args : argv) {
                parseSQLArgs(stmt, args);
                stmt.addBatch();
            }

            int[] rets = stmt.executeBatch();

            if (rets.length <= 0) {
                DebugLog.info("[TableCache.runUpdateSQLBatch] Non-effect update SQL: " + sql);
            }

            int res = 0;
            for (int rt : rets) {
                if (rt > 0) {
                    res++;
                }
            }

            return res;
        } catch (Exception ex) {
            increSQLErrorNum();
            logger.error("[TableCache.runUpdateSQLBatch] Error SQL: " + sql, ex);
            return BAD_RET;
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            if (elapsed > 50) {
                handleTime.info("[TableCache.runUpdateSQLBatch], sql=" + sql + ",time=" + elapsed + ",tname=" + Thread.currentThread().getName());
            }
            increExeSQLNum();
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                    logger.error("[TableCache.runUpdateSQLBatch] Error SQL: " + sql, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    logger.error("[TableCache.runUpdateSQLBatch] Error SQL: " + sql, ex);
                }
            }
        }
    }

    public long runCreateSQL(String sql, Object[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;
        long start = Long.MAX_VALUE;
        try {
            conn = _cacheManager.getConnection();
            start = System.currentTimeMillis();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            parseSQLArgs(stmt, args);

            stmt.executeUpdate();
            ResultSet autoKeys = stmt.getGeneratedKeys();
            if (autoKeys.next()) {
                return autoKeys.getLong(1);
            }

            if (!_cacheConfig.isPkInIndex) {
                logger.error("[TableCache.runCreateSQL] Pk not in index, SQL: " + sql);
                return BAD_RET;
            }

            return 0;
        } catch (Exception ex) {
            increSQLErrorNum();
            logger.error("[TableCache.runCreateSQL] Error SQL: " + sql, ex);
            return BAD_RET;
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            if (elapsed > 50) {
                handleTime.info("[TableCache.runCreateSQL], sql=" + sql + ",time=" + elapsed + ",tname=" + Thread.currentThread().getName());
            }
            increExeSQLNum();
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                    logger.error("[TableCache.runCreateSQL] Error SQL: " + sql, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    logger.error("[TableCache.runCreateSQL] Error SQL: " + sql, ex);
                }
            }
        }
    }

    private HashSet<String> getColumnSet() {
        HashSet<String> columnSet = new HashSet<String>();

        String sql = "select column_name from information_schema.columns where table_name = ?;";
        Object[] args = new Object[]{_cacheConfig.tableName};

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = _cacheManager.getConnection();
            stmt = conn.prepareStatement(sql);
            parseSQLArgs(stmt, args);

            ResultSet rs = stmt.executeQuery();
            ResultSetWP rswp = new ResultSetWP(rs);
            ArrayList<Object> results = rswp.getAllObject(TableField.class);
            for (Object fn : results) {
                TableField tfld = (TableField) fn;
                //columnSet.add(tfld.column_name);
                columnSet.add(tfld.COLUMN_NAME);
            }

            return columnSet;
        } catch (Exception ex) {
            logger.error("[TableCache.getColumnSet] " + _cacheConfig.tableName + ". Ex: ", ex);
            return columnSet;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                    stmt = null;
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    conn = null;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> ArrayList<T> runQuerySQL(Class<?> clazz, String sql, Object[] args) {
        ArrayList<T> results = new ArrayList<T>();

        Connection conn = null;
        PreparedStatement stmt = null;
        long start = Long.MAX_VALUE;
        try {
            conn = _cacheManager.getConnection();
            start = System.currentTimeMillis();
            stmt = conn.prepareStatement(sql);
            parseSQLArgs(stmt, args);

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();
            Map<String, Integer> mapFields = new HashMap<String, Integer>();

            int fieldCount = rsMeta.getColumnCount();
            if (fieldCount == 0) {
                return results;
            }

            for (int i = 1; i <= fieldCount; i++) {
                String fieldName = rsMeta.getColumnName(i);
                mapFields.put(fieldName, i);
            }

            boolean notAllColumnCovered = false;

            while (rs.next()) {
                TableBase newObj = (TableBase) clazz.newInstance();
                Map<String, Integer> fieldMap = newObj._getFieldMap();

                for (Map.Entry<String, Integer> entry : fieldMap.entrySet()) {
                    Integer fieldIdx = mapFields.get(entry.getKey());

                    if (null != fieldIdx) {
                        Object value = rs.getObject(fieldIdx);
                        if (value != null) {
                            newObj._setValue(entry.getValue(), value);
                        }
                    } else {
                        notAllColumnCovered = true;
                    }
                }

                results.add((T) newObj);
            }

            if (notAllColumnCovered) {
                logger.error("[TableCache.runQuerySQL] Not all fields covered: " + clazz.getName());
            }

            return results;
        } catch (Exception ex) {
            increSQLErrorNum();
            logger.error("[TableCache.runQuerySQL] Error SQL: " + sql, ex);
            return null;
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            if (elapsed > 100) {
                handleTime.info("[TableCache.runQuerySQL], sql=" + sql + ",time=" + elapsed + ",tname=" + Thread.currentThread().getName());
            }
            increExeSQLNum();
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                    logger.error("[TableCache.runQuerySQL] Error SQL: " + sql, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    logger.error("[TableCache.runQuerySQL] Error SQL: " + sql, ex);
                }
            }
        }
    }

    private static void parseSQLArgs(PreparedStatement stmt, Object[] args) throws Exception {
        if (args == null) {
            return;
        }
        for (int idx = 0; idx < args.length; ++idx) {
            Object obj = args[idx];
            if (obj instanceof java.util.Date) {
                java.util.Date dt = (java.util.Date) obj;
                java.sql.Timestamp timeStamp = new java.sql.Timestamp(dt.getTime());
                stmt.setTimestamp(idx + 1, timeStamp);
            } else {
                stmt.setObject(idx + 1, obj);
            }
        }
    }

    private int _evaluateMemory() {
        this._readLock.lock();
        try {
            if (_refObjMap.isEmpty()) {
                return 0;
            }

            int acc = 0;
            int tmpM = 0;

            for (Object key : _refObjMap.keySet()) {
                Serializable obj = (Serializable) _refObjMap.get(key);
                tmpM += MemoryMonitor.getObjectSize(obj);
                acc++;
                if (acc >= 10) {
                    break;
                }
            }

            if (acc > 0) {
                return tmpM / acc;
            }

            return 0;
        } finally {
            this._readLock.unlock();
        }
    }


    public void logCacheStatus() {
        this._readLock.lock();
        int refSize;
        try {
            refSize = _refObjMap.size();
        } finally {
            this._readLock.unlock();
        }
        int memory = _evaluateMemory(), cachedSize = (_ehCache != null) ? _ehCache.getSize() : 0;
        String log = "[CacheInfo] tableName:" + _cacheConfig.tableName +
                ",CacheCategory:" + _cacheConfig.category +
                ",globalSQLExecNum:" + GlobalExeSQLNum +
                ",globalSQLErrorNum:" + GlobalSQLErrorNum +
                ",localSQLExecNum:" + LocalExeSQLNum +
                ",localSQLErrorNum:" + LocalSQLErrorNum +
                ",MemoryPerRecord(Byte):" + memory +
                ",CachedObjectNum:" + cachedSize +
                ",ReferencedObjectNum:" + refSize +
                ",TotalQueryNum:" + LocalQueryOperNum +
                ",TotalHitCacheQueryOperNum:" + LocalQueryHitCacheOperNum +
                ",CreatedObjFoundInCache:" + CreatedObjFoundInCache +
                ",CreatedObjFoundByWeakRef:" + CreatedObjFoundByWeakRef;

        DebugLog.info(log);

        if (memory * cachedSize > 10000000) {
            DebugLog.info("[MemoryWarning] tableName:" + _cacheConfig.tableName);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> _queryByArbitarySQL(Class<T> clazz, String sql, Object[] args) {
        ArrayList<T> results = new ArrayList<T>();

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = _cacheManager.getConnection();
            stmt = conn.prepareStatement(sql);
            parseSQLArgs(stmt, args);

            ResultSet rs = stmt.executeQuery();
            ResultSetWP rswp = new ResultSetWP(rs);

            while (true) {
                T obj = (T) rswp.getNext(clazz);
                if (null == obj) {
                    break;
                }

                results.add(obj);
            }

            return results;
        } catch (Exception ex) {
            increSQLErrorNum();
            logger.error("[TableCache._queryByArbitarySQL] Error SQL: " + sql + ". Ex: ", ex);
            return null;
        } finally {
            increExeSQLNum();
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                    stmt = null;
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    conn = null;
                }
            }
        }
    }

    //=======================================
    //=======================================
    // query wrapper
    public static class ResultSetWP {
        private HashMap<String, Integer> _mapFields = new HashMap<String, Integer>();
        private ArrayList<Object[]> _data = new ArrayList<Object[]>();
        private int _index = -1;

        public ResultSetWP(ResultSet rs) throws Exception {
            ResultSetMetaData rsMeta = rs.getMetaData();

            int fieldCount = rsMeta.getColumnCount();
            if (fieldCount == 0) {
                return;
            }

            for (int i = 1; i < fieldCount + 1; i++) {
                String fieldName = rsMeta.getColumnName(i);
                _mapFields.put(fieldName, i - 1);
            }

            while (rs.next()) {
                Object[] row = new Object[fieldCount];

                for (int i = 1; i < fieldCount + 1; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                _data.add(row);
            }
        }

        public Object getNext(Class<?> clazz) {
            _index += 1;
            if (_index >= _data.size()) {
                return null;
            }

            try {
                if (clazz.isPrimitive()) {
                    return _data.get(_index)[0];
                }
                Object res = clazz.newInstance();

                Field[] fields = clazz.getFields();
                boolean notAllColumnCovered = false;

                for (Field field : fields) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }

                    Integer fieldIdx = _mapFields.get(field.getName());
                    if (null != fieldIdx) {
                        Object value = _data.get(_index)[fieldIdx];
                        if (value != null) {
                            field.set(res, _data.get(_index)[fieldIdx]);
                        }
                    } else {
                        notAllColumnCovered = true;
                    }
                }

                if (notAllColumnCovered) {
                    //DebugLog.info("[ResultSetWP.getNext] Not all fields covered: " + clazz.getName());
                }

                return res;
            } catch (Exception ex) {
                logger.error("[ResultSetWP.getNext] Ex: ", ex);

                return null;
            }
        }

        private ArrayList<Object> getAllObject(Class<?> clazz) {
            ArrayList<Object> res = new ArrayList<Object>();

            _index = -1;

            do {
                Object val = getNext(clazz);
                if (val == null) {
                    break;
                }

                res.add(val);

            } while (true);

            return res;
        }
    }

    public static class TableField {
        //public String column_name = "";
    	public String COLUMN_NAME = "";

        public TableField() {
        }
    }
}
