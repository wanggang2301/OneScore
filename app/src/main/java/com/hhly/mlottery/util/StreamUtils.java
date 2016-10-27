package com.hhly.mlottery.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/10/13
 */
public class StreamUtils {

    /**
     * 关闭流
     *
     * @param c closeable
     */
    public static void close(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件
     *
     * @param from from
     * @param to   to
     */
    public static void copy(File from, File to) throws IOException {
        FileInputStream fis = new FileInputStream(from);
        FileOutputStream fos = new FileOutputStream(to);
        ByteArrayOutputStream baos = copyToByteArrayOutputStream(fis);
        fos.write(baos.toByteArray());
        close(fis);
        close(baos);
        close(fos);
    }

    /**
     * 拷贝流
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IOException
     */
    @SuppressWarnings("all")
    public static ByteArrayOutputStream copyToByteArrayOutputStream(InputStream in) throws IOException {
        int len;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        return out;
    }
}
