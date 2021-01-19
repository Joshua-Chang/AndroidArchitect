package com.example.as.api.util;

import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtil {
    public static <T> Map<String, Object> getPageData(List<T> list) {
        PageInfo<T>pageInfo=new PageInfo<>(list);
        Map<String, Object> data = new HashMap<>();
        data.put("total", pageInfo.getTotal());
        data.put("pageIndex", pageInfo.getPageNum());
        data.put("pageSize", pageInfo.getPageSize());
        data.put("list", pageInfo.getList());
        return data;
    }
}
