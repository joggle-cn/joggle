package com.wuweibi.bullet.business;

import com.wuweibi.bullet.domain2.entity.UserDomain;
import org.shredzone.acme4j.exception.AcmeException;

import java.io.IOException;

public interface UserDomainCertBiz {


    /**
     * 启动定时任务，定时检查证书是否过期，过期后自动续期
     */
    void startCertReNewTask() ;


    /**
     * 申请域名证书
     * @param userDomain
     * @throws IOException
     * @throws AcmeException
     */
    public boolean reqDomainCert( UserDomain userDomain) throws IOException, AcmeException ;
}
