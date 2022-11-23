package com.luthfirr.core.di

import androidx.room.Room
import com.luthfirr.core.BuildConfig
import com.luthfirr.core.data.MusicRepository
import com.luthfirr.core.data.source.RemoteDataSource
import com.luthfirr.core.data.source.local.LocalDataSource
import com.luthfirr.core.data.source.local.room.MusicDatabase
import com.luthfirr.core.data.source.remote.network.ApiService
import com.luthfirr.core.domain.repository.IMusicRepository
import com.luthfirr.core.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<MusicDatabase>().musicDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("123".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            MusicDatabase::class.java, "Music"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val loggingInterceptor = if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val hostname = BuildConfig.HOSTNAME
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, BuildConfig.CERT1)
            .add(hostname, BuildConfig.CERT2)
            .add(hostname, BuildConfig.CERT3)
            .build()
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IMusicRepository> {
        MusicRepository(
            get(),
            get(),
            get()
        )
    }
}