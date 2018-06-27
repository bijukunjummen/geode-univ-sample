package org.bk.univ.service

import io.vavr.control.Try
import org.bk.univ.DateTimeUtils
import org.bk.univ.exceptions.EntityNotFoundException
import org.bk.univ.model.Course
import org.bk.univ.model.CourseCreateDto
import org.bk.univ.model.Teacher
import org.bk.univ.repo.CourseRepo
import org.bk.univ.repo.TeacherRepo
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class RepoCourseService(val courseRepo: CourseRepo, val teacherRepo: TeacherRepo) : CourseService {
    override fun findById(courseCode: String): Optional<Course> {
        return courseRepo.findById(courseCode).map { entity ->
            val teacherEntityOpt = Optional.ofNullable(entity.teacherId).flatMap { teacherId -> teacherRepo.findById(teacherId) }
            teacherEntityOpt.map { teacherEntity ->
                Course.fromEntity(entity, teacherEntity)
            }.orElse(Course.fromEntity(entity))
        }
    }

    override fun findCourses(): List<Course> {
        return courseRepo.findAll().map {
            Course.fromEntity(it)
        }
    }

    override fun findCoursesByTeacher(teacherId: String): List<Course> {
        return courseRepo.findByTeacherId(teacherId).map { Course.fromEntity(it) }
    }

    override fun saveCourse(course: CourseCreateDto): Course {
        val courseEntity = course.toEntity()
        return courseRepo.save(courseEntity).let { entity ->
            val teacherEntityOpt = Optional.ofNullable(entity.teacherId).flatMap { teacherId -> teacherRepo.findById(teacherId) }
            teacherEntityOpt.map { teacherEntity ->
                Course.fromEntity(entity, teacherEntity)
            }.orElse(Course.fromEntity(entity))
        }
    }

    override fun updateCourse(course: CourseCreateDto): Optional<Course> {

        return courseRepo.findById(course.courseCode).map { courseEntity ->
            val courseToSave = course.copy(courseCode = courseEntity.courseCode)
            courseRepo.save(
                    courseToSave.toEntity()
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