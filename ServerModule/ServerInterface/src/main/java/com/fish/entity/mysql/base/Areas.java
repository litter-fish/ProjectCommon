package com.fish.entity.mysql.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Areas implements Serializable {
    /** 地区编号 自增 */
    private Integer areaId;

    /** 标准区域编码 */
    private String areaCode;

    /** 地区名称 */
    private String areaName;

    /** 地区名称全拼 */
    private String fullSpelling;

    /** 地区名称首字母简拼 */
    private String briefSpelling;

    /** 地区等级 0:省;1:市;2:区 */
    private Integer areaLevel;

    /** 所属上级地区编号 */
    private Integer parentId;

    /** 描述 */
    private String description;

    /** 热门城市 0:否;1:是 */
    private Boolean isHot;

    /** 排序编号 */
    private Integer sequence;

    /** 创建人用户id */
    private String createUserId;

    /** 创建时间 */
    private Date createDateTime;

    /** 最后修改人编号 */
    private String updateUserId;

    /** 最后修改人时间 */
    private Date updateDateTime;

    /** 是否删除 0:未删除;1:已删除 默认为0 */
    private Boolean isDelete;

    private static final long serialVersionUID = 1L;
}