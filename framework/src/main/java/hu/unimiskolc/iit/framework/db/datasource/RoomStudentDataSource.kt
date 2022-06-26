package hu.unimiskolc.iit.framework.db.datasource

import hu.unimiskolc.iit.core.data.datasource.StudentDataSource
import hu.unimiskolc.iit.core.domain.Student
import hu.unimiskolc.iit.framework.db.dao.StudentDao
import hu.unimiskolc.iit.framework.db.mapper.StudentMapper

class RoomStudentDataSource (private val dao: StudentDao, private val mapper: StudentMapper):
    StudentDataSource {
    override suspend fun add(student: Student) = dao.add(mapper.mapToEntity(student))

    override suspend fun update(student: Student) = dao.update(mapper.mapToEntity(student))

    override suspend fun remove(student: Student) = dao.remove(mapper.mapToEntity(student))

    override suspend fun fetchById(neptunId: Int): Student? = dao.fetchById(neptunId)?.let { mapper.mapFromEntity(it) }

    override suspend fun fetchByCourse(course: Int): List<Student> =dao.fetchByCourse(course).map { mapper.mapFromEntity(it) }

    override suspend fun fetchAll(): List<Student> = dao.fetchAll().map { mapper.mapFromEntity(it) }

}