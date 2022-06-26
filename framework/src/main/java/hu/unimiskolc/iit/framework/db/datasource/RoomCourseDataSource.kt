package hu.unimiskolc.iit.framework.db.datasource

import hu.unimiskolc.iit.core.data.datasource.CourseDataSource
import hu.unimiskolc.iit.core.domain.Course
import hu.unimiskolc.iit.framework.db.dao.CourseDao
import hu.unimiskolc.iit.framework.db.mapper.CourseMapper

class RoomCourseDataSource (private val dao: CourseDao, private val mapper: CourseMapper):
    CourseDataSource {
    override suspend fun add(course: Course) = dao.add(mapper.mapToEntity(course))

    override suspend fun update(course: Course) = dao.update(mapper.mapToEntity(course))

    override suspend fun remove(course: Course) = dao.remove(mapper.mapToEntity(course))

    override suspend fun fetchById(id: Int): Course? = dao.fetchById(id)?.let { mapper.mapFromEntity(it) }

    override suspend fun fetchAll(): List<Course> = dao.fetchAll().map { mapper.mapFromEntity(it) }


}