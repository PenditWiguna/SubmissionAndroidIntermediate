package android.me.keenanstory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import android.me.keenanstory.api.ApiConfig
import android.me.keenanstory.componen.repository.StoriesRepository
import android.me.keenanstory.componen.repository.UsersRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object StoriesInjection {
    fun provideRepository(): StoriesRepository {
        val apiService = ApiConfig.getApiService()
        return StoriesRepository.getInstance(apiService)
    }
}

object UsersInjection {
    fun providePreferences(context: Context): UsersRepository {
        val apiService = ApiConfig.getApiService()
        return UsersRepository.getInstance(context.dataStore, apiService)
    }
}
