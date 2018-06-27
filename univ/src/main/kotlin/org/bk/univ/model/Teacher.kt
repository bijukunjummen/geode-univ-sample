package org.bk.univ.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.bk.univ.DateTimeUtils
import java.time.LocalDateTime

data class Teacher (
        @JsonProperty("teacher-id") var teacherId: String? = null,
        var name: String = "",
        var department: String = "",
        var age: Int = 0,
        @JsonProperty("joined-date") var joinedDate: LocalDateTime? =  null,
        @JsonProperty("retirement-date") var retirementDate: LocalDateTime? = null 
) {
    fun toEntity(): TeacherEntity = TeacherEntity(
            teacherId,
            name,
            department,
            age,
            joinedDate?.let { DateTimeUtils.toDate(it) },
            retirementDate?.let { DateTimeUtils.toDate(it)}
    )
    
    companion object {
        fun fromEntity(entity: TeacherEntity) = Teacher(
                entity.id,
                entity.name,
                entity.department,
                entity.age,
                entity.joinedDate?.let { DateTimeUtils.toDatetime(it) },
                entity.retirementDate?.let { DateTimeUtils.toDatetime(it) }
        )
    }
}
