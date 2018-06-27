package org.bk.univ.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CourseCreateDto (
        @JsonProperty("course-code") val courseCode: String,
        val name: String,
        val description: String,
        @JsonProperty("teacher-id") val teacherId: String?
)  {
    fun toEntity(): CourseEntity = CourseEntity(courseCode, name, description, teacherId)

    companion object {
        fun fromEntity(courseEntity: CourseEntity): CourseCreateDto =
                CourseCreateDto(courseEntity.courseCode, courseEntity.name, courseEntity.description, courseEntity.teacherId)
    }
}
