package com.fenghuang.component_base.net;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Create by wangchao on 2017/12/25 13:50
 * 文件上传
 */
public class UploadManager {

    public static MultipartBody fileToMultipartBody(String key ,File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        builder.addFormDataPart(key, file.getName(), requestBody);

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    public static MultipartBody filesToMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (File file : files) {
            // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }
}
