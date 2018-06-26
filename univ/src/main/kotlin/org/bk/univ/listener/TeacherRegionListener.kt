package org.bk.univ.listener

import org.apache.geode.cache.query.CqEvent
import org.springframework.data.gemfire.listener.ContinuousQueryListener

class TeacherRegionListener: ContinuousQueryListener {
    override fun onEvent(event: CqEvent?) {
        println(event)
    }

}