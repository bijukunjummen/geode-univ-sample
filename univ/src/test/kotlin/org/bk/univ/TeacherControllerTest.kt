package org.bk.univ

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.vavr.control.Try
import org.bk.univ.exceptions.EntityNotFoundException
import org.bk.univ.model.Teacher
import org.bk.univ.service.TeacherService
import org.bk.univ.web.TeacherController
import org.bk.univ.web.WebExceptionHandler
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.Optional


@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [TeacherController::class, WebExceptionHandler::class])
class TeacherControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var teacherService: TeacherService

    @Test
    fun getAListOfTeachers() {
        val page1: List<Teacher> = listOf(
                Teacher("1", "name1", "dep1", 25, LocalDateTime.now(), LocalDateTime.now()),
                Teacher("2", "name2", "dep2", 25, LocalDateTime.now(), LocalDateTime.now())
        )


        whenever(teacherService.findTeachers()).thenReturn(page1)


        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk)
                .andExpect(content().json("""
                    |[
                    |       {"teacherId":"1", "name":"name1", "department":"dep1"},
                    |       {"teacherId":"2", "name":"name2", "department":"dep2"}
                    |]
                """.trimMargin(), false))

    }

    @Test
    fun saveATeacher() {
        whenever(teacherService.save(any())).thenAnswer { invocation ->
            val teacher: Teacher = invocation.getArgument(0)
            Teacher("teacher-id", teacher.name, teacher.department, teacher.age, teacher.joinedDate, teacher.retirementDate)
        }
        mockMvc.perform(post("/teachers").contentType(MediaType.APPLICATION_JSON)
                .content("{\n  \"name\": \"Teacher1\",\n  \"department\": \"department1\"\n}"))
                .andExpect(status().isCreated)
                .andExpect(content().json("{\n  \"teacherId\": \"teacher-id\",\n  \"name\": \"Teacher1\",\n  \"department\": \"department1\"\n}"))
    }

    @Test
    fun updateATeacher() {
        whenever(teacherService.update(any())).thenAnswer { invocation ->
            val teacher: Teacher = invocation.getArgument(0)
            Optional.of(Teacher(teacher.teacherId, teacher.name, teacher.department, teacher.age, teacher.joinedDate, teacher.retirementDate))
        }
        
        mockMvc.perform(put("/teachers/someid").contentType(MediaType.APPLICATION_JSON)
                .content("{\n  \"name\": \"Teacher1\",\n  \"department\": \"department1\"\n}"))
                .andExpect(status().isAccepted)
                .andExpect(content().json("{\n  \"teacherId\": \"someid\",\n  \"name\": \"Teacher1\",\n  \"department\": \"department1\"\n}"))
    }

    @Test
    fun findATeacher() {
        whenever(teacherService.findTeacher("teacher-id")).thenReturn(Optional.of(Teacher("teacher-id", "Teacher1", "department1", 25, LocalDateTime.now(), LocalDateTime.now())))
        mockMvc.perform(get("/teachers/teacher-id"))
                .andExpect(status().isOk)
                .andExpect(content().json("{\n  \"teacherId\": \"teacher-id\",\n  \"name\": \"Teacher1\",\n  \"department\": \"department1\"\n}"))
    }

    @Test
    fun deleteATeacher() {
        whenever(teacherService.deleteTeacher("teacher-id")).thenReturn(Try.ofSupplier { true });

        mockMvc.perform(delete("/teachers/teacher-id"))
                .andExpect(status().isAccepted)
    }

    @Test
    fun deleteANonExistentTeacher() {
        whenever(teacherService.deleteTeacher("teacher-id")).thenReturn(Try.failure(EntityNotFoundException("Not found!!")));

        mockMvc.perform(delete("/teachers/teacher-id"))
                .andDo { print() }
                .andExpect(status().is4xxClientError)
                .andExpect(content().json("[{\"logref\":\"Error\",\"message\":\"Not found!!\",\"links\":[]}]"))
    }
}