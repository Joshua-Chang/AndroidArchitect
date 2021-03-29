package org.devio.`as`.proj.hi_hilt

import android.content.Context
import android.widget.Toast
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
abstract class MainModule {
    /*binds提供抽象类/接口的方法，由具体实现类去做*/
    @Binds
    @ActivityScoped
    abstract fun bindService(impl: LoginServiceImpl):ILoginService

//  providers 需提供具体的实现，此处需要context参数，无法提供
//    @Provides
//    fun bindService():ILoginService{
//        return
//    }
}

interface ILoginService{
    fun login()
}
class LoginServiceImpl @Inject constructor(@ApplicationContext /*context类型*/val context: Context):ILoginService{
    override fun login() {
        Toast.makeText(context,"LoginServiceImpl.login",Toast.LENGTH_LONG).show()
    }
}