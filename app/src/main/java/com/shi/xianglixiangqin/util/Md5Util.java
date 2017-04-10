package com.shi.xianglixiangqin.util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by  on 2017/4/1 10:35
 */

public class Md5Util {


    //获取文件的Md5
//    public static String getMd5ByFile(File file) throws FileNotFoundException {
//        String value = null;
//        FileInputStream in = new FileInputStream(file);
//        try {
//            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(byteBuffer);
//            BigInteger bi = new BigInteger(1, md5.digest());
//            value = bi.toString(16);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (null != in) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return value;
//    }

    //获取文件的Md5
    public static String getMd5ByFile(File file) {
        MessageDigest digest = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];

        try {
            if (!file.isFile()) {
                return "";
            }

            digest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);

            while (true) {
                int len;
                if ((len = fis.read(buffer, 0, 1024)) == -1) {
                    fis.close();
                    break;
                }

                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        BigInteger var5 = new BigInteger(1, digest.digest());
        return String.format("%1$032x", new Object[]{var5}).toUpperCase();
    }


    //获取文字的MD5
    public static String getMd5ByText(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString().toUpperCase();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

}
