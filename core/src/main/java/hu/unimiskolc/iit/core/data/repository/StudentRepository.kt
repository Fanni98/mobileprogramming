package hu.unimiskolc.iit.core.data.repository

import hu.unimiskolc.iit.core.data.datasource.StudentDataSource
import hu.unimiskolc.iit.core.domain.Student

class StudentRepository(private val dataSource: StudentDataSource) {
    suspend fun add(student: Student) = dataSource.add(student)
    suspend fun update(student: Student) = dataSource.update(student)
    suspend fun remove(student: Student) = dataSource.remove(student)

    suspend fun fetchById(neptunId: Int): Student? = dataSource.fetchById(neptunId)
    suspend fun fetchByCourse(course: Int): List<Student> = dataSource.fetchByCourse(course)
    suspend fun fetchAll(): List<Student> = dataSource.fetchAll()
}