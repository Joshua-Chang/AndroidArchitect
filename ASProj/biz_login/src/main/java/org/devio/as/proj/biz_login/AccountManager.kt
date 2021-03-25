package org.devio.`as`.proj.biz_login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.devio.`as`.proj.biz_login.api.AccountApi
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.`as`.proj.common.utils.SPUtil
import org.devio.`as`.proj.service_login.UserProfile
import org.devio.hi.library.cache.HiStorage
import org.devio.hi.library.executor.HiExecutor
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.library.util.AppGlobals

object AccountManager {
    private var userProfile: UserProfile? = null
    private var boardingPass: String?=null
    private val loginLiveData=MutableLiveData<Boolean>()
    private val profileLiveData=MutableLiveData<UserProfile>()
    private val loginForeverObservers= mutableListOf<Observer<Boolean>>()
    private val profileForeverObservers= mutableListOf<Observer<UserProfile?>>()
    private val KEY_BOARDING_PASS="boarding-pass"
    private val KEY_USER_PROFILE="user_profile"
    @Volatile
    private var isFetching=false
    fun login(context: Context?=AppGlobals.get(),observer: Observer<Boolean>){
        if (context is LifecycleOwner) {
            loginLiveData.observe(context,observer)
        }else{
            loginLiveData.observeForever(observer)
            loginForeverObservers.add(observer)
        }
        val intent=Intent(context,LoginActivity::class.java)
        if (context is Application) {
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (context == null) {
            throw IllegalStateException("context must not be null.")
        }
        context.startActivity(intent)
    }
    fun loginSuccess(boardingPass:String){
        SPUtil.putString(KEY_BOARDING_PASS,boardingPass)
        this.boardingPass=boardingPass
        loginLiveData.value=true
        clearLoginForeverObservers()
    }

    fun getBoardingPass():String?{
        if (TextUtils.isEmpty(boardingPass)){
            boardingPass=SPUtil.getString(KEY_BOARDING_PASS)
        }
        return boardingPass
    }
    @Synchronized
    fun getUserProfile(lifecycleOwner: LifecycleOwner?,observer: Observer<UserProfile?>,onlyCache:Boolean=true){
        if (lifecycleOwner == null) {
            profileLiveData.observeForever(observer)
            profileForeverObservers.add(observer)
        }else{
            profileLiveData.observe(lifecycleOwner,observer)
        }
        if (userProfile!=null&&onlyCache){
            profileLiveData.postValue(userProfile)
            return
        }
        if (isFetching) return
        isFetching=true
        ApiFactory.create(AccountApi::class.java).profile()
            .enqueue(object : HiCallback<UserProfile> {
                override fun onSuccess(response: HiResponse<UserProfile>) {
                    userProfile = response.data
                    if (response.code == HiResponse.SUCCESS && userProfile != null) {
                        HiExecutor.execute{
                            HiStorage.saveCache(KEY_USER_PROFILE,userProfile)
                            isFetching=false
                        }
//                        profileLiveData.postValue(userProfile)/*postValue 使用handler在消息队列中未必能在clear前及时收到消息*/
                        profileLiveData.value=userProfile/*使用set*/
                    } else {
//                        profileLiveData.postValue(null)
                        profileLiveData.value= null
                    }
                    clearProfileForeverObservers()
                }

                override fun onFailed(throwable: Throwable) {
                    isFetching=false
                    profileLiveData.postValue(null)
                    clearProfileForeverObservers()
                }
            })
    }
    private fun clearLoginForeverObservers() {
        for (observer in loginForeverObservers) {
            loginLiveData.removeObserver(observer)
        }
        loginForeverObservers.clear()
    }
    private fun clearProfileForeverObservers() {
        for (observer in profileForeverObservers) {
            profileLiveData.removeObserver(observer)
        }
        profileForeverObservers.clear()
    }
    fun isLogin():Boolean{
        return !TextUtils.isEmpty(getBoardingPass())
    }
}