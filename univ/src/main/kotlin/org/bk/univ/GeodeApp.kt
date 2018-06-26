package org.bk.univ

import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientCache
import org.apache.geode.cache.query.CqAttributesFactory
import org.bk.univ.listener.FluxSinkRegionListener
import org.bk.univ.listener.TeacherRegionListener
import org.bk.univ.model.Teacher
import org.bk.univ.model.TeacherEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
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
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux


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
    fun fluxNewTeachers(gemfireCache: GemFireCache):Flux<Teacher> {
        val query = String.format("SELECT * FROM /teachers")
        val clientCache = gemfireCache as ClientCache
        val queryService = clientCache.queryService
        val cqA = CqAttributesFactory()

        return Flux.create { fluxSink ->
            cqA.addCqListener(FluxSinkRegionListener(fluxSink))
            val cqQuery = queryService.newCq("flux-teacher", query, cqA.create())
            cqQuery.execute()
        }
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

@Component
class SampleCommandLine: CommandLineRunner {
    
    @Autowired
    lateinit var fluxNewTeachers: Flux<Teacher>
    override fun run(vararg args: String?) {
        fluxNewTeachers.subscribe { teacher -> println("From Flux!!!: " + teacher)}
    }

}

fun main(args: Array<String>) {
    runApplication<GeodeApp>(*args)
}

