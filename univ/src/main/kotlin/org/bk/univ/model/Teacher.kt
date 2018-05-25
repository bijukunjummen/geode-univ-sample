package org.bk.univ.model

import java.time.LocalDateTime

data class Teacher (
        var teacherId: String? = null, 
        var name: String = "",
        var department: String = "",
        var age: Int = 0,
        var joinedDate: LocalDateTime,
        var retirementDate: LocalDateTime
) {
}
