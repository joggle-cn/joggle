package com.wuweibi.bullet.domain.vo;

import lombok.Data;

@Data
public class ReleaseDetail {

    private ReleaseInfo release; //
    private String download_url; //": "https://open.joggle.cn/ngrok/darwin_amd64/ngrok",
    private String checksum; //":    "",
    private String signature; //":   "",
    private boolean available; //": true,
    private String patch_type; //":       ""
}
