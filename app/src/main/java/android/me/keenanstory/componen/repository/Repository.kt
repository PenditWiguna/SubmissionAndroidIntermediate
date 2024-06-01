package android.me.keenanstory.componen.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import android.me.keenanstory.Status
import android.me.keenanstory.api.Apiservice
import android.me.keenanstory.componen.response.*
import android.me.keenanstory.paging.StoriesPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class StoriesRepository (private val apiService: Apiservice) {
    fun listStory(token: String): LiveData<PagingData<TabStoriesItem>> {

        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),

            pagingSourceFactory = {
                StoriesPagingSource(apiService, token)

            }
        ).liveData
    }

        fun addFieldStories(token: String, photo: MultipartBody.Part, desc: RequestBody,lat:RequestBody?, lon:RequestBody?) : LiveData<Status<DataStoriesResponse>> = liveData {
            emit(Status.Process)
            try {
                val data = apiService.addFieldStories("Bearer $token", photo, desc,lat,lon)
                emit(Status.Done(data))
            } catch (except: Exception) {
                emit(Status.Fail(except.message.toString()))
            }
        }

    fun getMapData(token: String,location:Int) : LiveData<Status<TableStoriesResponse>> = liveData {
        emit(Status.Process)
        try {
        val data = apiService.getStoriesWiththePosition("Bearer $token",location)
        emit(Status.Done(data))
        } catch (except: Exception) {
            emit(Status.Fail(except.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instanceStories: StoriesRepository? = null
        fun getInstance(apiService: Apiservice): StoriesRepository =
            instanceStories ?: synchronized(this) {
                instanceStories ?: StoriesRepository(apiService)
            }.also { instanceStories = it }
    }
}

class UsersRepository(private val dataStore: DataStore<Preferences>, private val apiService: Apiservice) {
    fun login(email: String, password: String) : LiveData<Status<LoginResponse>> = liveData {
        emit(Status.Process)
        try {
            val data = apiService.loginModePost(email, password)
            emit(Status.Done(data))
        } catch (except: Exception) {
            emit(Status.Fail(except.message.toString()))
        }
    }

    fun register(name: String, email: String, password: String) : LiveData<Status<RegisterResponse>> = liveData {
        emit(Status.Process)
        try {
                val data =  apiService.registerModePost(name, email, password)
                emit(Status.Done(data))
            } catch (except: Exception) {
                emit(Status.Fail(except.message.toString()))
            }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun setToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[EXTRA_KEY] = false
        }
    }

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[EXTRA_KEY ] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[EXTRA_KEY ] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var instanceUsers: UsersRepository? = null

        private val TOKEN = stringPreferencesKey("token")
        private val EXTRA_KEY = booleanPreferencesKey("extra")

        fun getInstance(dataStore: DataStore<Preferences>, apiService: Apiservice): UsersRepository {
            return instanceUsers ?: synchronized(this) {
                val instance = UsersRepository(dataStore, apiService)
                instanceUsers = instance
                instance
            }
        }
    }
}
