package org.bk.univ.web

import org.bk.univ.model.Course
import org.bk.univ.service.CourseService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/teachers")
class CoursesByTeacherController(private val courseService: CourseService) {


    @RequestMapping(value = ["/{teacherId}/courses"], method = [RequestMethod.GET])
    fun getCoursesByTeacher(@PathVariable("teacherId") teacherId: String): List<Course> {
        return courseService.findCoursesByTeacher(teacherId)
    }
}