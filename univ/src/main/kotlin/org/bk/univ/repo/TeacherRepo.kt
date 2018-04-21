package org.bk.univ.repo

import org.bk.univ.model.TeacherEntity
import org.springframework.data.repository.CrudRepository

interface TeacherRepo: CrudRepository<TeacherEntity, String>