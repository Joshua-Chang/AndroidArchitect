package org.devio.`as`.proj.main.biz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_login.*
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.`as`.proj.common.utils.SPUtil
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.http.ApiFactory
import org.devio.`as`.proj.main.http.api.AccountApi
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse

@Route(path = "/account/login")
class LoginActivity : HiBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        action_back.setOnClickListener {
            onBackPressed()
        }
        action_register.setOnClickListener {
            goRegistration()
        }
        action_login.setOnClickListener {
            goLogin()
        }
    }

    private fun goLogin() {
        val name = input_item_username.getEditText().text
        val password = input_item_password.getEditText().text
        if (TextUtils.isEmpty(name) or (TextUtils.isEmpty(password))) return
        ApiFactory.create(AccountApi::class.java).login(name.toString(), password.toString())
            .enqueue(object : HiCallback<String> {
                override fun onSuccess(response: HiResponse<String>) {
                    if (response.code == HiResponse.SUCCESS) {
                        showToast(getString(R.string.login_success))
                        val data = response.data
                        SPUtil.putString("boarding-pass", data!!)
                        setResult(Activity.RESULT_OK, Intent())
                        finish()
                    } else {
                        showToast(getString(R.string.login_failed) + response.msg)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast(getString(R.string.login_failed) + throwable.message)
                }
            })
    }

    private val REQUEST_CODE_REGISTRATION: Int = 1000
    private fun goRegistration() {
        ARouter.getInstance().build("/account/registration")
            .navigation(this, REQUEST_CODE_REGISTRATION)//requestCode 在此处才会用到
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == RESULT_OK) and (data != null) and (requestCode == REQUEST_CODE_REGISTRATION)) {
            val username = data?.getStringExtra("username")
            if (!TextUtils.isEmpty(username)) {
                input_item_username.getEditText().setText(username)
            }
        }
    }
}