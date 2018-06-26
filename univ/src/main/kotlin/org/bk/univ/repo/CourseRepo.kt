package org.bk.univ.repo

import org.bk.univ.model.CourseEntity
import org.springframework.data.repository.CrudRepository

interface CourseRepo: CrudRepository<CourseEntity, String>