package org.bk.univ.web

import org.bk.univ.model.Teacher
import org.bk.univ.service.TeacherService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/teachers")
class TeacherController(val teacherService: TeacherService) {

    private val logger: Logger = LoggerFactory.getLogger(TeacherController::class.java)
    
    @RequestMapping(method = [RequestMethod.GET])
    fun listTeachers(page: Pageable): Page<Teacher> = 
            teacherService.findTeachers(page)
    
    @RequestMapping(method = [RequestMethod.POST])
    fun saveTeacher(@RequestBody teacher: Teacher): ResponseEntity<Teacher>  {
        val savedTeacher = teacherService.save(teacher)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeacher)
    }

    @RequestMapping(value = "/{teacherId}", method = [RequestMethod.PUT])
    fun updateTeacher(@PathVariable("teacherId") teacherId: String, @RequestBody teacher: Teacher): ResponseEntity<Teacher>  {
        val toUpdate = teacher.copy(teacherId = teacherId)
        val savedTeacher = teacherService.update(toUpdate)
        return savedTeacher.map { ResponseEntity.status(HttpStatus.ACCEPTED).body(it) }.orElse(ResponseEntity.badRequest().build())
    }
 
    @RequestMapping("/{teacherId}")
    fun getTeacher(@PathVariable("teacherId") teacherId: String): ResponseEntity<Teacher> = 
        teacherService.findTeacher(teacherId)
                .map { teacher -> ResponseEntity.ok(teacher) }
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build())

    @RequestMapping(value="/{teacherId}", method = [RequestMethod.DELETE])
    fun deleteTeacher(@PathVariable("teacherId") teacherId: String): ResponseEntity<*> =
            teacherService.deleteTeacher(teacherId)
                    .map { _ -> ResponseEntity.accepted().body("") }
                    .get()
}
