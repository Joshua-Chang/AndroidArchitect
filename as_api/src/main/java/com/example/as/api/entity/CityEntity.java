package com.example.as.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityEntity {
    /**
     * CREATE TABLE `sys_region` (
     *   `region_id` varchar(10) NOT NULL COMMENT '地区主键编号',
     *   `region_name` varchar(50) NOT NULL COMMENT '地区名称',
     *   `region_short_name` varchar(10) DEFAULT NULL COMMENT '地区缩写',
     *   `region_code` varchar(20) DEFAULT NULL COMMENT '行政地区编号',
     *   `region_parent_id` varchar(10) DEFAULT NULL COMMENT '地区父id',
     *   `region_level` int DEFAULT NULL COMMENT '地区级别 1-省、自治区、直辖市 2-地级市、地区、自治州、盟 3-市辖区、县级市、县',
     *   PRIMARY KEY (`region_id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地区表';
     */
    //id
    public String regionId;
    //父id
    public String regionParentId;
    //城市的名字
    public String regionName;
    public String regionShortName;
    public String regionCode;
    //城市的类型，0是国，1是省，2是市，3是区
    public int regionLevel;
}
