package org.bk.univ.service

import io.vavr.control.Try
import org.bk.univ.DateTimeUtils
import org.bk.univ.exceptions.EntityNotFoundException
import org.bk.univ.model.Teacher
import org.bk.univ.model.TeacherEntity
import org.bk.univ.repo.TeacherRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID

@Service
class RepoTeacherService(val teacherRepo: TeacherRepo) : TeacherService {
    override fun deleteTeacher(teacherid: String): Try<Boolean> =
            teacherRepo.findById(teacherid)
                    .map({ teacher ->
                        teacherRepo.delete(teacher)
                    }).map { Try.ofSupplier { true } }.orElse(Try.failure(EntityNotFoundException("Teacher with id: $teacherid, not found")))


    override fun update(teacher: Teacher): Optional<Teacher> {
        val teacherId = teacher.teacherId
        if (teacherId != null) {
            return teacherRepo.findById(teacherId).map {
                Teacher(
                        it.id,
                        it.name,
                        it.department,
                        teacher.age,
                        DateTimeUtils.toDatetime(it.joinedDate),
                        DateTimeUtils.toDatetime(it.retirementDate)
                )
            }
        } else {
            return Optional.empty()
        }
    }

    override fun findTeacher(teacherId: String): Optional<Teacher> =
            teacherRepo.findById(teacherId).map { entity ->
                Teacher(
                        entity.id,
                        entity.name,
                        entity.department,
                        entity.age,
                        DateTimeUtils.toDatetime(entity.joinedDate),
                        DateTimeUtils.toDatetime(entity.retirementDate))
            }

    override fun save(teacher: Teacher): Teacher {
        teacher.teacherId = UUID.randomUUID().toString()
        val savedEntity = teacherRepo.save(
                TeacherEntity(teacher.teacherId,
                        teacher.name,
                        teacher.department,
                        teacher.age,
                        DateTimeUtils.toDate(teacher.joinedDate),
                        DateTimeUtils.toDate(teacher.retirementDate)))
        return Teacher(savedEntity.id,
                savedEntity.name,
                savedEntity.department,
                savedEntity.age,
                DateTimeUtils.toDatetime(savedEntity.joinedDate),
                DateTimeUtils.toDatetime(savedEntity.retirementDate)
        )
    }

    override fun findTeachers(page: Pageable): Page<Teacher> {
        val teacherEntities = teacherRepo.findAll()
        return PageImpl(teacherEntities.map { entity -> 
            Teacher(entity.id, 
                    entity.name, 
                    entity.department, 
                    entity.age,
                    DateTimeUtils.toDatetime(entity.joinedDate),
                    DateTimeUtils.toDatetime(entity.retirementDate)
            ) })
    }
}