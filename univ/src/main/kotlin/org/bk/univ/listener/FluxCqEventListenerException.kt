package org.bk.univ.listener

import org.apache.geode.cache.query.CqEvent
import java.lang.RuntimeException

class FluxCqEventListenerException(val cqEvent: CqEvent):RuntimeException()