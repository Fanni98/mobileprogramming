package hu.unimiskolc.iit.core.data.datasource

import hu.unimiskolc.iit.core.domain.Course

interface CourseDataSource {
    suspend fun add(course: Course)
    suspend fun update(course: Course)
    suspend fun remove(course: Course)

    suspend fun fetchById(id: Int): Course?
    suspend fun fetchAll(): List<Course>
}