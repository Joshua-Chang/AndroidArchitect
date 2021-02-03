package org.devio.`as`.proj.main.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import org.devio.`as`.proj.main.R
import kotlinx.android.synthetic.main.fragment_profile_page.*
import org.devio.`as`.proj.common.ui.view.loadCircle
import org.devio.`as`.proj.common.ui.view.loadCorner
import org.devio.`as`.proj.main.biz.account.AccountManager
import org.devio.`as`.proj.main.http.ApiFactory
import org.devio.`as`.proj.main.http.api.AccountApi
import org.devio.`as`.proj.main.model.CourseNotice
import org.devio.`as`.proj.main.model.Notice
import org.devio.`as`.proj.main.model.UserProfile
import org.devio.`as`.proj.main.route.HiRoute
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.ui.banner.core.HiBannerMo

class ProfileFragment : HiBaseFragment() {
    private val REQUEST_CODE_LOGIN_PROFILE: Int = 1001
    val ITEM_PLACE_HOLDER = "   "
    override fun getLayoutId(): Int {
        return R.layout.fragment_profile_page
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        item_course.setText(R.string.if_notify)
        item_course.append(ITEM_PLACE_HOLDER + getString(R.string.item_notify))

        item_collection.setText(R.string.if_collection)
        item_collection.append(ITEM_PLACE_HOLDER + getString(R.string.item_collection))

        item_collection.setText(R.string.if_address)
        item_collection.append(ITEM_PLACE_HOLDER + getString(R.string.item_address))

        item_collection.setText(R.string.if_history)
        item_collection.append(ITEM_PLACE_HOLDER + getString(R.string.item_history))

        queryLoginUserData()
        queryCourseNotice()
    }

    private fun queryCourseNotice() {
        ApiFactory.create(AccountApi::class.java).notice().enqueue(object :HiCallback<CourseNotice>{
            override fun onSuccess(response: HiResponse<CourseNotice>) {
                if (response.data != null&&response.data!!.total>0) {
                    notify_count.setText(response.data!!.total.toString())
                    notify_count.visibility=View.VISIBLE
                }
            }

            override fun onFailed(throwable: Throwable) {
                throwable.printStackTrace()
            }
        })

    }

    private fun queryLoginUserData() {
        AccountManager.getUserProfile(this, Observer {profile->
            if (profile != null) {
                updateUI(profile)
            }else{
                showToast("用户信息获取失败")
            }
        },onlyCache = false)
    }

    private fun showToast(msg: String?) {
        if (msg == null) return
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(userProfile: UserProfile) {
        user_name.text =
            if (userProfile.isLogin) userProfile.userName else getString(R.string.profile_not_login)
        login_desc.text =
            if (userProfile.isLogin) getString(R.string.profile_login_desc_welcome_back) else getString(
                R.string.profile_login_desc
            )
        if (userProfile.isLogin){
            user_avatar.loadCircle(userProfile.avatar)
        }else{
            user_avatar.setImageResource(R.drawable.ic_avatar_default)
            user_name.setOnClickListener {
                /**
                 * 在fragment中，启动activity并得到result
                 * 传统方式：在fragment中fragment.startActivityForResult->fragment.onActivityResult
                 * 路由：在fragment中ARouter.navigation()是使用activity.startActivityForResult
                 * @see ProfileFragment.onActivityResult 是fragment的回调，无法直接取到result。
                 * 需要在fragment所属Activity即MainActivity内onActivityResult，遍历所有fragment并调用其onActivityResult
                 */
//                ARouter.getInstance().build("/account/login").navigation(activity,REQUEST_CODE_LOGIN_PROFILE)
                AccountManager.login(context, Observer { success->
                    queryLoginUserData()
                })
            }
        }

        tab_item_collection.text = spannableTabItem(
            userProfile.favoriteCount,
            getString(R.string.profile_tab_item_collection)
        )
        tab_item_history.text =
            spannableTabItem(userProfile.browseCount, getString(R.string.profile_tab_item_history))
        tab_item_learn.text =
            spannableTabItem(userProfile.learnMinutes, getString(R.string.profile_tab_item_learn))

        updateBanner(userProfile.bannerNoticeList)
    }

    private fun updateBanner(bannerNoticeList: List<Notice>?) {
        if (bannerNoticeList == null||bannerNoticeList.size<=0) return
        var modles = mutableListOf<HiBannerMo>()
        bannerNoticeList.forEach {
            val hiBannerMo = object : HiBannerMo() {}
            hiBannerMo.url=it.cover
            modles.add(hiBannerMo)
        }
        hi_banner.setBannerData(R.layout.layout_profile_banner_item,modles)
        hi_banner.setBindAdapter { viewHolder, mo, position ->
            if (viewHolder == null||mo==null) return@setBindAdapter
            val imageView = viewHolder.findViewById<ImageView>(R.id.banner_item_imageview)
            imageView.loadCorner(mo.url,HiDisplayUtil.dp2px(10f,resources))
        }
        hi_banner.setOnBannerClickListener { viewHolder, bannerMo, position ->
            HiRoute.startActivity4Browser(bannerNoticeList[position].url)
        }
        hi_banner.visibility=View.VISIBLE
    }

    private fun spannableTabItem(topText: Int, bottomText: String): CharSequence? {
        val spanStr = topText.toString()
        val ssb = SpannableStringBuilder()
        val ssTop = SpannableString(spanStr)
        ssTop.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.color_000)),
            0,
            ssTop.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssTop.setSpan(AbsoluteSizeSpan(18,true),
            0,
            ssTop.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ssTop.setSpan(StyleSpan(Typeface.BOLD),     0,
            ssTop.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ssb.append(ssTop)
        ssb.append(bottomText)
        return ssb
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode==REQUEST_CODE_LOGIN_PROFILE&&resultCode==Activity.RESULT_OK&&data!=null) {
//            queryLoginUserData()
//        }
    }
}