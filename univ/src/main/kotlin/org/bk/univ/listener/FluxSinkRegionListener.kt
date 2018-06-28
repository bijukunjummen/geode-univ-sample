package org.bk.univ.listener

import org.apache.geode.cache.query.CqAttributesFactory
import org.apache.geode.cache.query.CqEvent
import org.apache.geode.cache.query.QueryService
import org.apache.geode.cache.util.CqListenerAdapter
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.lang.RuntimeException


class FluxSinkRegionListener(private val fluxSink: FluxSink<CqEvent>) : CqListenerAdapter() {
    override fun close() {
        fluxSink.complete()
    }

    override fun onError(aCqEvent: CqEvent?) {
        fluxSink.error(RuntimeException("Something went wrong!"))
    }

    override fun onEvent(event: CqEvent) {
        fluxSink.next(event)
    }
}