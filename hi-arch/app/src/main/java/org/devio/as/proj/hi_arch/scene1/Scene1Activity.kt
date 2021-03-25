package org.devio.`as`.proj.hi_arch.scene1

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import org.devio.`as`.proj.hi_arch.User

//class Scene1Activity : AppCompatActivity(), HomeContract.View {
class Scene1Activity : BaseActivity<HomePresenter>(), HomeContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val mPresenter = HomePresenter()
//        mPresenter.attach(this)
//        mPresenter.getUserInfo()
        mPresenter?.getUserInfo()
    }

    override fun onGetUserInfoResult(user: User, errorMsg: String) {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun isAlive(): Boolean {
        return !isDestroyed && !isFinishing
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detach()
    }
}