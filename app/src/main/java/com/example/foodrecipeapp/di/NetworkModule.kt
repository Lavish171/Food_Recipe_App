/*
* so to actually provide food recipes API,
* we need to satisfy the retrofit dependencies,
* to satisfy retrofit dependencies,we need
*  to provide the GsonConver.. and okHttpClient
* dependencies..*/


/*
   * we can see that in order to provide the API service we need
   *  to provide all the dependencies ,and that what we have done .*/

/*
  * so how hilt will know or where it will search that whether
  * we have satisfied the dependency of GsonConvertorFactory...
  *  or not,so hilt will search it in the return type of
  *  the function*/

/*return type of the functions tell the hilt library that
we have specified how to create the intance of those dependencies
 */
package com.example.foodrecipeapp.di

import com.example.foodrecipeapp.util.Contants.Companion.BASE_URL
import com.example.foodrecipeapp.data.network.FoodRecipesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideHttpClient():OkHttpClient{
        return OkHttpClient.Builder()
                .readTimeout(15,TimeUnit.SECONDS)
                .connectTimeout(15,TimeUnit.SECONDS)
                .build()
    }


    @Singleton
    @Provides
    fun provideConvertorFactory():GsonConverterFactory
    {
         return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory
    ):Retrofit {
       return Retrofit.Builder()
               .baseUrl(BASE_URL)
               .client(okHttpClient)
               .addConverterFactory(gsonConverterFactory)
               .build()
    }


    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): FoodRecipesApi
    {
        return retrofit.create(FoodRecipesApi::class.java)
    }
}