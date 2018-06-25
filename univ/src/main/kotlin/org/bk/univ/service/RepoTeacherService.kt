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
                        it.age,
                        Optional.ofNullable(it.joinedDate).map { DateTimeUtils.toDatetime(it) },
                        Optional.ofNullable(it.retirementDate).map { DateTimeUtils.toDatetime(it) }
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
                        Optional.ofNullable(entity.joinedDate).map { DateTimeUtils.toDatetime(it) },
                        Optional.ofNullable(entity.retirementDate).map{DateTimeUtils.toDatetime(it)}
                )
            }

    override fun save(teacher: Teacher): Teacher {
        teacher.teacherId = UUID.randomUUID().toString()
        val savedEntity = teacherRepo.save(
                TeacherEntity(teacher.teacherId,
                        teacher.name,
                        teacher.department,
                        teacher.age,
                        teacher.joinedDate.map { DateTimeUtils.toDate(it) }.orElse(null),
                        teacher.retirementDate.map {DateTimeUtils.toDate(it)}.orElse(null)
                ))
        return Teacher(savedEntity.id,
                savedEntity.name,
                savedEntity.department,
                savedEntity.age,
                Optional.ofNullable(savedEntity.joinedDate).map { DateTimeUtils.toDatetime(it) },
                Optional.ofNullable(savedEntity.retirementDate).map{DateTimeUtils.toDatetime(it)}
        )
    }

    override fun findTeachers(page: Pageable): Page<Teacher> {
        val teacherEntities = teacherRepo.findAll()
        return PageImpl(teacherEntities.map { entity -> 
            Teacher(entity.id, 
                    entity.name, 
                    entity.department, 
                    entity.age,
                    Optional.ofNullable(entity.joinedDate).map { DateTimeUtils.toDatetime(it) },
                    Optional.ofNullable(entity.retirementDate).map { DateTimeUtils.toDatetime(it) }
            ) })
    }
}