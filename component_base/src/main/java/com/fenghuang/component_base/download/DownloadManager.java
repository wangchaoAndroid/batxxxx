package com.fenghuang.component_base.download;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fenghuang.component_base.R;
import com.fenghuang.component_base.net.Api;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.ContextManager;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Create by wangchao on 2017/12/13 09:44
 * 下载管理类
 */
public class DownloadManager {
    private static volatile DownloadManager downloadManager;

    private static final String DEFAULT_TYPE = "apk";

    public final static String download_Dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/battery_temp/";
    public final static String download_file_name = "battery_" + ContextManager.getAppContext().getPackageName() +".apk";

    public static final String TYPE_APK = "apk";
    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_VIDEO = "video";

    private String type ;

    public static DownloadManager getInstance(){
        if(downloadManager == null){
            synchronized (DownloadManager.class){
                if(downloadManager == null){
                    downloadManager = new DownloadManager();
                }
            }
        }
        return downloadManager;
    }


    /**
     * 获取更新信息
     * @param versionName
     * @param callback
     */
    public void upgradeInfo(String versionName, final ResponseCallback<BaseEntery<ApkUpgradeInfo>> callback){
        UploadLogManager.getInstance().upgradeInfo(versionName,callback);
    }


    /**
     * 下载更新apk
     * @param url
     * @param file
     * @param callback
     */
    public void download(String url , final File file , final Callback callback) {
        download(url,file,DEFAULT_TYPE,callback);
    }


    /**
     * @param url  下载url
     * @param file  下载的文件
     * @param type      下载类型
     * @param callback  下载进度的回调
     */
    public void download(String url , final File file , String type, final Callback callback){
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Api.baseurl);
        OkHttpClient.Builder builder = ProgressHelper.addProgress(new ProgressListener() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
                //
                if(callback != null){
                    callback.onProgress((int) ((100 * progress) / total));
                }
                if(file.length() == total){
                    installApk(file.getAbsolutePath());
                }

            }
        });
        DownloadApi downloadApi = retrofitBuilder
                .client(builder.build())
                .build().create(DownloadApi.class);
        downloadApi.retrofitDownload(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@NonNull ResponseBody responseBody) throws Exception {
                        InputStream inputStream = responseBody.byteStream();
                        StreamUtil.converyStream2File(inputStream,file);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);

    }

    private void installApk(String filePath){
            // TODO Auto-generated method stub
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        ContextManager.getAppContext().startActivity(intent);
    }

    public void upgrade(final Context context, final boolean auto) {
        DownloadManager.getInstance().upgradeInfo(IAppUtil.getVersionName(ContextManager.getAppContext()),  new ResponseCallback<BaseEntery<ApkUpgradeInfo>>() {

                    @Override
                    public void onSuccess(BaseEntery<ApkUpgradeInfo> value) {
                        mUpgradeInfo = value.obj;
                        showUpgradeDialog(context);

                    }

                    @Override
                    public void onFailture(String e) {
                        if(!auto){
                            RxToast.showToast(e + "");
                        }

                    }
                });


    }

    private Dialog mDialog;
    private TextView mSure,version_name;
    private ImageView mCancel;
    private ProgressBar mPsb;
    private TextView mTip;
    public ApkUpgradeInfo  mUpgradeInfo;
    public void showUpgradeDialog(Context context) {
        if(mUpgradeInfo == null) return;
        mDialog = new Dialog(context, R.style.my_dialog);
        View dialogView = LayoutInflater.from(ContextManager.getAppContext()).inflate(R.layout.layout_upgrade_dialog, null);
        mSure = dialogView.findViewById(R.id.sure_txt);
        version_name = dialogView.findViewById(R.id.version_name);
        mTip = dialogView.findViewById(R.id.tip_txt);
        mCancel = dialogView.findViewById(R.id.cancle_txt);
        mPsb = dialogView.findViewById(R.id.psb);
        mPsb.setVisibility(View.GONE);
        version_name.setText("v " +mUpgradeInfo.appVersion);
        mTip.setText(R.string.upgrade_tip);
        mDialog.setContentView(dialogView);
        WindowManager.LayoutParams attributes = mDialog.getWindow().getAttributes();
        attributes.width = (int) (IDisplayUtil.getScreenWidth() * 0.7);
        mDialog.getWindow().setAttributes(attributes);

        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mDialog.isShowing() && mUpgradeInfo != null) {
                        String updateUrl = mUpgradeInfo.updateUrl;
                        mPsb.setVisibility(View.VISIBLE);
                        mCancel.setVisibility(View.GONE);
                        mSure.setVisibility(View.GONE);
                        mTip.setText(R.string.upgrade_loading);

                        File file = new File(download_Dir,download_file_name );
                        if(!file.getParentFile().exists()){

                            boolean mkdirs = file.getParentFile().mkdirs();
                            ILog.e("mkdirs",mkdirs +"---" +  file.getParentFile().getAbsolutePath());
                        }
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        DownloadManager.getInstance().download(updateUrl, file, new Callback() {
                            @Override
                            public void onProgress(int progress) {
                                //下载的进度
                                ILog.e("progress" ,progress +"");
                                mPsb.setProgress(progress);
                                if (progress == 100) {
                                    mDialog.dismiss();
                                    mUpgradeInfo = null;
                                }
                            }

                            @Override
                            public void onFail(String msg) {
                                if (mDialog.isShowing()) {
                                    mDialog.dismiss();
                                    mUpgradeInfo = null;
                                }

                                RxToast.showToast(ContextManager.getAppContext().getString(R.string.no_network_to_remind));
                            }

                        });
                    }
                }catch (Exception e){
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mUpgradeInfo = null;
                    RxToast.error(ContextManager.getAppContext().getResources().getString(R.string.upgrade_fail));
                }

            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                mUpgradeInfo = null;
            }
        });
        mDialog.show();
    }


}
