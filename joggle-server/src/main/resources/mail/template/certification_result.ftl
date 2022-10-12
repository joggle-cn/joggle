<!DOCTYPE html>
<html>
<head>
    <title>Joggle实名认证结果</title>
    <meta charset="utf-8">
</head>
<body style="font-family: 'Microsoft YaHei'">

    <div style="border: 1px solid #ccc; width: 670px; height: 539px; margin: 0 auto;">
        <div style="border-bottom: 1px dashed #ccc; margin: 45px 50px;">
            <h3 style=" color: #676767;display: inline-block">尊敬的用户，感谢您的信任。</h3>
        </div>
        <div style="width: 500px; margin: 0 auto;">
            您的实名认证审核状态：
            <#if result == 1> <b style="color: #029a5b;">通过</b></#if>
            <#if result == 2> <b style="color: #029a5b;">不通过</b></#if>
            <#if result != 1>
                <p>失败原因：<b style="color: #ad1766">${resultMsg!}</b></p>
            </#if>
        </div>

        <div style=" margin:0 auto; width: 260px; margin-top: 50px;">
            <a href="${url!}" style="display:inline-block; height:35px; line-height: 35px; text-decoration: none; font-size: 16px;color: #ffffff; text-align: center; width: 260px; background: #3ab94f;">开始使用Joggle</a>
        </div>
        </div>
    </div>
</body>
</html>