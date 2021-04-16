package net.flow9.androidmvvm.repository

import net.flow9.androidmvvm.repository.local.SchoolDao
import net.flow9.androidmvvm.repository.remote.Api

class FindSchoolRepository  {
    private val schoolDao: SchoolDao? = null
    private val schoolService by lazy { Api.schoolService() }

    fun searchSchool(name:String, page:Int, size:Int=10) = schoolService.searchSchool(name, page, size)
}