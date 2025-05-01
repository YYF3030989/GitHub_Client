package com.example.githubclient.core.network

import com.example.githubclient.data.remote.api.GitHubAuthService
import com.example.githubclient.data.remote.api.GithubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_GITHUB_API_URL = "https://api.github.com/"
    private const val BASE_GITHUB_AUTH_URL = "https://github.com/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor = AuthInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_GITHUB_API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideGithubApiService(retrofit: Retrofit): GithubApiService =
        retrofit.create(GithubApiService::class.java)

    @Provides
    @Singleton
    fun provideGitHubAuthService(retrofit: Retrofit): GitHubAuthService =
        retrofit.newBuilder()
            .baseUrl(BASE_GITHUB_AUTH_URL) // override for auth
            .build()
            .create(GitHubAuthService::class.java)
}