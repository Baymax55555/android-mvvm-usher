package net.flow9.androidmvvm.repository.remote

import io.reactivex.Observable
import net.flow9.androidmvvm.repository.model.response.CityResponse
import retrofit2.http.GET

interface CityService {
    @GET("region")
    fun getCities(): Observable<CityResponse>
}
