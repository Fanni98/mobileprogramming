package hu.unimiskolc.iit.framework.db.entity
import androidx.room.*

@Entity(
    tableName = "courses",
)
data class CourseEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val maxNumber: Int,
    val creditValue: Int,

)

data class CourseInfo(
    @Embedded
    val entity: CourseEntity,
    @Relation(
        entity = StudentEntity::class,
        entityColumn = "courseId",
        parentColumn = "id",
    )
    val students: List<StudentEntity>,


)


