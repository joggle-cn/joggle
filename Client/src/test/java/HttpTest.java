/**
 * Created by marker on 2017/12/6.
 */

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * @author marker
 * @create 2017-12-06 下午9:49
 **/
public class HttpTest {


    @Test
    public void test1() throws IOException {
        String data = "GET /users/sign_in HTTP/1.1\n" +
                "Host: test.bullet.cn:8083\n" +
                "Connection: close\n" +
                "Cache-Control: max-age=0\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8\n" +
                "Cookie: _gitlab_session=8267d13b0a9141861dc7e50e7c25a6e8\n\r\n";

        byte[] requestData = data.getBytes();

        Socket socket = new Socket("192.168.1.4", 8080);
        OutputStream writer = socket.getOutputStream();
        writer.write(requestData);
        writer.flush();
        //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
//            socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int headLength = 0;

        String line = null;

        int contentLength = 0;
        do {
            int tmp = -1;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            boolean hasData = false;
            while ((tmp = inputStream.read()) != -1){
                headLength++;
                if(tmp == '\n'){
                    hasData = true;
                    break;
                }
                os.write((byte)tmp);
            }
//            line = list.toArray(new Byte[]{});
            line = new String(os.toByteArray());
            if (line.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
            if(line.startsWith("Content-Encoding")){

            }

            System.out.println(line);
            if("\r".equals(line)){
                break;
            }


            if(!hasData){
                break;
            }
        } while (line != null);

        // 读取body阶段

        if( contentLength != 0){
            byte[] buffer = new byte[contentLength];

            int nIdx = 0;
            int nReadLen = 0;
            while (nIdx < contentLength)  {
                nReadLen = inputStream.read(buffer,   nIdx, contentLength - nIdx);
                if (nReadLen > 0)  {
                    nIdx = nIdx + nReadLen;
                } else  {
                    break;
                }
            }
            System.out.println("========================");
            System.out.println(new String(buffer));
        } else {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1 )  {
                outputStream.write( Arrays.copyOf(buffer, len));
            }

            System.out.println(new String(outputStream.toByteArray()));

        }

    }
}
