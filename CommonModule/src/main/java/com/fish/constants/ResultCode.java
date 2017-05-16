/**
 * ---------------------------------------------------------------------------
 * Copyright (c) 2016, 深圳市华康全景信息技术有限公司- All Rights Reserved.
 * Project Name:ma-common  
 * File Name:ResultCode.java  
 * Package Name:com.hk.ma.common.constants
 * Author   luolong
 * Date:2016年7月22日下午2:14:32
 * ---------------------------------------------------------------------------  
 */

package com.fish.constants;

import lombok.Getter;

/**
 * 响应状态码及对应消息
 */
@Getter
public enum ResultCode {

    ERROR(1000, "服务器开小差了，请稍后重试", "{\"code\":\"1000\", \"msg\":\"服务器开小差了，请稍后重试!\"}"),
    PARAMETER_ERROR(1001, "参数异常", "{\"code\":\"1001\", \"msg\":\"参数异常!\"}"),
    DUBBO_SERVICE_ERROR(1002, "服务器开小差了，请稍后重试", "{\"code\":\"1001\", \"msg\":\"服务器开小差了，请稍后重试!\"}"),
    SUCCESS(2000, "请求成功"),
    TOKEN_INVALID(3000, "无效token,跳转到登录界面"),
    INVALID(4000, "数据校验失败"),
    INVALID_SEESION(5000, "session过期", "{\"code\":\"5000\", \"msg\":\"session过期!\"}"),

    LOGIN_INVALID_USER_NULL(10001, "账号或密码不能为空"),
    LOGIN_INVALID_VERICODE_NULL(10002, "图片验证码不能为空"),
    LOGIN_INVALID_VERICODE_ERROR(10003, "图片验证码输入错误"),

    /**
     * 订单已经取消
     */
    VIP_CANCELED(40002, "订单已经取消，不需要再取消"),
    /**
     * 当前状态不能取消
     */
    VIP_NOT_CANCEL(40003, "订单当前状态不允许取消"),

    UPLOAD_FILE_NULL(70001, "请选择上传文件"),
    UPLOAD_FILE_OUT_SIZE(70002, "文件大小超出最大限制（%s）"),
    UPLOAD_FILE_ERROR(70003, "文件上传失败"),

    SMS_SEND_ERROR(80001, "短信发送失败"),
    SMS_SEND_MSGTYPE_NO_EXSIT(80002, "短信模板不存在"),

    INVALID_WECHAT_SEESION(90001, "亲，你的账号未登录，请先登录!", "{\"code\":\"90001\", \"msg\":\"亲，你的账号未登录，请先登录!\"}"),

    INVALID_WECHAT_UNBIND(90002, "未绑定openId请登陆绑定"),

    USER_NOT_FIND(90003, "亲，你的账号不存在，请先注册!"),

    USER_IS_CLOSE(90005, "该用户已被锁定，暂不可登录!"),

    ACCOUNT_IS_VALID(90006, "无效的账号!"),


    ;

    private int val;
    private String desc;
    private String json;

    ResultCode(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    ResultCode(int val, String desc, String json) {
        this.val = val;
        this.desc = desc;
        this.json = json;
    }

    public String formatDesc(Object... objects) {
        return String.format(this.desc, objects);
    }

    public boolean isSuccess() {
        return this.val == 2000;
    }

    public boolean isNotSuccess() {
        return !isSuccess();
    }
}
  
