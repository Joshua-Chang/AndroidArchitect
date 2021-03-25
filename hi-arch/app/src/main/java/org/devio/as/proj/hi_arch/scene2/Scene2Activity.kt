package org.devio.`as`.proj.hi_arch.scene2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import org.devio.`as`.proj.hi_arch.R
import org.devio.`as`.proj.hi_arch.databinding.ActivityScene2Binding

/**
 * viewModel层替代P层完成与v/m交互。
 * 不同的是viewModel层使用双向绑定的方式，而不是回调
 *
 * 实现双向绑定
 * 方式一：dataBinding
 * 方式二：viewBinding+liveData生命周期安全
 * */
class Scene2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =DataBindingUtil.setContentView<ActivityScene2Binding>(this, R.layout.activity_scene2)
        val homeViewModel = HomeViewModel()
        binding.viewModel=homeViewModel
        homeViewModel.getUserInfo()

        binding.editAddress.addTextChangedListener {
            Log.e("scene2","afterTextChange"+homeViewModel.userField.get()?.adrress)
        }
    }

}