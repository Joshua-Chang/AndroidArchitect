package com.jph.pro.wxapi;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.devio.as.proj.ability.BuildConfig;
import org.devio.hi.library.util.AppGlobals;

/**
 * @版本号：
 * @需求编号：
 * @功能描述：必须和applicationid即微信平台的包名一致
 * @创建时间：2021/2/21 11:59 PM
 * @创建人：常守达
 * @备注：
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI xWxapi = WXAPIFactory.createWXAPI(AppGlobals.INSTANCE.get(), BuildConfig.WX_SHARE_APP_KEY, true);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            xWxapi.handleIntent(getIntent(),this);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
}
