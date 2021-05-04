package net.flow9.androidmvvm.viewmodel

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.flow9.androidmvvm.repository.FindSchoolRepository
import net.flow9.androidmvvm.repository.model.response.School
import net.flow9.androidmvvm.repository.model.response.SchoolResponse
import javax.inject.Inject

class FindSchoolViewModel @Inject constructor(
        private val repository:FindSchoolRepository
) : BaseViewModel(), LifecycleObserver {

//    val repository by lazy { FindSchoolRepository() }

    val currentPage = MutableLiveData<String>("0")
    val totalPage = MutableLiveData<String>("0")

    val schoolResponse by lazy {
        MutableLiveData<SchoolResponse>()
    }

    var word = ""

    fun textChanged(s: CharSequence,start: Int,before : Int,count :Int){
        word = s.toString()
        if(word.length > 1) searchSchool()
    }

    fun searchSchool(page:Int=0) {
        repository.searchSchool(word, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Log.d("학교검색", "response=$response page=$page")
                    currentPage.postValue("${page+1} ")
                    totalPage.postValue(" ${response.data.totalPages}")
                    schoolResponse.postValue(response)
                },{
                    Log.e("학교검색", "error=${it.localizedMessage} page=$page")
                })
    }

    fun onPrevClick() {
        Log.d("학교검색", "currentPage.value=${currentPage.value} word=$word")
        currentPage.value?.let { page ->
            val current = page.trim().toInt() - 1
            if(current > 0) {
                searchSchool( current - 1)
            }
        }
    }

    fun onNextClick() {
        Log.d("학교검색", "currentPage.value=${currentPage.value} word=$word total=${totalPage.value}")
        currentPage.value?.let { page ->
            val current = page.trim().toInt() - 1
            val total = totalPage.value?.trim()?.toInt() ?: 0

            if(current in 0 until total-1) {
                searchSchool( current  + 1)
            }
        }
    }

    var onItemClickCallback: ((school: School?) -> Unit)? = null

    fun onItemClick(school:School) {
        Log.d("학교검색", "onItemClick .school=${school}")
        onItemClickCallback?.let { it(school) }
    }
}