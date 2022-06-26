package hu.unimiskolc.iit.core.data.repository

import hu.unimiskolc.iit.core.data.datasource.CourseDataSource
import hu.unimiskolc.iit.core.domain.Course

class CourseRepository (private val dataSource: CourseDataSource) {
    suspend fun add(course: Course) = dataSource.add(course)
    suspend fun update(course: Course) = dataSource.update(course)
    suspend fun remove(course: Course) = dataSource.remove(course)

    suspend fun fetchById(id: Int): Course? = dataSource.fetchById(id)
    suspend fun fetchAll(): List<Course> = dataSource.fetchAll()
}