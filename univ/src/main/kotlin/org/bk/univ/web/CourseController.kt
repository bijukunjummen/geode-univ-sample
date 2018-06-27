package org.bk.univ.web

import org.bk.univ.model.Course
import org.bk.univ.model.CourseCreateDto
import org.bk.univ.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/courses")
class CourseController(private val courseService: CourseService) {

    @RequestMapping
    fun getCourses(): List<Course> = courseService.findCourses()
    
    @RequestMapping(method = [RequestMethod.POST])
    fun createCourse(@RequestBody course: CourseCreateDto): ResponseEntity<Course> {
        val savedCourse = courseService.saveCourse(course)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse)
    }

    @RequestMapping(method = [RequestMethod.PUT])
    fun updateCourse(@RequestBody course: CourseCreateDto): ResponseEntity<Course> {
        val updatedCourseOpt = courseService.updateCourse(course)
        return updatedCourseOpt
                .map { ResponseEntity.status(HttpStatus.ACCEPTED).body(it) }
                .orElse(ResponseEntity.notFound().build())
    }
    
    @RequestMapping("/{courseCode}")
    fun getACourse(@PathVariable("courseCode") courseCode: String): ResponseEntity<Course> {
        return courseService.findById(courseCode)
                .map { ResponseEntity.status(HttpStatus.OK).body(it) }
                .orElse(ResponseEntity.notFound().build())
    }

    @RequestMapping(method = [RequestMethod.DELETE], value=["/{courseCode}"])
    fun deleteACourse(@PathVariable("courseCode") courseCode: String): ResponseEntity<*> {
        return courseService.deleteCourse(courseCode)
                .map { _ -> ResponseEntity.accepted().body("") }
                .get()
    }
}
