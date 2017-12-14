package com.wuweibi.bullet;
/**
 * Created by marker on 2017/11/20.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author marker
 * @create 2017-11-20 下午1:19
 **/
public class SocketUtils {

    private static Logger logger = LoggerFactory.getLogger(SocketUtils.class);


    /**
     * 输入流转换byte[]
     *
     * @param socket
     * @return
     * @throws IOException
     */
    public static byte[] getInputStreamBytes(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        return ByteUtils.input2byte(is);
    }


    /**
     * NIO 读取数据
     *
     * @param socketChannel
     * @return
     * @throws IOException
     */
    public static byte[] receiveData(SocketChannel socketChannel) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            byte[] bytes;
            int size = 0;
            int count = 0;
            while ((size = socketChannel.read(buffer)) >= 0) {
                buffer.flip();
                bytes = new byte[size];
                buffer.get(bytes);
                baos.write(bytes);
                buffer.clear();
                count++;
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            try {
//                socketChannel.shutdownInput();
//                socketChannel.close();

            } catch (Exception ex) {
            }
        }
        return baos.toByteArray();
    }


}
