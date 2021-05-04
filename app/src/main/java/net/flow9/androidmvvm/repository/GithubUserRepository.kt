package net.flow9.androidmvvm.repository

import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    suspend fun getUsers() = remoteSource.getUsers()
            .subscribeOn(Schedulers.io())
            .timeout(3, TimeUnit.SECONDS)
            .doOnEach { Log.e(javaClass.simpleName, "${it.error?.localizedMessage}") }
            .doOnNext {
                CoroutineScope(Dispatchers.IO).launch { // save remote data to local room DB
                    localDao.insertAll(it)
                }
                Observable.just(it)
            }
            .onErrorResumeNext(
                    Observable.just(localDao.getAllUsers())
            )
}