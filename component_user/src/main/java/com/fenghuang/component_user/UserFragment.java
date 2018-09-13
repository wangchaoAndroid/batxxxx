package com.fenghuang.component_user;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.util.Range;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.helper.GlideCircleTransform;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ParamSignUtils;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.ActivityStackManager;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.component_user.bean.AvatarModel;
import com.fenghuang.component_user.login.LoginActivity;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_CANCELED;


/**
 * @author billy.qi
 * @since 17/12/8 15:30
 */
public class UserFragment extends LazyLoadFragment {
    //相机拍摄的头像文件(本次演示存放在SD卡根目录下)
    private static final File USER_ICON = new File(Environment.getExternalStorageDirectory(), "user_icon.jpg");
    //请求识别码(分别为本地相册、相机、图片裁剪)
    private static final int CODE_PHOTO_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_PHOTO_CLIP = 3;

    File file;
    String path, saveName;
    // 照片保存路径
    File cameraFile;

    private TextView tv_name,setting_tv;
    private ImageView avatar;
    NetServices mNetServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    private LoginModel loginModel;
    private Dialog dialog;

    @Override
    protected void init(View view,Bundle savedInstanceState) {

        tv_name = view.findViewById(R.id.tv_name);
        setting_tv = view.findViewById(R.id.setting_tv);
        avatar = view.findViewById(R.id.avatar);

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_user;
    }

    @Override
    protected void lazyLoad() {
        getNetUserInfo();

        addOnClickListeners(R.id.setting_tv,R.id.warn_info,R.id.feed_back,R.id.avatar);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        int id = view.getId();
        if(id == R.id.setting_tv){
            getActivity().startActivityForResult(new Intent(getActivity(),SettingActivity.class),11);
        }else if(id == R.id.warn_info){
            if(toLoginForToken()){
                CC.obtainBuilder("component_battery")
                        .setActionName("getWarnInfo")
                        .build()
                        .call();
            }

        }else if(id == R.id.feed_back){
            if(toLoginForToken()){
                startActivity(new Intent(getActivity(),FeedBackActivity.class));
            }
        }else if(id == R.id.avatar){

            String token1 = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
            ILog.e("1111","111111111111111" + token1 + loginModel) ;
            if(TextUtils.isEmpty(token1)){
                startActivityForResult(new Intent(getActivity(),LoginActivity.class),9);
            }else {
                if(loginModel != null){
                    String headPortrait = loginModel.headPortrait;
                    if(TextUtils.isEmpty(headPortrait)){
                        //弹出上传头像的dialog
                        openDialog();
                    }
                }
            }

        }
    }




    public boolean toLoginForToken(){
        String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        if(TextUtils.isEmpty(token)){
            CCResult ccResult = CC.obtainBuilder("component_user")
                    .setContext(getActivity())
                    .setActionName("toLoginActivityForToken")
                    .build()
                    .call();
            String data = ccResult.getDataItem(SPDataSource.USER_TOKEN);
            if(!TextUtils.isEmpty(data)){
                getNetUserInfo();
                return true;
            }
            return false;

        }
        return true;
    }


    public void getNetUserInfo(){
        String token = UserManager.getToken();
        if(TextUtils.isEmpty(token)){
            return;
        }
        mNetServices.getUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<LoginModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<LoginModel> value) {
                        loginModel = value.obj;
                        if(loginModel != null){
                            tv_name.setText(loginModel.nickName + "");
                            String headPortrait = loginModel.headPortrait;
                            if(!TextUtils.isEmpty(headPortrait)){
                                Glide.with(getActivity())
                                        .load(headPortrait)
                                        .transform(new GlideCircleTransform(getActivity())).into(avatar);
                            }
                        }

                    }

                    @Override
                    public void onFailture(String e) {

                    }
                });

    }

    private void openDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_head, null);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button toGrary = view.findViewById(R.id.toGrary);
        Button toCamera = view.findViewById(R.id.toCamera);
        dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                                         ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
            window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
    // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        toGrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicFromLocal();
            }
        });
        toCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera();
            }
        });
    }


    /**
     * 图片裁剪
     *
     * @param uri
     */
    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
         /*创建一个指向需要操作文件（filename）的文件流。（可解决无法“加载问题”）*/
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        /*outputX outputY 是裁剪图片宽高
         *这里仅仅是头像展示，不建议将值设置过高
         * 否则超过binder机制的缓存大小的1M限制
         * 报TransactionTooLargeException
         */
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_PHOTO_CLIP);
    }
    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            try {
                Bitmap photo = extras.getParcelable("data");
                File file = new File(Environment.getExternalStorageDirectory(),"avatar.jpg");
                FileOutputStream out = new FileOutputStream(file);
                if (photo.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                    out.flush();
                    out.close();
                    // 上传单一文件
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                    MultipartBody.Part no = MultipartBody.Part.createFormData("token", token);
                    uploadImg(body,no);
                }
            }catch (Exception e){

            }


        }
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void uploadImg( MultipartBody.Part part ,   MultipartBody.Part no ){

        mNetServices.uploadImg(part,no).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<AvatarModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<AvatarModel> value) {
                        RxToast.showToast("设置成功");
                        Log.e("1111","vvvvvvvv");
                        AvatarModel avatarModel = value.obj;
                        if(avatarModel != null){
                            String headPortrait = avatarModel.headPortrait;
                            Glide.with(getActivity())
                                    .load(headPortrait)
                                    .transform(new GlideCircleTransform(getActivity())).into(avatar);
                        }

                    }

                    @Override
                    public void onFailture(String e) {
                        ILog.e("value uploadImg","error" + e);
                        //上传失败
                        RxToast.error(e + "");
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            //Toast.makeText(getActivity(), "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case CODE_CAMERA_REQUEST:
                if (USER_ICON.exists()) {
                    photoClip(Uri.fromFile(USER_ICON));
                }
                break;
            case CODE_PHOTO_REQUEST:
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case CODE_PHOTO_CLIP:
                if (data != null) {
                    setImageToHeadView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    // 相机
    private void camera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(USER_ICON));
        startActivityForResult(intent, CODE_CAMERA_REQUEST);

    }

    /**
     * 从本机相册获取图片
     */
    private void getPicFromLocal() {
        Intent intent = new Intent();
        // 获取本地相册方法一
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
        //获取本地相册方法二
        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, CODE_PHOTO_REQUEST);
    }

}
