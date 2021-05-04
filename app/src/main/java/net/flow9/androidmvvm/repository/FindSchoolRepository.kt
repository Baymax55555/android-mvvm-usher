package net.flow9.androidmvvm.repository

import net.flow9.androidmvvm.repository.local.SchoolDao
import net.flow9.androidmvvm.repository.remote.SchoolApi
import net.flow9.androidmvvm.repository.remote.SchoolService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FindSchoolRepository @Inject constructor(
        private val schoolService: SchoolService
){

//    private val schoolService by lazy { SchoolApi.schoolService() }
    private val schoolDao: SchoolDao? = null
    fun searchSchool(name:String, page:Int, size:Int=10) = schoolService.searchSchool(name, page, size)
}