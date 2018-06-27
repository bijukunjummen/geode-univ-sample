package org.bk.univ.listener

import org.apache.geode.cache.query.CqEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.gemfire.listener.ContinuousQueryListener

class CourseRegionListener: ContinuousQueryListener {
    private val logger: Logger = LoggerFactory.getLogger(CourseRegionListener::class.java)
    
    override fun onEvent(event: CqEvent?) {
        logger.info("In CourseRegionListener: {}", event)
    }

}