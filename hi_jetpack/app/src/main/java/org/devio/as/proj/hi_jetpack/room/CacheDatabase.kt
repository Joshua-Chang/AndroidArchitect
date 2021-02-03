package org.devio.`as`.proj.hi_jetpack.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//exportSchema在gradle配置路径后生成json
@Database(entities = [Cache::class, User::class], version = 1, exportSchema = true)
abstract class CacheDatabase : RoomDatabase() {
    abstract val cacheDao: CacheDao

    companion object {
        private var database: CacheDatabase? = null

        @Synchronized
        fun get(context: Context): CacheDatabase {
            if (database == null) {
                val database =
                    //内存数据库，这种数据库中的数据只存在于内存中，进程杀死后数据丢失
                    Room.inMemoryDatabaseBuilder(context, CacheDatabase::class.java)
                Room.databaseBuilder(context,CacheDatabase::class.java,"jetpack"/*数据库名*/)
//                    .allowMainThreadQueries()/*默认不许主线程操作数据库*/
//                    .addCallback(callback)
//                    .setQueryExecutor{}/*查询时的线程池*/
//                    .openHelperFactory()/*创建sqliteopenhelper,默认frameworksqliteopenhelperFactory，可自行实现用来加密等用途*/
//                    .addMigrations(migration1_2)/*数据库升级操作*/
                    .build()

            }
            return database!!
        }
        val callback=object :RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
        val migration1_2=object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table table_cache add column cache_time LONG")
            }
        }
    }
}