package hu.unimiskolc.iit.framework.db.entity

import androidx.room.*
import java.util.*

@Entity(
    tableName = "students",
    foreignKeys = [
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("courseId"),
            onDelete = ForeignKey.CASCADE
        )
    ]

)
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val neptunId: Int,
    val name: String,
    val birthDate: Date,
    val courseId: Int,

)

