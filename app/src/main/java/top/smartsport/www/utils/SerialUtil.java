package top.smartsport.www.utils;

import android.content.Context;

import org.xutils.view.annotation.ContentView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 2017/8/22.
 */

public class SerialUtil {
    private static Context ctx;

    public static void initCtx(Context ctx) {
        SerialUtil.ctx = ctx;
    }

    public static void saveObject(String name, Serializable sod){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = ctx.openFileOutput(name, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(sod);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }
    public static Object getObject( String name){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = ctx.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }
}
