package org.bk.univ.model

class Course(val courseCode: String, val name: String, val description: String) {
    var teacher: Teacher? = null
    
    constructor(courseCode: String, name: String, description: String, teacher: Teacher): this(courseCode, name, description) {
        this.teacher = teacher
    }
}
