package org.bk.univ.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@JsonTest
class TeacherMarshalTest {

    @Autowired
    lateinit var jsonTester: JacksonTester<Teacher>

    @Test
    fun testMarshalToJson() {
        val teacher = Teacher("teacher-id", "name1", "department1", 25)
        println(jsonTester.write(teacher))
        assertThat(jsonTester.write(teacher)).isEqualToJson("{\n  \"teacherId\": \"teacher-id\",\n  \"name\": \"name1\",\n  \"department\": \"department1\"\n}")

        assertThat(jsonTester
                .parseObject("{\n  \"teacherId\": \"teacher-id\",\n  \"name\": \"name1\",\n  \"department\": \"department1\",\n  \"joinedDate\":\"2018-06-25T14:30:39.949\",\n  \"retirementDate\":\"2018-06-25T14:30:39.95\"  \n}"))
                .hasFieldOrPropertyWithValue("teacherId", "teacher-id")
                .hasFieldOrPropertyWithValue("name", "name1")
    }
}