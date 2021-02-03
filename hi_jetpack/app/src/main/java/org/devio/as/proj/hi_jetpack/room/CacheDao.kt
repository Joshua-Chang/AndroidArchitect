package org.devio.`as`.proj.hi_jetpack.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao//data access object 数据访问对象
interface CacheDao {
    @Query("select * from table_cache where `cache_key` =:keyword")
    fun queryList(keyword: String): List<Cache>

    @Query("select * from table_cache where `cache_key` =:keyword limit 1")
    fun query(keyword: String): Cache

    @Query("select * from table_cache")
    //1、可以同过livedata 以观察者的形式获得数据库数据，避免npe
    //2、监听数据变化，自动读取最新数据
    fun query2(): LiveData<List<Cache>>//返回livedata

    @Delete(entity = Cache::class)
    fun deleteByPrimaryKey(cacheKey: String)

    @Delete(entity = Cache::class)
    fun deleteByObject(cache: Cache)

    @Insert(entity = Cache::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(cache: Cache)

    @Update()
    fun update(cache: Cache)
}