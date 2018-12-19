package com.jian.mall.service;

import com.jian.mall.common.ServerResponse;
import com.jian.mall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(String categoryName, Integer categoryId);
    ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId);
    ServerResponse getCategoryRecursive(Integer categoryId);
}
