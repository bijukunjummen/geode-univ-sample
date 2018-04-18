package org.bk.univ.service

import org.bk.univ.model.Teacher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TeacherService {
    fun findTeachers(pageable: Pageable): Page<Teacher>
}
