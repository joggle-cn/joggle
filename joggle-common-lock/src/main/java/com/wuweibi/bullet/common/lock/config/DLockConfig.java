package com.wuweibi.bullet.common.lock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置更新后自动刷新到配置对象
 *
 * Created by kl on 2017/12/29.
 */
@ConfigurationProperties(prefix = DLockConfig.PREFIX)
public class DLockConfig {

    public static final String PREFIX = "spring.dlock";
    //redisson
    private String host;
    private String password;
    private int database=15;
    private int port = 3306;
    private ClusterServer clusterServer;

    private String codec = "org.redisson.codec.JsonJacksonCodec";
    //lock
    private long waitTime = 60;
    private long leaseTime = 60;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public ClusterServer getClusterServer() {
        return clusterServer;
    }

    public void setClusterServer(ClusterServer clusterServer) {
        this.clusterServer = clusterServer;
    }

    public static class ClusterServer{

        private String[] nodeAddresses;

        public String[] getNodeAddresses() {
            return nodeAddresses;
        }

        public void setNodeAddresses(String[] nodeAddresses) {
            this.nodeAddresses = nodeAddresses;
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
