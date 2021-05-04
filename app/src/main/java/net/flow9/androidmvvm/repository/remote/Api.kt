package net.flow9.androidmvvm.repository.remote

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object Api {


    @Singleton
    @Provides
    @Named("base_url")
    fun create_base_url() = "https://api.github.com/"

    var token = ""

    @Singleton
    @Provides
    fun retrofit(@Named("base_url") baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).apply {

            val client = OkHttpClient.Builder().apply {

                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(interceptor)

                addInterceptor(
                    Interceptor { chain ->
                        Log.d("Authorize", "intercepter ==================> ${token}")
                        val builder = chain.request().newBuilder()
                        val response = chain.proceed(builder.build())

                        val authorization = response.header("Authorization")
                        Log.d("Authorize", "Authorization=$authorization")

                        if (authorization?.isNotEmpty() == true) {
                            token = authorization
                        }
                        return@Interceptor response
                    }
                )
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(15, TimeUnit.SECONDS)
                writeTimeout(15, TimeUnit.SECONDS)
            }.build()

            client(client)
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addConverterFactory(GsonConverterFactory.create())

        }.build()
    }

    suspend fun isInternetAvailable(): Boolean {
        try {
            val address = InetAddress.getByName("www.google.com")
            return address.hostName != ""
        } catch (e: UnknownHostException) {
            // Log error
        }
        return false
    }
}