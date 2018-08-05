package com.wuweibi.bullet.tcp;/**
 * Created by marker on 2018/7/9.
 */

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author marker
 * @create 2018-07-09 21:53
 **/
public class Forwarder extends Thread {
    /** Socket 1 inputstream. */
    private InputStream is;
    /** Socket 2 outputstream. */
    private OutputStream os;


    public Forwarder(InputStream is, OutputStream os){
        this.is = is;
        this.os = os;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        try {
            while (true) {
                int bytesRead = is.read(buffer);
                if (bytesRead == -1) break; // End of stream is reached --> exit

                String add = new String(buffer, 0, bytesRead);
                System.out.println(is + ":" + add);


                os.write(buffer, 0, bytesRead);
                os.flush();
            }
        } catch (Exception e) {
            // Read/write failed --> connection is broken
        }
//    params.getUpObservers().forEach(TCPObserver::connectionBroken);
//    params.getDownObservers().forEach(TCPObserver::connectionBroken);
        //Notify the parent tunnel that the connection is broken
//        parent.connectionBroken();
    }
}
