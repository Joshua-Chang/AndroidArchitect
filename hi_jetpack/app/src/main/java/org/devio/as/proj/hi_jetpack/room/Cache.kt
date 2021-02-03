package org.devio.`as`.proj.hi_jetpack.room

import android.graphics.Bitmap
import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "table_cache")
class Cache {
    @PrimaryKey(autoGenerate = false)
    var cache_key: String = ""

    @ColumnInfo(name = "cacheId", defaultValue = "1")
    var cache_id: Long = 0

    @Ignore
    var bitmap: Bitmap? = null

    @Embedded
    var user: User? = null
}

@Entity(tableName = "table_user")
class User {
    @PrimaryKey
    @NonNull
    var name: String = ""
    var age = 10
}
