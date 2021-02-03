package org.devio.`as`.proj.main.fragment.detail

import android.text.TextUtils
import androidx.lifecycle.*
import org.devio.`as`.proj.main.BuildConfig
import org.devio.`as`.proj.main.http.ApiFactory
import org.devio.`as`.proj.main.http.api.DetailApi
import org.devio.`as`.proj.main.model.DetailModel
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse

class DetailViewModel(val goodsId: String?) : ViewModel() {
    companion object {
        private class DetailViewModelFactory(val goodsId: String?) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                try {
                    val constructor = modelClass.getConstructor(String::class.java)
                    if (constructor != null) {
                        return constructor.newInstance(goodsId)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return super.create(modelClass)/*无参构造*/
            }
        }

        fun get(goodsId: String?, viewModelStoreOwner: ViewModelStoreOwner): DetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsId)).get(
                DetailViewModel::class.java
            )
        }
    }

    fun queryDetailData(): LiveData<DetailModel?> {
        val pageData = MutableLiveData<DetailModel?>()
        if (!TextUtils.isEmpty(goodsId)) {
            ApiFactory.create(DetailApi::class.java).queryDetail(goodsId!!)
                .enqueue(object : HiCallback<DetailModel> {
                    override fun onSuccess(response: HiResponse<DetailModel>) {
                        if (response.successful() && response.data != null) {
                            pageData.postValue(response.data)
                        } else {
                            pageData.postValue(null)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        pageData.postValue(null)
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace()
                        }
                    }
                })
        }
        return pageData
    }
}