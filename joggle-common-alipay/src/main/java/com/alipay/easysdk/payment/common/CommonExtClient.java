package com.alipay.easysdk.payment.common;

import com.alipay.easysdk.payment.common.models.AlipayTradeCloseResponse;
import com.aliyun.tea.*;

import java.util.HashMap;
import java.util.Map;

public class CommonExtClient extends com.alipay.easysdk.payment.common.Client {


    public CommonExtClient(com.alipay.easysdk.kernel.Client kernel) throws Exception {
        super(kernel);
    }


    public AlipayTradeCloseResponse closeTradeNo(String tradeNo) throws Exception {
        Map<String, Object> runtime_ = TeaConverter.buildMap(new TeaPair[]{new TeaPair("ignoreSSL", this._kernel.getConfig("ignoreSSL")), new TeaPair("httpProxy", this._kernel.getConfig("httpProxy")), new TeaPair("connectTimeout", 15000), new TeaPair("readTimeout", 15000), new TeaPair("retry", TeaConverter.buildMap(new TeaPair[]{new TeaPair("maxAttempts", 0)}))});
        TeaRequest _lastRequest = null;
        long _now = System.currentTimeMillis();
        int _retryTimes = 0;

        while (Tea.allowRetry((Map) runtime_.get("retry"), _retryTimes, _now)) {
            if (_retryTimes > 0) {
                int backoffTime = Tea.getBackoffTime(runtime_.get("backoff"), _retryTimes);
                if (backoffTime > 0) {
                    Tea.sleep(backoffTime);
                }
            }

            ++_retryTimes;

            try {
                TeaRequest request_ = new TeaRequest();
                Map<String, String> systemParams = TeaConverter.buildMap(new TeaPair[]{new TeaPair("method", "alipay.trade.close"), new TeaPair("app_id", this._kernel.getConfig("appId")), new TeaPair("timestamp", this._kernel.getTimestamp()), new TeaPair("format", "json"), new TeaPair("version", "1.0"), new TeaPair("alipay_sdk", this._kernel.getSdkVersion()), new TeaPair("charset", "UTF-8"), new TeaPair("sign_type", this._kernel.getConfig("signType")), new TeaPair("app_cert_sn", this._kernel.getMerchantCertSN()), new TeaPair("alipay_root_cert_sn", this._kernel.getAlipayRootCertSN())});
                Map<String, Object> bizParams = TeaConverter.buildMap(new TeaPair[]{
                        new TeaPair("out_trade_no", tradeNo),
                });
                Map<String, String> textParams = new HashMap();
                request_.protocol = this._kernel.getConfig("protocol");
                request_.method = "POST";
                request_.pathname = "/gateway.do";
                request_.headers = TeaConverter.buildMap(new TeaPair[]{new TeaPair("host", this._kernel.getConfig("gatewayHost")), new TeaPair("content-type", "application/x-www-form-urlencoded;charset=utf-8")});
                request_.query = this._kernel.sortMap(TeaConverter.merge(String.class, new Map[]{TeaConverter.buildMap(new TeaPair[]{new TeaPair("sign", this._kernel.sign(systemParams, bizParams, textParams, this._kernel.getConfig("merchantPrivateKey")))}), systemParams, textParams}));
                request_.body = Tea.toReadable(this._kernel.toUrlEncodedRequestBody(bizParams));
                TeaResponse response_ = Tea.doAction(request_, runtime_);
                Map<String, Object> respMap = this._kernel.readAsJson(response_, "alipay.trade.close");
                if (this._kernel.isCertMode()) {
                    if (this._kernel.verify(respMap, this._kernel.extractAlipayPublicKey(this._kernel.getAlipayCertSN(respMap)))) {
                        return (AlipayTradeCloseResponse) TeaModel.toModel(this._kernel.toRespModel(respMap), new AlipayTradeCloseResponse());
                    }
                } else if (this._kernel.verify(respMap, this._kernel.getConfig("alipayPublicKey"))) {
                    return (AlipayTradeCloseResponse) TeaModel.toModel(this._kernel.toRespModel(respMap), new AlipayTradeCloseResponse());
                }

                throw new TeaException(TeaConverter.buildMap(new TeaPair[]{new TeaPair("message", "验签失败，请检查支付宝公钥设置是否正确。")}));
            } catch (Exception var13) {
                if (!Tea.isRetryable(var13)) {
                    throw new RuntimeException(var13);
                }
            }
        }

        throw new TeaUnretryableException((TeaRequest) _lastRequest);
    }
}
