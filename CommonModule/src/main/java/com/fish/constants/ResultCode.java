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
     * 期望就诊开始日期的前%s天的%s时后 不允许下单
     */
    VIP_CAN_NOT_ORDER(40001, "期望就诊开始日期的前%s天的%s时后 不允许下单"),
    /**
     * 订单已经取消
     */
    VIP_CANCELED(40002, "订单已经取消，不需要再取消"),
    /**
     * 当前状态不能取消
     */
    VIP_NOT_CANCEL(40003, "订单当前状态不允许取消"),
    /**
     * 陪诊服务已开始，不允许取消订单
     */
    VIP_NOT_CANCEL_BY_STARTED(40004, "陪诊服务已开始，不允许取消订单"),
    /**
     * 陪诊服务已开始，不允许取消订单
     */
    VIP_NOT_CANCEL_BY_END(40006, "陪诊服务已结束，不允许取消订单"),
    /**
     * VIP就诊日期开始前 {days}天的{hour}时 或者 开始陪诊服务后不允许取消订单
     */
    VIP_NOT_CANCEL_BY_STARTTIME(40005, "VIP就诊日期开始前%s天的%s时后不允许取消订单"),
    //C端提示
    VIP_ORDER_CANT_NOT_ORDER(40021, "VIP陪诊服务前%s天的%s时后 不允许下单"),

    /**
     * 无忧就诊日期开始前 {days}天的{hour}时 或者 开始陪诊服务后不允许取消订单
     */
    ACCOMPANY_NOT_CANCEL_BY_STARTTIME(50001, "无忧就诊日期开始前%s天的%s时后不允许取消订单"),
    ACCOMPANY_CANT_NOT_ORDER(50002, "就诊日期前%s天的%s时后 不允许下单"),
    /**
     * 陪诊服务已开始，不允许取消订单
     */
    ACCOMPANY_NOT_CANCEL_BY_STARTED(50003, "陪诊服务已开始，不允许取消订单"),
    //C端提示
    ACCOMPANY_ORDER_CANT_NOT_ORDER(50021, "无忧陪诊服务前%s天的%s时后 不允许下单"),


    TAKE_REPORT_NOT_CANCEL_ONE(60001, "可取报告时间  前%s分钟后不允许取消订单"),
    TAKE_REPORT_NOT_CANCEL_TWO(60002, "报告已寄出（订单完成）后 不允许取消订单"),
    //C端提示
    TAKE_REPORT_CANT_NOT_ORDER(60021, "代取报告服务前%s天的%s时后 不允许下单"),


    UPLOAD_FILE_NULL(70001, "请选择上传文件"),
    UPLOAD_FILE_OUT_SIZE(70002, "文件大小超出最大限制（%s）"),
    UPLOAD_FILE_ERROR(70003, "文件上传失败"),

    SMS_SEND_ERROR(80001, "短信发送失败"),
    SMS_SEND_MSGTYPE_NO_EXSIT(80002, "短信模板不存在"),

    INVALID_WECHAT_SEESION(90001, "亲，你的账号未登录，请先登录!", "{\"code\":\"90001\", \"msg\":\"亲，你的账号未登录，请先登录!\"}"),

    INVALID_WECHAT_UNBIND(90002, "未绑定openId请登陆绑定"),

    USER_NOT_FIND(90003, "亲，你的账号不存在，请先注册!"),

    USER_ACTIVE_FAIL(10086, "护士或医生激活失败"),

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
  
