package com.chockqiu.login.wechat;

import android.content.Context;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.UUID;

/**
 * @author Administrator
 */
public class WechatLoginUtils {

    private WechatLoginUtils() {
    }

    private static WechatLoginUtils mWechatLoginUtils;
    public static final String M_TRANSACTION = "login";

    public synchronized static WechatLoginUtils getInstance() {
        if (mWechatLoginUtils == null) {
            mWechatLoginUtils = new WechatLoginUtils();
        }
        return mWechatLoginUtils;
    }


    IWXAPI wxapi;

    /**
     * 注册微信APPID
     *
     * @param mContext 上下文
     * @param appId    向微信申请的AppId
     * @return 结果
     */
    public boolean registerApp(Context mContext, String appId) {
        wxapi = WXAPIFactory.createWXAPI(mContext.getApplicationContext(), appId, true);
        return wxapi.registerApp(appId);
    }

    /**
     * 是否安装微信客户端
     *
     * @return 结果
     */
    public boolean isWxAppInstalled() {
        if (wxapi == null) {
            return false;
        }
        return wxapi.isWXAppInstalled();
    }

    /**
     * 调起微信登录
     *
     * @return 结果
     */
    public boolean login() {
        if (isWxAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = UUID.randomUUID().toString();
            req.transaction = M_TRANSACTION;
            return wxapi.sendReq(req);
        }
        return false;
    }

    public ResultCallback mResultCallback;

    /**
     * 设置微信回调结果
     *
     * @param callback 回调
     */
    public void setResultCallback(ResultCallback callback) {
        mResultCallback = callback;
    }

    void processResult(BaseResp baseResp) {
        if (mResultCallback != null) {
            mResultCallback.processResult(baseResp);
        }
    }

    public static abstract class ResultCallback {

        public void processResult(BaseResp baseResp) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK && M_TRANSACTION.equals(baseResp.transaction) && baseResp instanceof SendAuth.Resp) {
                onSuccess(((SendAuth.Resp) baseResp).code);
            } else {
                onFailed(baseResp.errCode);
            }
        }

        public abstract void onSuccess(String code);

        public abstract void onFailed(int errorCode);
    }
}
