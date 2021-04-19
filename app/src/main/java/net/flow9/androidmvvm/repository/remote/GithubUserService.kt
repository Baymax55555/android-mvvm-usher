package net.flow9.androidmvvm.repository.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.Observable
import net.flow9.androidmvvm.repository.model.response.GithubUser
import retrofit2.http.GET
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object GithubUserApi {
    @Singleton
    @Provides
    fun githubUserService(): GithubUserService = Api.retrofit(Api.BASE_URL).create(GithubUserService::class.java)
}

interface GithubUserService {
    @GET("users")
    fun getUsers(): Observable<List<GithubUser>>
}