package org.devio.as.proj.ability.pay.wxpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.devio.as.proj.ability.BuildConfig;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.devio.as.proj.ability.HiAbility;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

    private IWXAPI api;
	private String mPrice, mName, mTel, mAddress;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_PAY_APP_ID);
        api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent,this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX) {
			HiAbility.INSTANCE.postWXPayResult$hi_ability_debug(resp.errCode);
			finish();
		}
	}
}