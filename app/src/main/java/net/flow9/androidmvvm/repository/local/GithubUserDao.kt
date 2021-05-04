package net.flow9.androidmvvm.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.flow9.androidmvvm.repository.model.response.GithubUser

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM github_user")
    suspend fun getAllUsers() : List<GithubUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<GithubUser>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: GithubUser)
}