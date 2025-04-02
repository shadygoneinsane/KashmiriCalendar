package koushur.kashmirievents.di.module.application

import com.google.gson.GsonBuilder
import koushir.kashmirievents.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val retrofitModule = module {
    single { provideLoggingInterceptor() }
    single { provideBaseUrl() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get(), get()) }
}

/**
 * The method returns the Retrofit object
 */
private fun provideRetrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient.Builder
): Retrofit {
    return Retrofit.Builder().client(okHttpClient.build())
        .addConverterFactory(provideGsonConverterFactory())
        .baseUrl(baseUrl)
        .build()
}

private fun provideBaseUrl(): String {
    return "https//api.github.com/"
}

private fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
    val httpClient = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) httpClient.addInterceptor(httpLoggingInterceptor)

    httpClient.readTimeout(2, TimeUnit.MINUTES)
    httpClient.writeTimeout(3, TimeUnit.MINUTES)
    httpClient.connectTimeout(3, TimeUnit.MINUTES)
    httpClient.build()

    return httpClient
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
}

private fun provideGsonConverterFactory(): GsonConverterFactory {
    val gsonBuilder = GsonBuilder()
    return GsonConverterFactory.create(gsonBuilder.create())
}