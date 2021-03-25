package org.devio.`as`.proj.hi_arch.scene1

import org.devio.`as`.proj.hi_arch.User

class HomePresenter :HomeContract.Presenter(){
    override fun getUserInfo() {
        /*model网络请求或直接请求，得到结果*/
        if (view != null&& view!!.isAlive()) {
            val user = User("Tom","123","beijing")
            view!!.onGetUserInfoResult(user,"")
        }
    }
}