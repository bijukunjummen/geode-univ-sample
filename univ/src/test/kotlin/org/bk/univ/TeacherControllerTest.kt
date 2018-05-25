package org.bk.univ

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.vavr.control.Try
import org.bk.univ.exceptions.EntityNotFoundException
import org.bk.univ.model.Teacher
import org.bk.univ.service.TeacherService
import org.bk.univ.web.TeacherController
import org.bk.univ.web.WebExceptionHandler
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Optional


@RunWith(SpringRunner::class)
@WebMvcTest(controllers = [TeacherController::class, WebExceptionHandler::class])
class TeacherControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var teacherService: TeacherService

    @Test
    fun getAListOfTeachers() {
        val page1: Page<Teacher> = PageImpl(listOf(
                Teacher("1", "name1", "dep1"),
                Teacher("2", "name2", "dep2")
        ), PageRequest.of(0, 2), 4)

        val page2: Page<Teacher> = PageImpl(listOf(
                Teacher("10", "name10", "dep10"),
                Teacher("20", "name20", "dep20")
        ), PageRequest.of(1, 2), 4)

        whenever(teacherService.findTeachers(any())).thenReturn(page1)

        whenever(teacherService.findTeachers(PageRequest.of(1, 2))).thenReturn(page2)

        mockMvc.perform(get("/teachers").param("page", "0").param("size", "2"))
                .andExpect(status().isOk)
                .andExpect(content().json("""
                    | {
                    |   "content": [
                    |       {"teacherId":"1", "name":"name1", "department":"dep1"},
                    |       {"teacherId":"2", "name":"name2", "department":"dep2"}
                    |   ],
                    |   "last":false,
                    |   "totalPages":2,
                    |   "totalElements":4,
                    |   "size":2,
                    |   "number":0,
                    |   "numberOfElements":2,
                    |   "first":true
                    | }
                """.trimMargin(), false))

        mockMvc.perform(get("/teachers").param("page", "1").param("size", "2"))
                .andExpect(status().isOk)
                .andExpect(content().json("""
                    | {
                    |   "content": [
                    |       {"teacherId":"10", "name":"name10", "department":"dep10"},
                    |       {"teacherId":"20", "name":"name20", "department":"dep20"}
                    |   ],
                    |   "last":true,
                    |   "totalPages":2,
                    |   "totalElements":4,
                    |   "size":2,
                    |   "number":1,
                    |   "numberOfElements":2,
                    |   "first":false
                    | }
                """.trimMargin(), false))

    }

    @Test
    fun saveATeacher() {
        whenever(teacherService.save(any())).thenAnswer({invocation -> 
            val teacher:Teacher = invocation.getArgument(0)
            Teacher("teacher-id", teacher.name, teacher.department)
        })
        mockMvc.perform(post("/teachers").contentType(MediaType.APPLICATION_JSON)
                .content("{\n  \"name\": \"Teacher1\",\n  \"department\": \"department1\"\n}"))
                .andExpect(status().isCreated)
                .andExpect(content().json("{\n  \"teacherId\": \"teacher-id\",\n  \"name\": \"Teacher1\",\n  \"department\": \"department1\"\n}"))
    }

    @Test
    fun updateATeacher() {
        whenever(teacherService.update(any())).thenAnswer({invocation ->
            val teacher:Teacher = invocation.getArgument(0)
            Optional.of(Teacher(teacher.teacherId, teacher.name, teacher.department))
        })
        mockMvc.perform(put("/teachers/someid").contentType(MediaType.APPLICATION_JSON)
                .content("{\n  \"name\": \"Teacher1\",\n  \"department\": \"department1\"\n}"))
                .andExpect(status().isAccepted)
                .andExpect(content().json("{\n  \"teacherId\": \"someid\",\n  \"name\": \"Teacher1\",\n  \"department\": \"department1\"\n}"))
    }

    @Test
    fun findATeacher() {
        whenever(teacherService.findTeacher("teacher-id")).thenReturn(Optional.of(Teacher("teacher-id", "Teacher1", "department1")))
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