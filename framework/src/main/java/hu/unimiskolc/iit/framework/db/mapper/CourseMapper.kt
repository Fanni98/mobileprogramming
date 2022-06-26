package hu.unimiskolc.iit.framework.db.mapper

import hu.unimiskolc.iit.core.domain.Course
import hu.unimiskolc.iit.framework.db.entity.CourseEntity
import hu.unimiskolc.iit.framework.db.entity.CourseInfo

class CourseMapper {
    private val studentMapper: StudentMapper = StudentMapper()

    fun mapToEntity(data: Course) : CourseEntity=
        CourseEntity(
            data.id,
            data.title,
            data.description,
            data.maxNumber,
            data.creditValue,

    )
    fun mapFromEntity(info: CourseInfo):Course=
        Course(
            info.entity.id,
            info.entity.title,
            info.entity.description,
            info.entity.maxNumber,
            info.entity.creditValue,
            info.students.map{studentMapper.mapFromEntity(it)}
        )
}