package org.bk.univ.model

import java.time.LocalDateTime
import java.util.Optional

data class Teacher (
        var teacherId: String? = null,
        var name: String = "",
        var department: String = "",
        var age: Int = 0,
        var joinedDate: LocalDateTime? =  null,
        var retirementDate: LocalDateTime? = null 
) {
}
