package hu.unimiskolc.iit.core.data.datasource

import hu.unimiskolc.iit.core.domain.Student

interface StudentDataSource {
    suspend fun add(student: Student)
    suspend fun update(student: Student)
    suspend fun remove(student: Student)

    suspend fun fetchById(neptunId: Int): Student?
    suspend fun fetchByCourse(course: Int): List<Student>
    suspend fun fetchAll(): List<Student>
}