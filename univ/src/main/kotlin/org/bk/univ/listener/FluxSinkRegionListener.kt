package org.bk.univ.listener

import org.apache.geode.cache.query.CqEvent
import org.apache.geode.cache.util.CqListenerAdapter
import reactor.core.publisher.FluxSink


class FluxSinkRegionListener(private val fluxSink: FluxSink<CqEvent>) : CqListenerAdapter() {
    override fun close() {
        fluxSink.complete()
    }

    override fun onError(aCqEvent: CqEvent) {
        fluxSink.error(FluxCqEventListenerException(aCqEvent))
    }

    override fun onEvent(event: CqEvent) {
        fluxSink.next(event)
    }
}