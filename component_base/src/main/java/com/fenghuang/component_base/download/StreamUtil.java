package com.fenghuang.component_base.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Create by wangchao on 2017/12/13 11:27
 */
public class StreamUtil {
    public static void converyStream2File(InputStream inputStream, File file){
        if(file ==null || inputStream == null){
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            int len = -1;
            byte[] buffer = new byte[1024 *8];
            while ((len = inputStream.read(buffer)) != -1){
                fos.write(buffer,0,len);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(inputStream != null)inputStream.close();
                if(fos != null)fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
