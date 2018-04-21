package org.bk.univ.model

class Course(val courseId: String, val courseCode: String, val courseName: String) {
    var teacher: Teacher? = null
}
