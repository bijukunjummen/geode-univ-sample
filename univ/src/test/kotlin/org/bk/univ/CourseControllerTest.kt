package org.bk.univ

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.vavr.control.Try
import org.bk.univ.model.Course
import org.bk.univ.service.CourseService
import org.bk.univ.web.CourseController
import org.bk.univ.web.CoursesByTeacherController
import org.bk.univ.web.WebExceptionHandler
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Optional


@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [CourseController::class, WebExceptionHandler::class, CoursesByTeacherController::class])
class CourseControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var courseService: CourseService

    @Test
    @DisplayName("Get a list of courses")
    fun getAListOfCourses() {
        whenever(courseService.findCourses())
                .thenReturn(listOf(
                        Course("id-1", "course-name", "some-desc"),
                        Course("id-2", "course-name", "some-desc")
                )
                )
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk)
                .andExpect(content()
                        .json("[\n  {\"course-code\": \"id-1\", \"name\": \"course-name\", \"description\": \"some-desc\"},\n  {\"course-code\": \"id-2\", \"name\": \"course-name\", \"description\": \"some-desc\"}\n]"))
    }

    @Test
    @DisplayName("Create a course without any errors")
    fun createCourseSuccessfully() {
        whenever(courseService.saveCourse(any())).thenAnswer { invocation ->
            invocation.arguments[0]
        }
        mockMvc.perform(post("/courses")
                .contentType("application/json")
                .content("{\"course-code\": \"id-1\", \"name\": \"course-name\", \"description\": \"some-desc\"}")
        ).andExpect(status().isCreated)
                .andExpect(content().json("{\"course-code\": \"id-1\", \"name\": \"course-name\", \"description\": \"some-desc\"}"))
    }

    @Test
    @DisplayName("Create a course with errors")
    fun createWithError() {
        whenever(courseService.saveCourse(any())).thenThrow(RuntimeException("Something went wrong!"))

        mockMvc.perform(post("/courses")
                .contentType("application/json")
                .content("{\"course-code\": \"id-1\", \"name\": \"course-name\", \"description\": \"some-desc\"}")
        ).andExpect(status().isInternalServerError)
                .andExpect(content().json("[{\"logref\":\"Error\",\"message\":\"Something went wrong!\",\"links\":[]}]"))
    }

    @Test
    @DisplayName("Get a Course")
    fun getACourse() {
        whenever(courseService.findById("id-1"))
                .thenReturn(Optional.of(Course("id-1", "course-name", "some-desc")))

        mockMvc.perform(get("/courses/id-1"))
                .andExpect(status().isOk)
                .andExpect(content().json("{\"course-code\": \"id-1\", \"name\": \"course-name\", \"description\": \"some-desc\"}"))
    }

    @Test
    @DisplayName("Delete a Course")
    fun deleteCourse() {
        whenever(courseService.deleteCourse("id-1")).thenReturn(Try.success(true))

        mockMvc.perform(delete("/courses/id-1"))
                .andExpect(status().isAccepted)
    }

    @Test
    @DisplayName("Get Courses By Teacher")
    fun getCoursesByTeacher() {
        whenever(courseService.findCoursesByTeacher("id-1"))
                .thenReturn(listOf(
                        Course("id-1", "course-name", "some-desc"),
                        Course("id-2", "course-name", "some-desc")
                ))
        
        mockMvc.perform(get("/teachers/id-1/courses"))
                .andExpect(status().isOk)
                .andExpect(content().json("[\n  {\"course-code\": \"id-1\", \"name\": \"course-name\", \"description\": \"some-desc\"},\n  {\"course-code\": \"id-2\", \"name\": \"course-name\", \"description\": \"some-desc\"}\n]"))
    }
}