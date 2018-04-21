package org.bk.univ.service

import org.bk.univ.model.Teacher
import org.bk.univ.model.TeacherEntity
import org.bk.univ.repo.TeacherRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class RepoTeacherService(val teacherRepo: TeacherRepo) : TeacherService {
    override fun update(teacher: Teacher): Optional<Teacher> {
        val teacherId = teacher.teacherId
        if (teacherId != null) {
            return teacherRepo.findById(teacherId).map { Teacher(it.id, it.name, it.department) }
        } else {
            return Optional.empty()
        }
    }

    override fun findTeacher(teacherId: String): Optional<Teacher> =
            teacherRepo.findById(teacherId).map { entity -> Teacher(entity.id, entity.name, entity.department) }

    override fun save(teacher: Teacher): Teacher {
        teacher.teacherId = UUID.randomUUID().toString()
        val savedEntity = teacherRepo.save(TeacherEntity(teacher.teacherId, teacher.name, teacher.department))
        return Teacher(savedEntity.id, savedEntity.name, savedEntity.department)
    }
    
    

    override fun findTeachers(page: Pageable): Page<Teacher> {
        val teacherEntities = teacherRepo.findAll()
        return PageImpl(teacherEntities.map { entity -> Teacher(entity.id, entity.name, entity.department) })
    }

}