package org.bk.univ

import org.apache.geode.cache.query.CqEvent
import org.bk.univ.model.Course
import org.bk.univ.model.CourseEntity
import org.bk.univ.model.Teacher
import org.bk.univ.model.TeacherEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux


@SpringBootApplication
class GeodeApp

@Component
class CqUsingFluxDemo : CommandLineRunner {

    val logger: Logger = LoggerFactory.getLogger(CqUsingFluxDemo::class.java)

    @Autowired
    lateinit var fluxTeachers: Flux<CqEvent>

    @Autowired
    lateinit var fluxCourses: Flux<CqEvent>

    override fun run(vararg args: String?) {
        fluxTeachers
                .map { cqEvent ->
                    val teacherEntity = cqEvent.newValue as TeacherEntity
                    Teacher.fromEntity(teacherEntity)
                }.subscribe { teacher ->
                    logger.info("From Flux: Teacher Event - {} ", teacher)
                }

        fluxCourses
                .map { cqEvent ->
                    val courseEntity = cqEvent.newValue as CourseEntity
                    Course.fromEntity(courseEntity)
                }.subscribe { course ->
                    logger.info("From Flux: Course Event - {}", course)
                }
    }

}

fun main(args: Array<String>) {
    runApplication<GeodeApp>(*args)
}

