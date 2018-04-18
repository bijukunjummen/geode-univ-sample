package org.bk.univ.model

class Teacher(val teacherId: Long, val name: String) {

    var department: String? = null

    constructor(teacherId: Long, name: String, department: String?) : this(teacherId, name) {
        this.department = department
    }

}
