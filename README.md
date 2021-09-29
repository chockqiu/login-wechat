# login-wechat
微信一键登录快速集成开发库

## 仅需3步，快速接入微信一键登录
#### 1、添加依赖
在工程根目录的**build.gradle**中添加
```
allprojects {
	repositories {
		maven { url 'https://www.jitpack.io' }
		...
	}
}
```
在app的build.gradle中添加lib依赖
```
implementation 'com.github.chockqiu:login-wechat:v1.1'
```
#### 2、注册应用
在Application的onCreate注册应用
```
WechatLoginUtils.getInstance().registerApp(this, "从微信官方申请的AppId");//AppId与应用一一对应, 不可随意填写
```
#### 3、唤起微信登录
```
WechatLoginUtils.getInstance().login(new WechatLoginUtils.ResultCallback() {
    @Override
    public void onSuccess(String code) {
        //已获得微信code
    }

    @Override
    public void onFailed(int errorCode) {
        //出现异常, 错误码见微信官网
    }
});
```
