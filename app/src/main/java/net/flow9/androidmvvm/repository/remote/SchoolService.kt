package net.flow9.androidmvvm.repository.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.Observable
import net.flow9.androidmvvm.repository.model.response.SchoolResponse
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

@Module
@InstallIn(ApplicationComponent::class)
object SchoolApi {

    @Provides
    fun schoolService(retrofit: Retrofit): SchoolService = retrofit.create(SchoolService::class.java)
}

interface SchoolService {
    @GET("school")
    fun searchSchool(@Query("name") name: String
                     , @Query("page") page: Int
                     , @Query("size") size: Int): Observable<SchoolResponse>
}
