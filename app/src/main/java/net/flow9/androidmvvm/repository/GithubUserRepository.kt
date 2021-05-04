package net.flow9.androidmvvm.repository

import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.flow9.androidmvvm.repository.local.GithubUserDao
import net.flow9.androidmvvm.repository.model.response.GithubUser
import net.flow9.androidmvvm.repository.remote.GithubUserService
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubUserRepository @Inject constructor(
    private val remoteSource: GithubUserService,
    private val localDao: GithubUserDao
) {
    suspend fun getUsers() : Observable<List<GithubUser>> {

        return remoteSource.getUsers()
                .subscribeOn(Schedulers.io())
                .timeout(3, TimeUnit.SECONDS)
                .doOnEach { Log.e(javaClass.simpleName, "${it.error?.localizedMessage}") }
                .onErrorResumeNext (
                    Observable.just(localDao.getAllUsers())
                )
    }
}