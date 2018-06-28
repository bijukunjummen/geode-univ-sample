package org.bk.univ.listener

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.apache.geode.cache.query.CqAttributes
import org.apache.geode.cache.query.CqEvent
import org.apache.geode.cache.query.CqQuery
import org.apache.geode.cache.query.QueryService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.test.StepVerifier
import reactor.test.expectError
import java.time.Duration

class FluxSinkRegionListenerTest {

    @Test
    @DisplayName("Test creating a flux from listener - Happy Path")
    fun testListenerNoErrors() {
        val flux: Flux<CqEvent> = Flux.create { fluxSink: FluxSink<CqEvent> ->
            val fluxListener = FluxSinkRegionListener(fluxSink)
            val cqEvent1 = mock<CqEvent>()
            val cqEvent2 = mock<CqEvent>()
            fluxListener.onEvent(cqEvent1)
            fluxListener.onEvent(cqEvent2)
            fluxListener.close()
        }

        StepVerifier
                .create(flux)
                .expectNextCount(2)
                .expectComplete()
                .verify(Duration.ofSeconds(10))
    }

    @Test
    @DisplayName("Test creating a flux from listener with errors")
    fun testListenerWithErrors() {
        val flux: Flux<CqEvent> = Flux.create { fluxSink: FluxSink<CqEvent> ->
            val fluxListener = FluxSinkRegionListener(fluxSink)
            val cqEvent1 = mock<CqEvent>()
            val cqEvent2 = mock<CqEvent>()
            fluxListener.onEvent(cqEvent1)
            fluxListener.onError(cqEvent2)
            fluxListener.close()
        }

        StepVerifier
                .create(flux)
                .expectNextCount(1)
                .expectError<FluxCqEventListenerException>()
                .verify(Duration.ofSeconds(10))
    }

    @Test
    @DisplayName("Tests Utility function to create a stream of events")
    fun streamFromTest() {

        val mockQueryService = mock<QueryService>()
        val argumentCaptor: ArgumentCaptor<CqAttributes> = ArgumentCaptor.forClass(CqAttributes::class.java)
        val mockCqQuery = mock<CqQuery>()
        whenever(mockQueryService.newCq(eq("test-cq"), eq("some query"), argumentCaptor.capture())).thenReturn(mockCqQuery)

        val flux = streamFrom("test-cq", "some query", mockQueryService)
        flux.subscribe()
        val cqAttributes = argumentCaptor.value
        val cqListener = cqAttributes.cqListener as FluxSinkRegionListener

        StepVerifier
                .create(flux)
                .then {
                    cqListener.onEvent(mock())
                    cqListener.onEvent(mock())
                    cqListener.close()
                }.expectNextCount(2)
                .expectComplete()
                .verify(Duration.ofSeconds(3))

    }

}