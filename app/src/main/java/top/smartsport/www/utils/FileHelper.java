package top.smartsport.www.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bajie on 2015/6/15.
 */
public class FileHelper {

    /** 缓存路径，没有设为常量，在应用初始化的时候，根据具体应用做相应的目录变更 */
    public static String PATH_CACHE = Environment.getExternalStorageDirectory()
            .getPath() + "/%s/cache/";

    /** 下载路径，没有设为常量，在应用初始化的时候，根据具体应用做相应的目录变更 */
    public static String PATH_DOWNLOAD = Environment
            .getExternalStorageDirectory().getPath() + "/%s/DOWNLOAD/";
    public static String PATH_BASE = "smartsport/";
    /**
     * 拍照路径
     */

    public static String PATH_PHOTOGRAPH = "smartsport/UserHead/";
    /**
     * 相册路径
     */
    public static String PATH_MEDIARECORDER = "smartsport/MediaRecorder/";


    /**
     * 路径初始化
     *
     * @param path
     */
    public static void initPath(String path) {
        PATH_CACHE = String.format(PATH_CACHE, path);
        PATH_DOWNLOAD = String.format(PATH_DOWNLOAD, path);
    }

    /**
     * 由指定的路径和文件名创建文件
     *
     * @param path
     * @param name
     * @return
     * @throws IOException
     */
    public static File createFile(String path, String name) throws IOException {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(path + name);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 根据指定的路径创建文件
     *
     * @param filepath
     * @return
     * @throws IOException
     */
    public static File createFile(String filepath) throws IOException {
        int last_seperate = filepath.lastIndexOf("/") + 1;
        String path = filepath.substring(0, last_seperate);
        String name = filepath.substring(last_seperate);
        return createFile(path, name);
    }

    /**
     * 获取文件名
     *
     * @return
     */
    public static String getFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault());
        Date cruDate = Calendar.getInstance().getTime();
        String strDate = sdf.format(cruDate);
        String fileName = strDate + ".jpg";
        return fileName;
    }

    /**
     * 图片基础路径
     *
     * @return
     */
    public static File getBaseFile(String filePath) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // 文件可用
            File f = new File(Environment.getExternalStorageDirectory(),
                    filePath);
            if (!f.exists())
                f.mkdirs();
            return f;
        } else {
            return null;
        }
        // C}

    }

    /**
     * 保存图片
     *
     * @param bitmap
     * @param fileName
     * @param baseFile
     */
    public static void saveBitmap(Bitmap bitmap, String fileName, File baseFile) {
        FileOutputStream bos = null;
        File imgFile = new File(baseFile, "/" + fileName);
        try {
            bos = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            Log.e("smile", "bitmap.getByteCount()  = " + bitmap.getByteCount());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除指定文件
     *
     * @param path
     * @param name
     */
    public static void deleteFile(String path, String name) {
        if (!fileExist(path, name)) {
            return;
        }
        File file = new File(path + name);
        file.delete();
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @param name
     * @return
     */
    public static boolean fileExist(String path, String name) {
        File file = new File(path + name);
        if (file.exists() & !file.isDirectory()) {
            return true;
        }
        return false;
    }

    /**
     * 拷贝文件
     *
     * @param srcPath
     * @param srcName
     * @param desPath
     * @param desName
     * @return boolean
     */
    public static boolean copyFile(String srcPath, String srcName,
                                   String desPath, String desName) {
        if (!fileExist(srcPath, srcName)) {
            return false;
        }

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File inFile = new File(srcPath, srcName);
            File outFile = new File(desPath, desName);

            if (!fileExist(desPath, desName)) {
                createFile(desPath, desName);
            }

            fis = new FileInputStream(inFile);
            bis = new BufferedInputStream(fis);

            fos = new FileOutputStream(outFile);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024 * 8];
            int len = -1;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            AppUtil.closeStream(bis);
            AppUtil.closeStream(fis);
            AppUtil.closeStream(bos);
            AppUtil.closeStream(fos);
        }
        return false;
    }

    /**
     * 保存bitmap成图片
     *
     * @param bitmap
     * @param name
     * @param path
     * @throws IOException
     */
    public static void saveBitmap(Bitmap bitmap, String name, String path)
            throws IOException {
        if (bitmap == null) {
            return;
        }
        File file = createFile(path, name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
        } finally {
            AppUtil.closeStream(fos);
        }
    }

    /**
     *
     * 保存bitmap成图片
     *
     * @param bitmap
     * @param name
     *            完整的文件名（带有路径）
     * @throws IOException
     */
    public static void saveBitmap(Bitmap bitmap, String name)
            throws IOException {
        if (bitmap == null) {
            return;
        }
        File f = new File(name);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(name);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
        } finally {
            AppUtil.closeStream(fos);
        }
    }

    /**
     * 写入文件
     *
     * @param dataPath
     * @param data
     * @throws IOException
     */
    public static synchronized void saveData(String dataPath, String data)
            throws IOException {
        File file = createFile(dataPath);
        RandomAccessFile raf = new RandomAccessFile(file, "rws");
        // raf.seek(0);
        byte[] byteds = data.getBytes();
        raf.setLength(byteds.length);
        raf.write(byteds);
        AppUtil.closeStream(raf);
    }

    /**
     * 读取文件内容，并以字符串形式返回（该方法去掉了注释：//和#）path或者file只要能获取绝对路径即可
     *
     * @param path
     * @param file
     * @throws IOException
     */
    public static String readFile(String path, String file) throws IOException {
        if (path == null)
            path = "";
        if (file == null)
            file = "";
        FileInputStream fis = new FileInputStream(path + file);
        return readFromInputStream(fis);

    }

    /**
     * 将输入流内容写入字符串
     *
     * @param is
     * @return
     * @throws IOException
     */
    private static String readFromInputStream(InputStream is)
            throws IOException {
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            inputReader = new InputStreamReader(is);
            bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.startsWith("//") || line.startsWith("#")) {
                    continue;
                }
                sb.append(line);
            }
        } finally {
            AppUtil.closeStream(bufReader);
            AppUtil.closeStream(inputReader);
            AppUtil.closeStream(is);
        }
        return sb.toString();
    }

    /**
     * SD卡是否可用（挂载）
     *
     * @return
     */
    public static boolean isSDCardMounted() {
		/*
		 * Environment.MEDIA_MOUNTED // sd卡在手机上正常使用状态
		 * Environment.MEDIA_UNMOUNTED // 用户手工到手机设置中卸载sd卡之后的状态
		 * Environment.MEDIA_REMOVED // 用户手动卸载，然后将sd卡从手机取出之后的状态
		 * Environment.MEDIA_BAD_REMOVAL // 用户未到手机设置中手动卸载sd卡，直接拨出之后的状态
		 * Environment.MEDIA_SHARED // 手机直接连接到电脑作为u盘使用之后的状态
		 * Environment.MEDIA_CHECKINGS // 手机正在扫描sd卡过程中的状态
		 */
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    @SuppressLint("NewApi")
    public static long calculateFilePathSize(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
            return 0l;
        }
        return file.getTotalSpace();
    }

    /**
     * 删除路径
     *
     * @param filePath
     */
    public static void clearFilePath(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            String[] children = file.list();
            if (children == null) {// 该目录没有子元素(空目录)
                file.delete();
                return;
            }
            for (String child : children) {
                clearFilePath(child);
            }
        }
    }


    /**
     * 序列化对象
     *
     * @param obj
     * @return
     */
    public static boolean saveObj(Object obj, String path) {
        ObjectOutputStream objOutput = null;
        try {
            objOutput = new ObjectOutputStream(new FileOutputStream(path));
            objOutput.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            AppUtil.closeStream(objOutput);
        }
        return true;
    }

    /**
     * 反序列化对象
     *
     * @param path
     * @return
     */
    public static Object readObj(String path) {
        Object result = null;
        ObjectInputStream objInput = null;
        try {
            objInput = new ObjectInputStream(new FileInputStream(path));
            result = objInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            AppUtil.closeStream(objInput);
        }
        return result;
    }
}
