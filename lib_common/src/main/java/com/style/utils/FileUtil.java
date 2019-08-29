package com.style.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件相关工具类
 * Created by xiajun on 2017/1/9.
 */
public class FileUtil {

    public static boolean isNewFileCanWrite(File file) {
        try {
            //父文件夹不存在时
            if (!file.getParentFile().exists()) {
                boolean mkdirs = file.getParentFile().mkdirs();
                return mkdirs && file.createNewFile() && file.canWrite();
            }
            //父文件夹存在时且文件存在时
            if (file.exists()) {
                return file.delete() && file.createNewFile() && file.canWrite();
            }
            //父文件夹存在时但文件不存在时
            return file.createNewFile() && file.canWrite();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean copyFile(File fromFile, String targetPath) {
        boolean isFile = fromFile.isFile();
        boolean canRead = fromFile.canRead();
        if (!isFile || !canRead)
            return false;
        File copeFile = new File(targetPath);
        if (!isNewFileCanWrite(copeFile)) {
            return false;
        }
        FileInputStream fosfrom = null;
        FileOutputStream fosto = null;
        try {
            fosfrom = new FileInputStream(fromFile);
            fosto = new FileOutputStream(copeFile);
            byte b[] = new byte[1024];
            int len;
            while ((len = fosfrom.read(b)) > 0) {
                fosto.write(b, 0, len); // 将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if (fosfrom != null)
                    fosfrom.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fosto != null)
                    fosto.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static void delete(String dir, String name) {
        delete(dir + "/" + name);
    }

    public static void delete(String path) {
        delete(new File(path));
    }

    public static void delete(File file) {
        if (file.isFile()) {
            deleteFileSafely(file);// file.delete();
        }
        if (file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                delete(childFile); // 递规的方式删除文件夹
            }
            deleteFileSafely(file);// file.delete();// 删除目录本身
        }
    }

    /**
     * 安全删除文件.
     *
     * @param file
     * @return
     */
    public static boolean deleteFileSafely(File file) {
        if (file != null) {
            String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
            File tmp = new File(tmpPath);
            file.renameTo(tmp);
            return tmp.delete();
        }
        return false;
    }


    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ImageColumns.DATA}, null, null,
                    null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static File UriToFile(Context context, Uri uri) {
        String res = UriToRealFilePath(context, uri);
        File file = new File(res);
        return file;
    }

    public static String UriToRealFilePath(Context context, Uri uri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        Log.e("uritopath", res);
        return res;
    }
}
