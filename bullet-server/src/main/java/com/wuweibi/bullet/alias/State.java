package com.wuweibi.bullet.alias;


import com.wuweibi.bullet.annotation.Text;

/**
 * 状态码代码
 * 
 * 通过@Text 注解 作为注释
 * （@Text 注解用于生成国际化内容）
 * 说明：
 *    000000 操作成功
 *    100000 通用消息
 *    110000 定制消息
 * 
 * 
 * @author marker
 * @version 1.0
 */
public interface State {
	

	@Text("操作成功")
	int OperationSuccess = 000000;
	
	@Text("操作失败")
	int OperationFieldError = 000001;


	@Text("表单错误")
	int FormFieldError = 000002;
	
	@Text("操作失败")
	int OperationError = 100500;
	
	@Text("邮箱格式错误")
	int RegEmailError = 100001;
	
	@Text("邮箱已经被注册")
	int RegEmailExist = 100002;
	
	@Text("请输入内容")
	int MustFillInput = 100003;
	
	@Text("两次密码输入不一致")
	int PasswordInputNotEquals = 100004;

	@Text("邮箱为空")
	int EmailEmpty =100005;
	
	@Text("激活码失效")
	int ActivateCodeInvalid =100006;
	
	
	@Text("验证码错误")
	int ValidateCodeError = 100007;
	
	@Text("异常登录")
	int AbnormalLoginError = 100008;
	
	@Text("账号或者密码错误")
	int AccountOrPassError = 100009;
	

	@Text("异常注册")
	int AbnormalRegisterError = 100010;

	
	@Text("您还没有登录哦")
	int UserNotLoginError = 100011;
	
	@Text("请输入内容")
	int PleseInputContent = 100012;


	@Text("邮箱账号不存在")
	int RegEmailNotExist = 100013;



	@Text("code失效了哦")
	int CodeInvalid = 100014;


	/**
	 * 设备
	 * */

	@Text("设备不在线！")
	int DeviceNotOnline = 200000;

    @Text("设备已经被其他账号绑定！")
	int DeviceIdBinded = 200001;
	@Text("请输入设备码！")
	int DeviceNotInput = 200002;



    @Text("域名已经被使用！")
    int DomainIsUsed = 200100;




}
