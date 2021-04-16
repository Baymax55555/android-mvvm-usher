package net.flow9.androidmvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.flow9.androidmvvm.repository.GithubUserRepository
import net.flow9.androidmvvm.repository.model.response.GithubUser
import net.flow9.androidmvvm.repository.model.response.GithubUserResponse
import javax.inject.Inject

class GithubUserViewModel @Inject constructor(
    private val githubUserRepository: GithubUserRepository
) : ViewModel(), LifecycleObserver {

    val userList by lazy { MutableLiveData<GithubUserResponse>() }

    init {
        getUser()
    }

    fun getUser() {
        githubUserRepository.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                userList.postValue(response)
            },{
                Log.e("학교검색", "error=${it.localizedMessage}")
            })
    }

    fun onItemClick(user: GithubUser) {
        Log.d("뷰모델", "item clicked = $user")
    }
}