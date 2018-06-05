package org.bk.univ.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@JsonTest
class TeacherMarshalTest {

    @Autowired
    lateinit var jacksonTester: JacksonTester<Teacher>

    @Test
    fun testMarshalToJson() {
        val teacher = Teacher("teacher-id", "name1", "department1", 25, LocalDateTime.now(), LocalDateTime.now())
        assertThat(jacksonTester.write(teacher)).isEqualToJson("{\n  \"teacherId\": \"teacher-id\",\n  \"name\": \"name1\",\n  \"department\": \"department1\"\n}")

        assertThat(jacksonTester
                .parseObject("{\n  \"teacherId\": \"teacher-id\",\n  \"name\": \"name1\",\n  \"department\": \"department1\"\n}"))
                .isEqualTo(teacher)
    }
}