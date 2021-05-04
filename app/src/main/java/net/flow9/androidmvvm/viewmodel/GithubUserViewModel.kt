package net.flow9.androidmvvm.viewmodel

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import net.flow9.androidmvvm.repository.GithubUserRepository
import net.flow9.androidmvvm.repository.model.response.GithubUser
import javax.inject.Inject

class GithubUserViewModel @Inject constructor(
    private val githubUserRepository : GithubUserRepository
): ViewModel(), LifecycleObserver {

    val userList by lazy { MutableLiveData<List<GithubUser>>() }

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            githubUserRepository.getUsers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        userList.postValue(response)
                    },{ error ->
                        Log.e("검색", "error=${error.localizedMessage}")
                    })
        }

    }

    fun onItemClick(user: GithubUser) {
        Log.d("뷰모델", "item clicked = $user")
    }
}