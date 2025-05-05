package com.wuweibi.bullet.protocol.consts;


import org.apache.commons.lang3.ArrayUtils;

/**
 * 协议类型
 *
 * @author marker
 */
public class ProtocolType {


    public static final int PROTOCOL_HTTP  = 1;
    public static final int PROTOCOL_TCP   = 2;
    public static final int PROTOCOL_HTTPS = 3;
    public static final int PROTOCOL_HTTP_HTTPS = 4;
    public static final int PROTOCOL_UDP   = 5;


    public static boolean isPort(Integer protocol) {
        return ArrayUtils.contains(new Integer[]{PROTOCOL_TCP, PROTOCOL_UDP}, protocol);
    }

    public static UserPackageLimitEnum toPackageEnum(Integer resourceType) {
        switch (resourceType) {
            case PROTOCOL_HTTP:
            case PROTOCOL_HTTPS:
            case PROTOCOL_HTTP_HTTPS:
                return UserPackageLimitEnum.DomainNum;
            case PROTOCOL_TCP:
            case PROTOCOL_UDP:
                return UserPackageLimitEnum.PortNum;
        }
        return null;
    }
}
