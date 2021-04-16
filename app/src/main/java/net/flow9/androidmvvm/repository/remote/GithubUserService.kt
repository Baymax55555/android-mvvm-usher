package net.flow9.androidmvvm.repository.remote

import io.reactivex.Observable
import net.flow9.androidmvvm.repository.model.response.GithubUserResponse
import retrofit2.http.GET

interface GithubUserService {
    @GET("users")
    fun getUsers(): Observable<GithubUserResponse>
}