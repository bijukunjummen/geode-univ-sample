package org.bk.univ.web

import org.apache.geode.cache.Region
import org.apache.geode.cache.client.ClientCache
import org.apache.geode.cache.query.SelectResults
import org.bk.univ.model.StudentEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/students")
class StudentController(val clientCache: ClientCache, val region: Region<String, StudentEntity>) {

    @RequestMapping(method = [RequestMethod.GET])
    fun listStudents(): List<StudentEntity> {
        val query = clientCache.queryService.newQuery("SELECT * FROM /students")
        val results = query.execute() as SelectResults<StudentEntity>
        return ArrayList(results)
    }
}