package com.pony.ninjarpcserver.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;

public class FileUtils {


    public static String readToString(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        try {
            StringBuilder stringBuffer = new StringBuilder();
            // 当External的可用状态为可用时
            // 打开文件输入流
            FileInputStream fileInputStream = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            int len = fileInputStream.read(buffer);
            // 读取文件内容
            while (len > 0) {
                stringBuffer.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
                // 继续把数据存放在buffer中
                len = fileInputStream.read(buffer);
            }
            // 关闭输入流
            fileInputStream.close();
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            VLog.e(e.getMessage());

        }
        return null;
    }

    //保存文件到sd卡
    public static void saveToFile(String targetPath, String content) {
        File file = new File(targetPath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            FileOutputStream fo = null;
            ReadableByteChannel src = null;
            FileChannel out = null;
            try {
                byte[] data = content.getBytes(StandardCharsets.UTF_8);
                src = Channels.newChannel(new ByteArrayInputStream(data));
                fo = new FileOutputStream(targetPath);
                out = fo.getChannel();
                out.transferFrom(src, 0, data.length);
            } finally {
                if (fo != null) {
                    fo.close();
                }
                if (src != null) {
                    src.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            VLog.e(e.getMessage());
        }
    }


    public static void writeFile(String path, byte[] bytes) {
        try {
            FileOutputStream out = new FileOutputStream(path);//指定写到哪个路径中
            FileChannel fileChannel = out.getChannel();
            fileChannel.write(ByteBuffer.wrap(bytes)); //将字节流写入文件中
            fileChannel.force(true);//强制刷新
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
            VLog.e(e.getMessage());
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    public static void saveLogToFile(String path, String content) {
        BufferedWriter out = null;
        //获取SD卡状态
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
            out.newLine();
            out.write(content);

        } catch (IOException e) {
            e.printStackTrace();
            VLog.e(e.getMessage());

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    VLog.e(e.getMessage());
                }
            }
        }
    }
}
