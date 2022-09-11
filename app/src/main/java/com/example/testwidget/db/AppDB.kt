package com.example.testwidget.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(entities = [SwitchEntity::class, WidgetIdEntity::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        fun create(context: Context): AppDB {
            synchronized(this) {
                return Room.databaseBuilder(
                    context,
                    AppDB::class.java,
                    "database-name"
                ).build()
            }
        }
    }
}

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSwitchValue(entity: SwitchEntity)

    @Query("SELECT * FROM SwitchEntity")
    suspend fun getSwitchValues(): List<SwitchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppWidgetId(entity: WidgetIdEntity)

    @Delete
    suspend fun deleteAppWidgetId(entity: WidgetIdEntity)

    @Query("SELECT * FROM WidgetIdEntity")
    suspend fun getWidgetIds(): List<WidgetIdEntity>

    @Query("SELECT * FROM SwitchEntity")
    fun getSwitchFlow(): Flow<List<SwitchEntity>>

    @Query("SELECT * FROM WidgetIdEntity")
    fun getWidgetIdFlow() : Flow<List<WidgetIdEntity>>
}

@Entity
data class SwitchEntity(
    @PrimaryKey
    val id: Int = 0,
    val enable: Boolean
)

@Entity
data class WidgetIdEntity(
    @PrimaryKey
    val id: Int
)
