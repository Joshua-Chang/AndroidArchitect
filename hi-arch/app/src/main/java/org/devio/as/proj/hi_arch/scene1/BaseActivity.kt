package org.devio.`as`.proj.hi_arch.scene1

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.ParameterizedType

open class BaseActivity<P : BasePresenter<*>> : AppCompatActivity(), BaseView {
    protected var mPresenter:P ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val superClass = this.javaClass.genericSuperclass
        if (superClass is ParameterizedType) {
            val arguments = superClass.actualTypeArguments
            if (arguments[0] is BasePresenter<*>) {
                mPresenter = arguments[0].javaClass.newInstance() as P
            }
        }
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