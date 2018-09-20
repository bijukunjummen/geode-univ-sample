package org.bk.univ.config

import org.apache.geode.cache.Region
import org.bk.univ.model.CourseCreateDto
import org.bk.univ.model.StudentEntity
import org.bk.univ.model.Teacher
import org.bk.univ.service.CourseService
import org.bk.univ.service.TeacherService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class GfDataPopulatorConfig {
    @Bean
    fun dataPopulator(teacherService: TeacherService, courseService: CourseService, studentRegion: Region<String, StudentEntity>): CommandLineRunner = CommandLineRunner { _ ->
        for (i in 1..100) {
            val teacherId = "id-$i"
            if (!teacherService.findTeacher(teacherId).isPresent) {
                val joinYear = 2000 + (i % 18)
                val retirementYear = joinYear + 30
                val joinedDate = LocalDateTime.parse("$joinYear-01-01T00:00:00")
                val retirementDate = LocalDateTime.parse("$retirementYear-01-01T00:00:00")
                val savedTeacher = teacherService.save(Teacher("id-$i", "name-$i", "department-$i", 20 + (i % 20),
                        joinedDate, retirementDate))
                val courseCode = "code-id-$i"
                if (!courseService.findById(courseCode).isPresent) {
                    courseService.saveCourse(CourseCreateDto("code-id-$i", "course-name-$i", "course-description-$i", savedTeacher.teacherId))
                }
            }
        }

        studentRegion.put("1", StudentEntity("1", "student-1"))
        studentRegion.put("2", StudentEntity("2", "student-2"))
        studentRegion.put("3", StudentEntity("3", "student-3"))


    }
}