package com.fiap.comunidadeconectada.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fiap.comunidadeconectada.data.dao.EventDao
import com.fiap.comunidadeconectada.data.dao.MessageDao
import com.fiap.comunidadeconectada.data.dao.PostDao
import com.fiap.comunidadeconectada.data.dao.RatingDao
import com.fiap.comunidadeconectada.data.dao.ResourceDao
import com.fiap.comunidadeconectada.data.dao.TransactionDao
import com.fiap.comunidadeconectada.data.dao.UserDao
import com.fiap.comunidadeconectada.data.entities.EventEntity
import com.fiap.comunidadeconectada.data.entities.MessageEntity
import com.fiap.comunidadeconectada.data.entities.PostEntity
import com.fiap.comunidadeconectada.data.entities.RatingEntity
import com.fiap.comunidadeconectada.data.entities.ResourceEntity
import com.fiap.comunidadeconectada.data.entities.TransactionEntity
import com.fiap.comunidadeconectada.data.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ResourceEntity::class,
        MessageEntity::class,
        RatingEntity::class,
        TransactionEntity::class,
        EventEntity::class,
        PostEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun resourceDao(): ResourceDao
    abstract fun messageDao(): MessageDao
    abstract fun ratingDao(): RatingDao
    abstract fun transactionDao(): TransactionDao
    abstract fun eventDao(): EventDao
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "comunidade_conectada_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
