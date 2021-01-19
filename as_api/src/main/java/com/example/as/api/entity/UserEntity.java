package com.example.as.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)/*不返回null的字段*/
@JsonIgnoreProperties(value = {"pwd"})/*返回忽略pwd字段*/
public class UserEntity {
    public String uid ;
    /** 幕客ID */
    public String imoocId ;
    /** 订单ID */
    public Integer orderId ;
    /** 用户名 */
    public String userName ;
    /** 密码 */
    public String pwd ;
    /** 创建时间 */
    public String createTime ;
    /** 是否被禁用 */
    public String forbid ;
}
