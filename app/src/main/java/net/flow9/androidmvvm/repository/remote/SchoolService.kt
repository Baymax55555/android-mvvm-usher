package net.flow9.androidmvvm.repository.remote


import io.reactivex.Observable
import net.flow9.androidmvvm.repository.model.response.SchoolResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SchoolService {
    @GET("school")
    fun searchSchool(@Query("name") name: String
                     , @Query("page") page: Int
                     , @Query("size") size: Int): Observable<SchoolResponse>
}
