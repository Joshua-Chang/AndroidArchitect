package org.devio.`as`.proj.main.fragment

import android.os.Bundle
import android.view.View
import org.devio.`as`.proj.common.flutter.HiFlutterCacheManager
import org.devio.`as`.proj.common.flutter.HiFlutterFragment

class RecommendFragment : HiFlutterFragment(HiFlutterCacheManager.MODULE_NAME_RECOMMEND) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("推荐")
    }
    override fun getPageName(): String {
        return "RecommendFragment"
    }
}