package org.bk.univ.config

import org.bk.univ.model.Teacher
import org.bk.univ.service.TeacherService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GfDataPopulatorConfig {
    @Bean
    fun dataPopulator(teacherService: TeacherService): CommandLineRunner = CommandLineRunner { args ->
        for (i in 1 .. 10) {
            val teacherId = "id-$i"
            if (!teacherService.findTeacher(teacherId).isPresent) {
                teacherService.save(Teacher("id-$i", "name-$i", "department-$i"))
            }
        }
    }
}