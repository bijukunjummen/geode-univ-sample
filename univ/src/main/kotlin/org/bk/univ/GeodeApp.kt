package org.bk.univ

import org.apache.geode.cache.GemFireCache
import org.bk.univ.listener.TeacherRegionListener
import org.bk.univ.model.TeacherEntity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.listener.ContinuousQueryDefinition
import org.springframework.data.gemfire.listener.ContinuousQueryListenerContainer
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.web.config.EnableSpringDataWebSupport


@SpringBootApplication
@EnableSpringDataWebSupport
class GeodeApp 


@Configuration
@ClientCacheApplication(subscriptionEnabled = true)
@EnableEntityDefinedRegions(basePackageClasses = [TeacherEntity::class])
@EnableGemfireRepositories
@EnablePdx
class GemfireConfig {

    @Bean
    fun continuousQueryListenerContainer(gemfireCache: GemFireCache): ContinuousQueryListenerContainer {
        val container = ContinuousQueryListenerContainer()
        container.setCache(gemfireCache)
        container.setQueryListeners(setOf(newTeachersQuery()))
        return container
    }
    
    @Bean
    fun teacherRegionListener()  = TeacherRegionListener()
    
    @Bean
    fun newTeachersQuery(): ContinuousQueryDefinition {

        val query = String.format("SELECT * FROM /teachers")

        return ContinuousQueryDefinition("new-teachers", query,
                teacherRegionListener(), true)
    }
    
}

fun main(args: Array<String>) {
    runApplication<GeodeApp>(*args)
}

