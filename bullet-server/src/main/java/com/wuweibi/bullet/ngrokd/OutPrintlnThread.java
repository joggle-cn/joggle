package com.wuweibi.bullet.ngrokd;
/**
 * Created by marker on 2019/4/10.
 */

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.wuweibi.bullet.ConfigUtils.getClientProjectPath;

/**
 * 输出控制台的线程
 *
 * @author marker
 * @create 2020-02-27 12:50
 **/
@Slf4j
public class OutPrintlnThread extends Thread  {



    InputStream inputStream;
    /**
     * 构造
     */
    public OutPrintlnThread( ){

    }

    public OutPrintlnThread(InputStream inputStream) {
        this.inputStream = inputStream;
    }


    @Override
    public void run() {
        try {
            //用一个读输出流类去读
            InputStreamReader isr = new InputStreamReader(inputStream);
            //用缓冲器读行
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            //直到读完为止
            while((line=br.readLine())!=null)
            {
                System.out.println(line);
            }
        } catch (IOException e) {
            log.error("", e);
        }

    }

}
