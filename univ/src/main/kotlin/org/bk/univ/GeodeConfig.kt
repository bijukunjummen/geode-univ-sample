package org.bk.univ

import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientCache
import org.apache.geode.cache.query.CqEvent
import org.bk.univ.listener.CourseRegionListener
import org.bk.univ.listener.TeacherRegionListener
import org.bk.univ.listener.streamFrom
import org.bk.univ.model.CourseEntity
import org.bk.univ.model.TeacherEntity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.listener.ContinuousQueryDefinition
import org.springframework.data.gemfire.listener.ContinuousQueryListenerContainer
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import reactor.core.publisher.Flux

@Configuration
@ClientCacheApplication(subscriptionEnabled = true)
@EnableEntityDefinedRegions(basePackageClasses = [TeacherEntity::class, CourseEntity::class])
@EnableGemfireRepositories
@EnablePdx
class GeodeConfig {

    @Bean
    fun cqContainer(gemfireCache: GemFireCache): ContinuousQueryListenerContainer {
        val container = ContinuousQueryListenerContainer()
        container.setCache(gemfireCache)
        container.setQueryListeners(setOf(teachersQuery(), coursesQuery()))
        return container
    }

    @Bean
    fun teachersQuery(): ContinuousQueryDefinition {
        val query = String.format("SELECT * FROM /teachers")

        return ContinuousQueryDefinition("new-teachers", query,
                teacherRegionListener(), false)
    }

    @Bean
    fun teacherRegionListener() = TeacherRegionListener()


    @Bean
    fun coursesQuery(): ContinuousQueryDefinition {
        val query = String.format("SELECT * FROM /courses")

        return ContinuousQueryDefinition("new-courses", query,
                coursesRegionListener(), false)
    }

    @Bean
    fun coursesRegionListener() = CourseRegionListener()


    @Bean
    fun fluxTeachers(gemfireCache: GemFireCache): Flux<CqEvent> {
        val query = String.format("SELECT * FROM /teachers")
        val clientCache = gemfireCache as ClientCache
        val queryService = clientCache.queryService
        return streamFrom("flux-teacher", query, queryService)
    }

    @Bean
    fun fluxCourses(gemfireCache: GemFireCache): Flux<CqEvent> {
        val query = String.format("SELECT * FROM /courses")
        val clientCache = gemfireCache as ClientCache
        val queryService = clientCache.queryService
        return streamFrom("flux-courses", query, queryService)
    }
}