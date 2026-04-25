package com.wuweibi.bullet.protocol.consts;

/**
 * 资源类型 1域名 2端口 3流量 4 充值 5套餐
 * @author marker
 */
public class ResourceType {

    public static final int DOMAIN = 1;  // 域名
    public static final int PORT = 2;    // 端口
    public static final int TRAFFICE = 3; // 流量
    public static final int RECHARGE = 4; // 充值
    public static final int PACKAGE = 5; // 套餐

    public static UserPackageLimitEnum toPackageEnum(Integer resourceType) {
        switch (resourceType) {
            case 1:
                return UserPackageLimitEnum.DomainNum;
            case 2:
                return UserPackageLimitEnum.PortNum;
        }
        return null;
    }
}
