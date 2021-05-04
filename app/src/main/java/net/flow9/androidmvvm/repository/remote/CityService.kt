package net.flow9.androidmvvm.repository.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.Observable
import net.flow9.androidmvvm.repository.model.response.CityResponse
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CityApi {

    @Provides
    fun cityService(retrofit: Retrofit): CityService = retrofit.create(CityService::class.java)
}

interface CityService {
    @GET("region")
    fun getCities(): Observable<CityResponse>
}
