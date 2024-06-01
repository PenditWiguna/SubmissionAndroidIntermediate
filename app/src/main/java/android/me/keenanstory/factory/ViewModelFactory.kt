package android.me.keenanstory.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.me.keenanstory.componen.repository.StoriesRepository
import android.me.keenanstory.componen.repository.UsersRepository
import android.me.keenanstory.StoriesInjection
import android.me.keenanstory.UsersInjection
import android.me.keenanstory.LoginModel
import android.me.keenanstory.MainViewModel
import android.me.keenanstory.MapViewModel
import android.me.keenanstory.RegisterModel

class StoriesViewModelFactory private constructor(private val usersRepository: UsersRepository, private val storiesRepository: StoriesRepository) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                    MainViewModel(usersRepository,storiesRepository ) as T
                }
                modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                    MapViewModel(storiesRepository) as T
                }
                else -> {
                    throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
                }
            }
        }
        companion object {

             @Volatile
            private var instanceStories: StoriesViewModelFactory? = null
            fun getInstance(context: Context): StoriesViewModelFactory =
                instanceStories ?: synchronized(this) {
                instanceStories ?: StoriesViewModelFactory(UsersInjection.providePreferences(context), StoriesInjection.provideRepository())
            }.also { instanceStories = it }
        }

    }

class UsersViewModelFactory (private val usersRepository: UsersRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterModel::class.java) -> {
                RegisterModel() as T
            }
            modelClass.isAssignableFrom(LoginModel::class.java) -> {
                LoginModel() as T
            }

            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }
    companion object {
        @Volatile
        private var instanceUsers: UsersViewModelFactory? = null
        fun getInstance(context: Context): UsersViewModelFactory =
            instanceUsers ?: synchronized(this) {
                instanceUsers ?: UsersViewModelFactory(UsersInjection.providePreferences(context))
            }.also { instanceUsers = it }
    }
}

