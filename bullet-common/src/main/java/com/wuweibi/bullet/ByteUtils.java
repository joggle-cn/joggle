package com.wuweibi.bullet;
/**
 * Created by marker on 2017/11/20.
 */

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author marker
 * @create 2017-11-20 下午12:58
 **/
public final class ByteUtils {




    public static long bytes2Long(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }


    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }


    /**
     * InputStream 转byte数组
     *
     * @param inStream 输入流
     * @return
     * @throws IOException
     */
    public static byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int size = 1024;
        byte[] buffer = new byte[size];
        int len = -1;
        while((len = inStream.read(buffer,0, size)) > -1){
            outStream.write(buffer,0, len);
            if (len != size) {
                break;
            }
        }
        return outStream.toByteArray();
    }


    public static byte[] input2byte2(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        BufferedInputStream dis = new BufferedInputStream(inStream);


        byte[] bs = new byte[512];
        while(dis.available() > 512) {
            dis.read(bs);
//            ByteBuffer src = ByteBuffer.wrap(bs);
            // write data to client socket channel
            outStream.write(bs);
            Arrays.fill(bs, (byte)0);
        }

        // 处理不足512的剩余部分
        int remain = dis.available();
        byte[] last = new byte[remain];
        dis.read(last);
        outStream.write(last);

        return outStream.toByteArray();
    }






}
