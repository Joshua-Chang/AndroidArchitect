package org.devio.`as`.proj.hi_arch.scene2

import androidx.databinding.ObservableField
import org.devio.`as`.proj.hi_arch.User

class HomeViewModel {
    val userField:ObservableField<User> = ObservableField<User>()
    fun getUserInfo(){
        val user = User("Jim", "324", "Shanghai")
        userField.set(user)
    }
}