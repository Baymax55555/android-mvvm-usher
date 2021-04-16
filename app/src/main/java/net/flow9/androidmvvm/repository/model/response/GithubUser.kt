package net.flow9.androidmvvm.repository.model.response

import net.flow9.androidmvvm.activity.base.BaseDiffItem

data class GithubUserResponse(
    val users : List<GithubUser>
)

data class GithubUser(
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val html_url: String,
    val id: Int,
    val login: String,
    val node_id: String,
    val organizations_url: String,
    val received_events_url: String,
    val repos_url: String,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val type: String,
    val url: String
) : BaseDiffItem {
    override fun getId(): String {
        return "$id"
    }
}