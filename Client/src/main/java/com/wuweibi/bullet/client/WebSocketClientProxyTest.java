package com.wuweibi.bullet.client;

/**
 * Created by marker on 2017/11/20.
 */

import com.wuweibi.bullet.ByteUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author marker
 * @create 2017-11-20 下午1:12
 **/
public class WebSocketClientProxyTest implements WebSocketClientProxy {

    /** socket */
    private volatile Socket socket;


    public WebSocketClientProxyTest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void send(byte[] bytes) {
        System.out.println("==============================");

        OutputStream os = null;
        try {

            byte[] timeBytes = new byte[8];
            System.arraycopy(bytes, 0, timeBytes, 0, 8);
            long time = ByteUtils.bytes2Long(timeBytes);
            int size  = bytes.length - 8;
            byte[] dst = new byte[size];
            System.arraycopy(bytes, 8, dst, 0, size);


            os = socket.getOutputStream();

            os.write(dst);
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            IOUtils.closeQuietly(os);
            try {
//                socket.isConnected()
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
