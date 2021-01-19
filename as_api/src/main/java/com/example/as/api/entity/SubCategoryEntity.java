package com.example.as.api.entity;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubCategoryEntity {
    public Integer subcategoryId ;
    /**  */
    public String groupName ;
    /**  */
    public Integer categoryId ;
    /**  */
    public String subcategoryName ;
    /**  */
    public String subcategoryIcon ;
    /**  */
    public Integer showType ;
}
