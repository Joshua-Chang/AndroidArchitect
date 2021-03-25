package org.devio.`as`.proj.hi_arch.scene2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.devio.`as`.proj.hi_arch.R
import org.devio.`as`.proj.hi_arch.User
import org.devio.`as`.proj.hi_arch.databinding.ActivityScene2Binding
import org.devio.`as`.proj.hi_arch.databinding.ActivityScene3Binding
import org.devio.`as`.proj.hi_arch.scene3.HomeViewModel

/**
 * viewModel层替代P层完成与v/m交互。
 * 不同的是viewModel层使用双向绑定的方式，而不是回调
 *
 * 实现双向绑定
 * 方式一：dataBinding
 * 方式二：viewBinding+liveData生命周期安全
 * */
class Scene3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityScene3Binding>(this, R.layout.activity_scene3)
        val viewModelProvider = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
        val viewModel = viewModelProvider.get(HomeViewModel::class.java)
        viewModel.getUserInfo().observe(this,object :Observer<User>{
            override fun onChanged(user: User?) {
                binding.user=user
            }
        })
    }
}