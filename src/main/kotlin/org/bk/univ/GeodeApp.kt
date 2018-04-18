package org.bk.univ

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport

@SpringBootApplication
@EnableSpringDataWebSupport
class GeodeApp

fun main(args: Array<String>) {
    runApplication<GeodeApp>(*args)
}
