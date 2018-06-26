package org.bk.univ.service

import io.vavr.control.Try
import org.bk.univ.DateTimeUtils
import org.bk.univ.exceptions.EntityNotFoundException
import org.bk.univ.model.Teacher
import org.bk.univ.model.TeacherEntity
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
                teacherRepo.save(
                        TeacherEntity(
                                teacher.teacherId,
                                teacher.name,
                                teacher.department,
                                teacher.age,
                                teacher.joinedDate?.let { DateTimeUtils.toDate(it) },
                                teacher.retirementDate?.let { DateTimeUtils.toDate(it) }
                        ))
            }.map { entity ->
                Teacher(
                        entity.id,
                        entity.name,
                        entity.department,
                        entity.age,
                        entity.joinedDate?.let { DateTimeUtils.toDatetime(it) },
                        entity.retirementDate?.let { DateTimeUtils.toDatetime(it) }
                )
            }
        } else {
            Optional.empty()
        }
    }

    override fun findTeacher(teacherId: String): Optional<Teacher> =
            teacherRepo.findById(teacherId).map { entity ->
                Teacher(
                        entity.id,
                        entity.name,
                        entity.department,
                        entity.age,
                        entity.joinedDate?.let { DateTimeUtils.toDatetime(it) },
                        entity.retirementDate?.let { DateTimeUtils.toDatetime(it) }
                )
            }

    override fun save(teacher: Teacher): Teacher {
        teacher.teacherId = teacher.teacherId ?: UUID.randomUUID().toString()
        val savedEntity = teacherRepo.save(
                TeacherEntity(teacher.teacherId,
                        teacher.name,
                        teacher.department,
                        teacher.age,
                        teacher.joinedDate?.let { DateTimeUtils.toDate(it) },
                        teacher.retirementDate?.let { DateTimeUtils.toDate(it) }
                ))
        return Teacher(savedEntity.id,
                savedEntity.name,
                savedEntity.department,
                savedEntity.age,
                savedEntity.joinedDate?.let { DateTimeUtils.toDatetime(it) },
                savedEntity.retirementDate?.let { DateTimeUtils.toDatetime(it) }
        )
    }

    override fun findTeachers(): List<Teacher> {
        val teacherEntities = teacherRepo.findAll()
        return (teacherEntities.map { entity ->
            Teacher(entity.id,
                    entity.name,
                    entity.department,
                    entity.age,
                    entity.joinedDate?.let { DateTimeUtils.toDatetime(it) },
                    entity.retirementDate?.let { DateTimeUtils.toDatetime(it) }
            )
        }).toList()
    }
}