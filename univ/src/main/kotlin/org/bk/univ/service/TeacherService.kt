package org.bk.univ.service

import io.vavr.control.Try
import org.bk.univ.model.Teacher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface TeacherService {
    fun findTeachers(pageable: Pageable): Page<Teacher>
    fun findTeacher(teacherId: String): Optional<Teacher>
    fun save(teacher: Teacher): Teacher
    fun update(teacher: Teacher): Optional<Teacher>
    fun deleteTeacher(teacherid: String): Try<Boolean> 
}
