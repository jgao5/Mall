package com.jian.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jian.mall.common.ServerResponse;
import com.jian.mall.dao.CategoryMapper;
import com.jian.mall.pojo.Category;
import com.jian.mall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (StringUtils.isBlank(categoryName) || parentId == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true); //该分类可用

        int selectCount = categoryMapper.insertSelective(category);
        if (selectCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }

        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId) {
        if (StringUtils.isBlank(categoryName) || categoryId == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0 ) {
            return ServerResponse.createBySuccessMessage("更新品类名称成功");
        }
        return ServerResponse.createByErrorMessage("新品类名称失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }


    /**
     * 递归查询本节点与子节点categoryId
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse getCategoryRecursive(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null){
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //递归找到子节点
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
