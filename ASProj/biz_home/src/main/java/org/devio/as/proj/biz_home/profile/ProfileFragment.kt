package org.devio.`as`.proj.biz_home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_profile_page.*
import org.devio.`as`.proj.biz_home.R
import org.devio.`as`.proj.biz_home.databinding.FragmentProfilePageBinding
import org.devio.`as`.proj.biz_home.databinding.FragmentProfilePageBinding.inflate
import org.devio.`as`.proj.common.route.HiRoute
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import org.devio.`as`.proj.common.ext.loadCorner
import org.devio.`as`.proj.common.ext.showToast
import org.devio.`as`.proj.service_login.LoginServiceProvider
import org.devio.`as`.proj.service_login.Notice
import org.devio.`as`.proj.service_login.UserProfile
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.ui.banner.core.HiBannerAdapter
import org.devio.hi.ui.banner.core.HiBannerMo

class ProfileFragment : HiBaseFragment() {
    private lateinit var binding: FragmentProfilePageBinding
    private lateinit var viewModel: ProfileViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile_page
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        queryLoginUserData()
        queryCourseNotice()
    }

    override fun getPageName(): String {
       return "ProfileFragment"
    }

    private fun queryCourseNotice() {
        viewModel.queryCourseNotice().observe(viewLifecycleOwner, Observer {
            binding.courseNotice = it
        })
    }

    private fun queryLoginUserData() {
        LoginServiceProvider.getUserProfile(this, Observer { profile ->
            if (profile != null) {
                binding.userProfile = profile
                updateUI(profile)
            } else {
                showToast(getString(R.string.fetch_user_profile_failed))
            }
        })
    }

    private fun updateUI(userProfile: UserProfile) {
        if (!userProfile.isLogin) {
            user_avatar.setImageResource(R.drawable.ic_avatar_default)
            user_name.setOnClickListener {
                LoginServiceProvider.login(context, Observer { success ->
                    queryLoginUserData()
                })
            }
        }
        updateBanner(userProfile.bannerNoticeList)

        binding.llNotice.setOnClickListener {
            HiRoute.startActivity(context, destination = "/notice/list")
        }
        binding.itemHistory.setOnClickListener {
            HiRoute.startActivity(context, destination = "/playground/main")
        }
    }

    private fun updateBanner(bannerList: List<Notice>?) {
        if (bannerList == null || bannerList.isEmpty()) return
        var models = mutableListOf<HiBannerMo>()
        bannerList.forEach {
            var hiBannerMo = object : HiBannerMo() {}
            hiBannerMo.url = it.cover
            models.add(hiBannerMo)
        }
        hi_banner.setOnBannerClickListener { viewHolder, bannerMo, position ->
            HiRoute.startActivity4Browser(bannerList[position].url)
        }
        hi_banner.setBannerData(R.layout.layout_profile_banner_item, models)
        hi_banner.setBindAdapter { viewHolder: HiBannerAdapter.HiBannerViewHolder?, mo: HiBannerMo?, position: Int ->
            if (viewHolder == null || mo == null) return@setBindAdapter
            val imageView = viewHolder.findViewById<ImageView>(R.id.banner_item_imageview)
            imageView.loadCorner(mo.url, HiDisplayUtil.dp2px(10f, resources))
        }
        hi_banner.visibility = View.VISIBLE
    }
}