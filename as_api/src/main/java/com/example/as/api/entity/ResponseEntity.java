package com.example.as.api.entity;

import com.example.as.api.hiconfig.HiConfigDelegate;
import com.example.as.api.util.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)/*不返回null的字段*/
public class ResponseEntity {
    private int code;
    private String message;
    private Object data;
    private Map<String, Object> extra = new HashMap<>();

    private ResponseEntity(ResponseCode responseCode) {
        this.message = responseCode.msg();
        this.code=responseCode.code();
        /*在数据返回前，添加探针，真实项目中通过网关做*/
        HiConfigDelegate.bindConfig(extra);
    }

    public static ResponseEntity of(ResponseCode responseCode) {
        return new ResponseEntity(responseCode);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public Map<String, Object> getExtra() {
        return extra == null || extra.isEmpty() ? null : extra;
    }

    public ResponseEntity setCode(int code) {
        this.code = code;
        return this;
    }

    public ResponseEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResponseEntity setData(Object data) {
        this.data = data;
        return this;
    }
    public ResponseEntity addParams(String key, Object value) {
        if (data == null) data = new HashMap<>();
        ((Map) data).put(key, value);
        return this;

    }
    public static ResponseEntity success(Object data){
        return ResponseEntity.of(ResponseCode.RC_SUCCESS).setData(data);
    }
    public static ResponseEntity successMessage(String msg) {
        return ResponseEntity.of(ResponseCode.RC_SUCCESS).setMessage(msg);
    }
}
