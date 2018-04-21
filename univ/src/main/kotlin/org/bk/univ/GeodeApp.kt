package org.bk.univ

import org.apache.geode.cache.client.ClientRegionShortcut
import org.bk.univ.model.Teacher
import org.bk.univ.service.TeacherService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.web.config.EnableSpringDataWebSupport

@SpringBootApplication
@EnableSpringDataWebSupport
class GeodeApp 


@Configuration
@ClientCacheApplication
@EnableEntityDefinedRegions(basePackages = ["org.bk.univ.model"],
        clientRegionShortcut = ClientRegionShortcut.PROXY)
@EnableGemfireRepositories
@EnablePdx
class GemfireConfig

fun main(args: Array<String>) {
    runApplication<GeodeApp>(*args)
}

