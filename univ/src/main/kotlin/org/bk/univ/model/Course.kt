package org.bk.univ.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Course(
        @JsonProperty("course-code") val courseCode: String,
        val name: String,
        val description: String
) {
    var teacher: Teacher? = null
    
    constructor(courseCode: String, name: String, description: String, teacher: Teacher): this(courseCode, name, description) {
        this.teacher = teacher
    }
    
    fun toEntity(): CourseEntity = CourseEntity(courseCode, name, description, teacher?.teacherId)
    
    companion object {
        fun fromEntity(courseEntity: CourseEntity): Course = 
                Course(courseEntity.courseCode, courseEntity.name, courseEntity.description)

        fun fromEntity(courseEntity: CourseEntity, teacherEntity: TeacherEntity): Course =
                Course(courseEntity.courseCode, courseEntity.name, courseEntity.description, Teacher.fromEntity(teacherEntity))
    }
}
