package org.bk.univ.service

import io.vavr.control.Try
import org.bk.univ.model.Course
import org.bk.univ.model.CourseCreateDto
import java.util.Optional

interface CourseService {
    fun findCourses(): List<Course>
    fun findById(courseCode: String): Optional<Course>
    fun findCoursesByTeacher(teacherId: String): List<Course>
    fun saveCourse(course: CourseCreateDto): Course
    fun updateCourse(course: CourseCreateDto): Optional<Course>
    fun deleteCourse(courseId: String): Try<Boolean>
}