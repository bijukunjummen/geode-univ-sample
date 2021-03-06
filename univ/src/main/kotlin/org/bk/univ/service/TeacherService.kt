package org.bk.univ.service

import io.vavr.control.Try
import org.bk.univ.model.Teacher
import java.util.Optional

interface TeacherService {
    fun findTeachers(): List<Teacher>
    fun findTeacher(teacherId: String): Optional<Teacher>
    fun save(teacher: Teacher): Teacher
    fun update(teacher: Teacher): Optional<Teacher>
    fun deleteTeacher(teacherid: String): Try<Boolean> 
}
