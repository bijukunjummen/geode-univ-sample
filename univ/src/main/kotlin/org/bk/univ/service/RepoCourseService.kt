package org.bk.univ.service

import io.vavr.control.Try
import org.bk.univ.DateTimeUtils
import org.bk.univ.exceptions.EntityNotFoundException
import org.bk.univ.model.Course
import org.bk.univ.model.CourseEntity
import org.bk.univ.model.Teacher
import org.bk.univ.repo.CourseRepo
import org.bk.univ.repo.TeacherRepo
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class RepoCourseService(val courseRepo: CourseRepo, val teacherRepo: TeacherRepo) : CourseService {
    override fun findById(courseCode: String): Optional<Course> {
        return courseRepo.findById(courseCode).map { Course(it.courseCode, it.name, it.description) }
    }

    override fun findCourses(): List<Course> {
        return courseRepo.findAll().map {
            Course(it.courseCode, it.name, it.description)
        }
    }

    override fun findCoursesByTeacher(teacherId: String): List<Course> {
        return courseRepo.findByTeacherId(teacherId).map { Course(it.courseCode, it.name, it.description) }
    }

    override fun saveCourse(course: Course): Course {
        val courseEntity = CourseEntity(course.courseCode, course.name, course.description, course.teacher?.teacherId)
        return courseRepo.save(courseEntity).let {
            Course(it.courseCode, it.name, it.description)
        }
    }

    override fun updateCourse(course: Course): Optional<Course> {
        return courseRepo.findById(course.courseCode).map { courseEntity ->
            courseRepo.save(
                    CourseEntity(courseEntity.courseCode, course.name, course.description, course.teacher?.teacherId)
            )
        }.map {
            val savedCourse = Course(it.courseCode, it.name, it.description)
            if (it.teacherId != null) {
                teacherRepo.findById(it.teacherId)
                        .map { Teacher(it.id, it.name, it.department, it.age, it.joinedDate?.let { DateTimeUtils.toDatetime(it) }, it.retirementDate?.let { DateTimeUtils.toDatetime(it) }) }
                        .ifPresent {
                            savedCourse.teacher = it
                        }

            }
            savedCourse
        }
    }

    override fun deleteCourse(courseId: String): Try<Boolean> {
        return courseRepo.findById(courseId)
                .map { course ->
                    courseRepo.delete(course)
                }.map { Try.ofSupplier { true } }.orElse(Try.failure(EntityNotFoundException("Course with id: $courseId, not found")))

    }
}