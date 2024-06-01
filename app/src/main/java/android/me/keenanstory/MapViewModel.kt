package android.me.keenanstory

import android.me.keenanstory.componen.repository.StoriesRepository
import androidx.lifecycle.ViewModel

class MapViewModel (private val storiesRepository: StoriesRepository) : ViewModel() {
    fun getAllMapData(token: String,location:Int) = storiesRepository.getMapData(token,location)

}