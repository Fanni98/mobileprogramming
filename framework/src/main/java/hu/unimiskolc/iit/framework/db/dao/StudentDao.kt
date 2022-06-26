package hu.unimiskolc.iit.framework.db.dao

import androidx.room.*
import hu.unimiskolc.iit.framework.db.entity.StudentEntity

@Dao
interface StudentDao {
    @Insert
    suspend fun add(entity: StudentEntity)

    @Update
    suspend fun update(entity: StudentEntity)

    @Delete
    suspend fun remove(entity: StudentEntity)

    @Query("SELECT * FROM students WHERE neptunId = :neptunId")
    suspend fun fetchById(neptunId: Int): StudentEntity?

    @Query("SELECT * FROM students WHERE courseId = :courseId")
    suspend fun fetchByCourse(courseId: Int): List<StudentEntity>

    @Query("SELECT * from students")
    suspend fun fetchAll() :List<StudentEntity>
}