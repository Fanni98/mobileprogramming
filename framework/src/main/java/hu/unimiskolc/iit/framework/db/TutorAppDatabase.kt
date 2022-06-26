package hu.unimiskolc.iit.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.unimiskolc.iit.framework.db.converter.DateTypeConverter
import hu.unimiskolc.iit.framework.db.dao.CourseDao
import hu.unimiskolc.iit.framework.db.dao.StudentDao
import hu.unimiskolc.iit.framework.db.entity.CourseEntity
import hu.unimiskolc.iit.framework.db.entity.StudentEntity

@Database(
    entities = [CourseEntity::class, StudentEntity::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(
    DateTypeConverter::class,
)

abstract class TutorAppDatabase:RoomDatabase() {
    companion object  {
        private const val DATABASE_NAME = "tutorApp.db"
        private var instance: TutorAppDatabase? = null

        private fun create(context: Context) : TutorAppDatabase = Room.databaseBuilder(context, TutorAppDatabase::class.java, DATABASE_NAME)
            .addCallback(DB_CALLBACK)
            .build()

        fun getInstance(context: Context) : TutorAppDatabase = (instance ?: create(context).also { instance = it })

        private val DB_CALLBACK = object: RoomDatabase.Callback() {
        }
    }

    abstract fun courseDao() : CourseDao
    abstract fun studentDao(): StudentDao
}