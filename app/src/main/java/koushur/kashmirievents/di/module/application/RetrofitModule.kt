package koushur.kashmirievents.di.module.application

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import koushir.kashmirievents.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitModule {

    companion object {
        private const val BASE_URL = "baseUrl"
    }

    /*
    * The method returns the Retrofit object
    * */
    @Provides
    @Singleton
    fun provideRetrofit(
        @Named(BASE_URL) baseUrl: String,
        okHttpClient: OkHttpClient.Builder,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        Timber.v("Base Url $baseUrl")
        return Retrofit.Builder()
            .client(okHttpClient.build())
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(baseUrl)
            .build()
    }

    /**
     * The method returns the Gson object
     */
    @Provides
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return gsonBuilder.create()
    }

    @Provides
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }


    @Singleton
    @Provides
    fun createLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Singleton
    @Provides
    fun createHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(httpLoggingInterceptor)
        Timber.v("")
        return httpClient
    }
}