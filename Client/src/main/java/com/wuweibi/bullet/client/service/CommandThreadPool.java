package com.wuweibi.bullet.client.service;
/**
 * Created by marker on 2019/4/10.
 */

import com.wuweibi.bullet.client.threads.CommandThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 *
 * @author marker
 * @create 2019-04-10 15:48
 **/
@Service
@Slf4j
public class CommandThreadPool {


    /**
     * 池
     */
    volatile static Map<Long, CommandThread> cache = Collections.synchronizedMap(new HashMap<>());


    /**
     * 推线程进池
     * @param commandThread
     */
    public void push(CommandThread commandThread) {
        cache.put(commandThread.getMappingId(), commandThread);

        // 启动
        commandThread.start();
    }

    /**
     * 包含ID
     * @param mappingId 包含ID
     * @return
     */
    public boolean containsId(Long mappingId) {
        return cache.containsKey(mappingId);
    }

    /**
     * 杀死线程
     * @param mappingId
     */
    public void killThread(Long mappingId) {
        if(cache.containsKey(mappingId)){
            CommandThread commandThread = cache.get(mappingId);

            log.debug("killing thread {}", mappingId);
            commandThread.interrupt();

        }
    }
}
