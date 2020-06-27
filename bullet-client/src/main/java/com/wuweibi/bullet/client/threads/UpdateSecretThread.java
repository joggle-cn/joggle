package com.wuweibi.bullet.client.threads;
/**
 * Created by marker on 2019/4/10.
 */

import com.wuweibi.bullet.client.domain.NgrokConf;
import com.wuweibi.bullet.utils.FileTools;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.wuweibi.bullet.client.utils.ConfigUtils.getClientProjectPath;

/**
 *
 * 更新设备秘钥的
 *
 * @author marker
 * @create 2019-04-10 12:50
 **/
@Slf4j
public class UpdateSecretThread extends Thread  {

    private String secret;


    /**
     * 构造
     * @param secret
     */
    public UpdateSecretThread(String secret){
        this.secret = secret;

    }


    @Override
    public void run() {
        // 解析为命令
        String projectPath = getClientProjectPath();
        // 生成文件目录
        String configPath = projectPath + "/conf/domain/";
        String logsPath   = projectPath + "/logs/domain/";
        new File(configPath).mkdirs();
        new File(logsPath).mkdirs();


        Yaml yaml = new Yaml();

        String configYmlFilePath = projectPath + "/conf/ngrok.yml";
        File configYmlFile = new File(configYmlFilePath);

        String ymlText = "";
        try {
            ymlText = FileTools.getFileContet(configYmlFile, FileTools.FILE_CHARACTER_UTF8);
        } catch (IOException e) {
            log.error("", e);
        }

        NgrokConf testEntity = yaml.loadAs(ymlText, NgrokConf.class);//如果读入Map,这里可以是Mapj接口,默认实现为LinkedHashMap
        testEntity.setAuth_token(secret);

        try {
            yaml.dump(testEntity, new FileWriter(configYmlFile));
        } catch (IOException e) {
            log.error("", e);
        }

    }



}
