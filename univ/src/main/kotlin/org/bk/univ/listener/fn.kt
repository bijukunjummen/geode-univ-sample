package org.bk.univ.listener

import org.apache.geode.cache.query.CqAttributesFactory
import org.apache.geode.cache.query.CqEvent
import org.apache.geode.cache.query.QueryService
import reactor.core.publisher.Flux


//Creates a single publisher that multiple subscribers can listen to
fun streamFrom(cqName: String, query: String, queryService: QueryService): Flux<CqEvent> {
    val flux: Flux<CqEvent> = Flux.create { fluxSink ->
        val cqA = CqAttributesFactory()
        cqA.addCqListener(FluxSinkRegionListener(fluxSink))
        val cqQuery = queryService.newCq(cqName, query, cqA.create())
        cqQuery.execute()
    }
    return flux.publish().autoConnect()
}
