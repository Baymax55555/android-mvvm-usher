package net.flow9.androidmvvm.repository.remote


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.Observable
import net.flow9.androidmvvm.repository.model.response.SchoolResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object SchoolApi {
    @Singleton
    @Provides
    fun schoolService(): SchoolService = Api.retrofit(Api.BASE_URL).create(SchoolService::class.java)
}

interface SchoolService {
    @GET("school")
    fun searchSchool(@Query("name") name: String
                     , @Query("page") page: Int
                     , @Query("size") size: Int): Observable<SchoolResponse>
}
