package net.flow9.androidmvvm.repository.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.Observable
import net.flow9.androidmvvm.repository.model.response.GithubUser
import retrofit2.Retrofit
import retrofit2.http.GET

@Module
@InstallIn(ApplicationComponent::class)
object GithubUserApi {

    @Provides
    fun githubUserService(retrofit: Retrofit): GithubUserService = retrofit.create(GithubUserService::class.java)
}

interface GithubUserService {
    @GET("users")
    fun getUsers(): Observable<List<GithubUser>>
}