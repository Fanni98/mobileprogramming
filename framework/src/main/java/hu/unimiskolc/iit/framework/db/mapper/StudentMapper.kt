package hu.unimiskolc.iit.framework.db.mapper

import hu.unimiskolc.iit.core.domain.Student
import hu.unimiskolc.iit.framework.db.entity.StudentEntity

class StudentMapper {

    fun mapToEntity(data: Student) : StudentEntity=
        StudentEntity(
            data.neptunId,
            data.name,
            data.birthDate,
            data.course?.id?:0,
    )
    fun mapFromEntity(entity: StudentEntity):Student=
        Student(
            entity.neptunId,
            entity.name,
            entity.birthDate,
            null
        )
}