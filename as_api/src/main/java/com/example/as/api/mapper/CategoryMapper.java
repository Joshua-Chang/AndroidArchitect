package com.example.as.api.mapper;


import com.example.as.api.entity.CategoryEntity;
import com.example.as.api.entity.SubCategoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    List<CategoryEntity> getCategoryList(int pageIndex, int pageSize);

    List<SubCategoryEntity> getSubCategoryList(String id);

    void addCategory(String categoryName, String createTime);

    void removeCategory(String id);
}
