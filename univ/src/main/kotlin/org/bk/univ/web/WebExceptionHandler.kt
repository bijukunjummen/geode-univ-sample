package org.bk.univ.web

import org.bk.univ.exceptions.EntityNotFoundException
import org.springframework.hateoas.VndErrors
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class WebExceptionHandler:ResponseEntityExceptionHandler() {

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(ex: EntityNotFoundException, request: WebRequest): ResponseEntity<Any> {
        val vndErrors = VndErrors("Error", ex.message) 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(vndErrors)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val vndErrors = VndErrors("Error", ex.message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(vndErrors)
    }
}