package org.devio.`as`.proj.hi_arch.scene1

import org.devio.`as`.proj.hi_arch.User

interface HomeContract {
    interface View:BaseView{
        fun onGetUserInfoResult(user:User,errorMsg:String)
    }
    abstract class Presenter: BasePresenter<View>() {
        abstract fun getUserInfo()
    }
    /*此处可根据复杂度，来确定是否需要单独的model层，去具体做P层的工作*/
}