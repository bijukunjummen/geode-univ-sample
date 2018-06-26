package org.bk.univ.listener

import org.apache.geode.cache.query.CqEvent
import org.apache.geode.cache.util.CqListenerAdapter
import org.bk.univ.DateTimeUtils
import org.bk.univ.model.Teacher
import org.bk.univ.model.TeacherEntity
import reactor.core.publisher.FluxSink
import java.lang.RuntimeException


class FluxSinkRegionListener(private val fluxSink: FluxSink<Teacher>) : CqListenerAdapter() {
    override fun close() {
        fluxSink.complete()
    }

    override fun onError(aCqEvent: CqEvent?) {
        fluxSink.error(RuntimeException("Something went wrong!"))
    }

    override fun onEvent(event: CqEvent) {
        val teacherEntity = event.newValue as TeacherEntity
        fluxSink.next(teacherEntity.let {
            Teacher(
                    it.id,
                    it.name,
                    it.department,
                    it.age,
                    it.joinedDate?.let { DateTimeUtils.toDatetime(it) },
                    it.retirementDate?.let { DateTimeUtils.toDatetime(it) }
            )
        })
    }
}