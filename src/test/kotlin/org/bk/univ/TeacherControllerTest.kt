package org.bk.univ

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.bk.univ.model.Teacher
import org.bk.univ.service.TeacherService
import org.bk.univ.web.TeacherController
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@RunWith(SpringRunner::class)
@WebMvcTest(TeacherController::class)
class TeacherControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var teacherService: TeacherService

    @Test
    fun getAListOfTeachers() {
        val page1: Page<Teacher> = PageImpl(listOf(
                Teacher(1, "name1", "dep1"),
                Teacher(2, "name2", "dep2")
        ), PageRequest.of(0, 2), 4)

        val page2: Page<Teacher> = PageImpl(listOf(
                Teacher(10, "name10", "dep10"),
                Teacher(20, "name20", "dep20")
        ), PageRequest.of(1, 2), 4)

        whenever(teacherService.findTeachers(any())).thenReturn(page1)
        
        whenever(teacherService.findTeachers(PageRequest.of(1, 2))).thenReturn(page2)

        mockMvc.perform(get("/teachers").param("page", "0").param("size", "2"))
                .andExpect(status().isOk)
                .andExpect(content().json("""
                    | {
                    |   "content": [
                    |       {"teacherId":1, "name":"name1", "department":"dep1"},
                    |       {"teacherId":2, "name":"name2", "department":"dep2"}
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
                    |       {"teacherId":10, "name":"name10", "department":"dep10"},
                    |       {"teacherId":20, "name":"name20", "department":"dep20"}
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
}