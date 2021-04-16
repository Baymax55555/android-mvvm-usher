package net.flow9.androidmvvm.repository

import net.flow9.androidmvvm.repository.remote.ApiService

class FindCityRepository  {
//    private val schoolDao: SchoolDao? = null
    private val cityService by lazy { ApiService.cityService() }

    fun getCities() = cityService.getCities()
}