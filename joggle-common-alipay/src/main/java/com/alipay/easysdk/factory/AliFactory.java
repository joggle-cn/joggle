package com.alipay.easysdk.factory;

import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.Context;
import com.alipay.easysdk.kms.aliyun.AliyunKMSClient;
import com.alipay.easysdk.kms.aliyun.AliyunKMSSigner;
import com.aliyun.tea.TeaModel;

public final class AliFactory {

    public static final String SDK_VERSION = "alipay-easysdk-java-2.2.2";
    private static Context context;

    public AliFactory() {
    }

    public static void setOptions(Config options) {
        try {
            context = new Context(options, "alipay-easysdk-java-2.2.2");
            if ("AliyunKMS".equals(context.getConfig("signProvider"))) {
                context.setSigner(new AliyunKMSSigner(new AliyunKMSClient(TeaModel.buildMap(options))));
            }

        } catch (Exception var2) {
            throw new RuntimeException(var2.getMessage(), var2);
        }
    }


    public static com.alipay.easysdk.payment.common.CommonExtClient Common() throws Exception {
        return new com.alipay.easysdk.payment.common.CommonExtClient(new com.alipay.easysdk.kernel.Client(AliFactory.context));
    }

}
