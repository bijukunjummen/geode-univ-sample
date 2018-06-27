package org.bk.univ.service

import io.vavr.control.Try
import org.bk.univ.exceptions.EntityNotFoundException
import org.bk.univ.model.Teacher
import org.bk.univ.repo.TeacherRepo
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID

@Service
class RepoTeacherService(val teacherRepo: TeacherRepo) : TeacherService {
    override fun deleteTeacher(teacherid: String): Try<Boolean> =
            teacherRepo.findById(teacherid)
                    .map { teacher ->
                        teacherRepo.delete(teacher)
                    }.map { Try.ofSupplier { true } }.orElse(Try.failure(EntityNotFoundException("Teacher with id: $teacherid, not found")))


    override fun update(teacher: Teacher): Optional<Teacher> {
        val teacherId = teacher.teacherId
        return if (teacherId != null) {
            teacherRepo.findById(teacherId).map {
                val teacherToSave = teacher.copy(teacherId = it.id)
                teacherRepo.save(teacherToSave.toEntity())
            }.map {
                Teacher.fromEntity(it)
            }
        } else {
            Optional.empty()
        }
    }

    override fun findTeacher(teacherId: String): Optional<Teacher> =
            teacherRepo.findById(teacherId).map { Teacher.fromEntity(it) }

    override fun save(teacher: Teacher): Teacher {
        val teacherToSave = teacher.copy(teacherId = teacher.teacherId ?: UUID.randomUUID().toString())
        val savedEntity = teacherRepo.save(teacherToSave.toEntity())
        return Teacher.fromEntity(savedEntity)
    }

    override fun findTeachers(): List<Teacher> {
        val teacherEntities = teacherRepo.findAll()
        return (teacherEntities.map { entity ->
            Teacher.fromEntity(entity)
        }).toList()
    }
}