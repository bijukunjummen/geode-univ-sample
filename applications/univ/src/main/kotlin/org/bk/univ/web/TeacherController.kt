package org.bk.univ.web

import org.bk.univ.model.Teacher
import org.bk.univ.service.TeacherService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/teachers")
class TeacherController(val teacherService: TeacherService) {

    @RequestMapping(method = [RequestMethod.GET])
    fun listTeachers(page: Pageable): Page<Teacher> = 
            teacherService.findTeachers(page)
}
