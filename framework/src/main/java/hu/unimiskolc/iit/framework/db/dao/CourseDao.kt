package hu.unimiskolc.iit.framework.db.dao

import androidx.room.*
import hu.unimiskolc.iit.framework.db.entity.CourseEntity
import hu.unimiskolc.iit.framework.db.entity.CourseInfo

@Dao
interface CourseDao {
    @Insert
    suspend fun add(entity: CourseEntity)

    @Update
    suspend fun update(entity: CourseEntity)

    @Delete
    suspend fun remove(entity: CourseEntity)

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun fetchById(id: Int): CourseInfo?

    @Query("SELECT * from courses")
    suspend fun fetchAll():List<CourseInfo>
}