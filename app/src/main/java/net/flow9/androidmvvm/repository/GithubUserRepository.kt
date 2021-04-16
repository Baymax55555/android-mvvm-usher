package net.flow9.androidmvvm.repository

import net.flow9.androidmvvm.repository.remote.GithubUserService
import javax.inject.Inject

class GithubUserRepository @Inject constructor(
    private val githubUserService: GithubUserService
) {
    fun getUsers() = githubUserService.getUsers()
}