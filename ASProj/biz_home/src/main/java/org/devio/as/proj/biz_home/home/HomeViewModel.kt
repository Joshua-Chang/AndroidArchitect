package org.devio.`as`.proj.biz_home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.devio.`as`.proj.biz_home.api.HomeApi
import org.devio.`as`.proj.biz_home.model.HomeModel
import org.devio.`as`.proj.biz_home.model.TabCategory
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse

/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/8 3:32 PM
 * @创建人：常守达
 * @备注：
 */
class HomeViewModel(private val savedState: SavedStateHandle/*用savedState来缓存数据*/) : ViewModel() {
    /*savedState 为官方内存不足时的缓存*/
    fun queryCategoryTab(): MutableLiveData<List<TabCategory>?> {
        val liveData = MutableLiveData<List<TabCategory>?>()
        val memCache = savedState.get<List<TabCategory>?>("categoryTabs")
        if (memCache != null) {
            liveData.postValue(memCache)
            return liveData
        }
        ApiFactory.create(HomeApi::class.java)
            .queryTabList().enqueue(object : HiCallback<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.successful() && data != null) {
                        liveData.value = data
//                        liveData.postValue(data)
                        savedState.set("categoryTabs", data)
                    }
                }
            })
        return liveData
    }

    fun queryTabCategoryList(categoryId:String?, pageIndex:Int,cacheStrategy: Int):LiveData<HomeModel?> {
        val liveData=MutableLiveData<HomeModel?>()
        val memCache = savedState.get<HomeModel?>("categoryList")
        if (memCache != null) {
            liveData.postValue(memCache)
        }
        ApiFactory.create(HomeApi::class.java).queryTabCategoryList(cacheStrategy,categoryId!!, pageIndex, 10)
            .enqueue(object : HiCallback<HomeModel> {
                override fun onSuccess(response: HiResponse<HomeModel>) {
                    val data = response.data
                    if (response.successful() && data != null) {
                        liveData.postValue(data)
                        savedState.set("categoryList",data)
                    } else {
                        liveData.postValue(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })
        return liveData
    }
}
