package com.youwu.shouyinsaas.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.bumptech.glide.request.RequestOptions;
import com.cretin.www.cretinautoupdatelibrary.model.TypeConfig;
import com.cretin.www.cretinautoupdatelibrary.model.UpdateConfig;
import com.cretin.www.cretinautoupdatelibrary.utils.AppUpdateUtils;
import com.cretin.www.cretinautoupdatelibrary.utils.SSLUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.xuexiang.xui.XUI;
import com.youwu.shouyinsaas.BuildConfig;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.fu_ping.SunConnection;
import com.youwu.shouyinsaas.fu_ping.bean.SystemBannerBean;
import com.youwu.shouyinsaas.service.HttpHelper;
import com.youwu.shouyinsaas.service.VolleyProcessor;
import com.youwu.shouyinsaas.sunmi.AidlUtil;
import com.youwu.shouyinsaas.ui.login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.youwu.shouyinsaas.update_model.OkHttp3Connection;
import com.youwu.shouyinsaas.update_model.UpdateModel;
import com.youwu.shouyinsaas.util.SPUtils;
import com.youwu.shouyinsaas.utils.CrashHandler;
import com.youwu.shouyinsaas.utils_view.RxToast;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;
import okhttp3.OkHttpClient;


/**
 * 2022/03/21
 */

public class AppApplication extends BaseApplication {

    public static SPUtils spUtils;
    private static Context context;
    private static AppApplication instance;

    public static TextToSpeech textToSpeech = null;//创建自带语音对象
    public static SpeechSynthesizer mSpeechSynthesizer;
    public static String speak = "0";
    public static String speed = "5";
    public static String volume = "5";

    public static String orderType = "A";

    public static Gson gson;

    public static RequestOptions options = new RequestOptions()
            .centerCrop().error(R.mipmap.load_fail)
            .placeholder(R.mipmap.loading);

    public static SystemBannerBean systemBannerBean = new SystemBannerBean();

    @Override
    public void onCreate() {
        super.onCreate();
        //是否开启打印日志
        KLog.init(BuildConfig.DEBUG);
        //初始化全局异常崩溃
        initCrash();
        RxToast.setContext(this);
        context = getApplicationContext();
        spUtils = new SPUtils("com.youwu.shouyin_saas");
        XUI.init(this); //初始化UI框架
        //初始化Volley方式网络请求代理
        HttpHelper.init(new VolleyProcessor(this));

        /*用于捕获上线后的异常*/
        CrashHandler.getInstance().init(this);


        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(this);
        mSpeechSynthesizer.setAppId("28998075");
        mSpeechSynthesizer.setApiKey("b6kVeI65HMVcevGkjfkPRQ9f", "HvI8ia3GZncGngixa11A6vZFdwp31UMq");
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, speak);
        mSpeechSynthesizer.initTts(TtsMode.ONLINE);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, speed);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, volume);

        initTTS(getApplicationContext());

        gson = new Gson();
        //sunmi  打印服务
        AidlUtil.getInstance().connectPrinterService(this);


        /**
         * 更新库
         */
        //如果你想使用okhttp作为下载的载体，那么你需要自己依赖okhttp，更新库不强制依赖okhttp！可以使用如下代码创建一个OkHttpClient 并在UpdateConfig中配置setCustomDownloadConnectionCreator start
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30_000, TimeUnit.SECONDS)
                .readTimeout(30_000, TimeUnit.SECONDS)
                .writeTimeout(30_000, TimeUnit.SECONDS)
                //如果你需要信任所有的证书，可解决根证书不被信任导致无法下载的问题 start
//                .sslSocketFactory(SSLUtils.createSSLSocketFactory())
                .hostnameVerifier(new SSLUtils.TrustAllHostnameVerifier())
                //如果你需要信任所有的证书，可解决根证书不被信任导致无法下载的问题 end
                .retryOnConnectionFailure(true);
        //如果你想使用okhttp作为下载的载体，那么你需要自己依赖okhttp，更新库不强制依赖okhttp！可以使用如下代码创建一个OkHttpClient 并在UpdateConfig中配置setCustomDownloadConnectionCreator end

        //更新库配置
        UpdateConfig updateConfig = new UpdateConfig()
                .setDebug(true)//是否是Debug模式
                .setBaseUrl("http://www.cretinzp.com/system/versioninfo")//当dataSourceType为DATA_SOURCE_TYPE_URL时，配置此接口用于获取更新信息
                .setMethodType(TypeConfig.METHOD_GET)//当dataSourceType为DATA_SOURCE_TYPE_URL时，设置请求的方法
                .setDataSourceType(TypeConfig.DATA_SOURCE_TYPE_URL)//设置获取更新信息的方式
                .setShowNotification(true)//配置更新的过程中是否在通知栏显示进度
                .setNotificationIconRes(R.mipmap.download_icon)//配置通知栏显示的图标
                .setUiThemeType(TypeConfig.UI_THEME_AUTO)//配置UI的样式，一种有12种样式可供选择
                .setRequestHeaders(null)//当dataSourceType为DATA_SOURCE_TYPE_URL时，设置请求的请求头
                .setRequestParams(null)//当dataSourceType为DATA_SOURCE_TYPE_URL时，设置请求的请求参数
                .setAutoDownloadBackground(false)//是否需要后台静默下载，如果设置为true，则调用checkUpdate方法之后会直接下载安装，不会弹出更新页面。当你选择UI样式为TypeConfig.UI_THEME_CUSTOM，静默安装失效，您需要在自定义的Activity中自主实现静默下载，使用这种方式的时候建议setShowNotification(false)，这样基本上用户就会对下载无感知了
//                .setCustomActivityClass(CustomActivity.class)//如果你选择的UI样式为TypeConfig.UI_THEME_CUSTOM，那么你需要自定义一个Activity继承自RootActivity，并参照demo实现功能，在此处填写自定义Activity的class
                .setNeedFileMD5Check(false)//是否需要进行文件的MD5检验，如果开启需要提供文件本身正确的MD5校验码，DEMO中提供了获取文件MD5检验码的工具页面，也提供了加密工具类Md5Utils
                .setCustomDownloadConnectionCreator(new OkHttp3Connection.Creator(builder))//如果你想使用okhttp作为下载的载体，可以使用如下代码创建一个OkHttpClient，并使用demo中提供的OkHttp3Connection构建一个ConnectionCreator传入，在这里可以配置信任所有的证书，可解决根证书不被信任导致无法下载apk的问题
                .setModelClass(new UpdateModel());
        //初始化
        AppUpdateUtils.init(this, updateConfig);

        //内存泄漏检测
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this);
//        }

        handleSSLHandshake();
    }

    /**
     * Enables https connections     * https://www.itstrike.cn/Question/13b0aa2b-ce37-4243-a488-330980408a64.html
     */
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {

                        }
                    }};
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(LoginActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }

    public static void initTTS(Context context) {
        //实例化自带语音对象
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {
                    // Toast.makeText(MainActivity.this,"成功输出语音",
                    // Toast.LENGTH_SHORT).show();
                    // Locale loc1=new Locale("us");
                    // Locale loc2=new Locale("china");

                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速

                    //判断是否支持下面两种语言
                    int result1 = textToSpeech.setLanguage(Locale.US);
                    int result2 = textToSpeech.setLanguage(Locale.
                            SIMPLIFIED_CHINESE);
                    boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
                    boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);

                    Log.i("zhh_tts", "US支持否？--》" + a +
                            "\nzh-CN支持否》--》" + b);

                }

            }
        });
    }

    /**
     * 获取全局Context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 格式化输出JSON字符串
     *
     * @return 格式化后的JSON字符串
     */
    public static String toPrettyFormat(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    public static <T> ArrayList<T> getObjectList(String jsonString, Class<T> cls) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 重写此方法
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 主要是添加下面这句代码
        MultiDex.install(this);
    }

    //打开钱箱
    public void openDraw() {
        Intent intent = new Intent();
        // 注意在 Android 5.0以后，不能通过隐式 Intent 启动 service，必须制定包名
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        context.bindService(intent, new SunConnection(), Context.BIND_AUTO_CREATE);
//        startService(intent);
    }

    public synchronized static AppApplication getInstance() {
        if (null == instance) {
            instance = new AppApplication();
        }
        return instance;
    }

    public static int getStrCunt(String mainStr, String subStr) {
        // 声明一个要返回的变量
        int count = 0;
        // 声明一个初始的下标，从初始位置开始查找
        int index = 0;
        // 获取主数据的长度
        int mainStrLength = mainStr.length();
        // 获取要查找的数据长度
        int subStrLength = subStr.length();
        // 如果要查找的数据长度大于主数据的长度则返回0
        if (subStrLength > mainStrLength) {
            return 0;
        }
        // 循环使用indexOf查找出现的下标，如果出现一次则count++
        while ((index = mainStr.indexOf(subStr, index)) != -1) {
            count++;
            // 从找到的位置下标加上要查找的字符串长度，让指针往后移动继续查找
            index += subStrLength;
        }
        return count;
    }
}

