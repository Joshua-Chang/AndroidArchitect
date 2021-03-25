package org.devio.`as`.proj.hi_arch.scene3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.devio.`as`.proj.hi_arch.User

class HomeViewModel : ViewModel() {
    fun getUserInfo(): LiveData<User> {
        val liveData = MutableLiveData<User>()
        val user = User("Dave","456","New York")
        liveData.postValue(user)
        return liveData
    }
}