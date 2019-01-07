package com.jian.mall.service.impl;

import com.jian.mall.common.ResponseCode;
import com.jian.mall.common.ServerResponse;
import com.jian.mall.dao.CategoryMapper;
import com.jian.mall.dao.ProductMapper;
import com.jian.mall.pojo.Category;
import com.jian.mall.pojo.Product;
import com.jian.mall.service.IProductService;
import com.jian.mall.util.DateTimeUtil;
import com.jian.mall.util.PropertiesUtil;
import com.jian.mall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String [] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            //更新产品
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("产品更新成功");
                } else {
                    return ServerResponse.createByErrorMessage("产品更新失败");
                }
            } else { //添加新产品
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("产品更新成功");
                } else {
                    return ServerResponse.createByErrorMessage("产品更新失败");
                }
            }
        }
        return ServerResponse.createByErrorMessage("新增或更新产品参数错误");
    }

    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改产品状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品状态失败");
    }


    @Override
    public ServerResponse<Object> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        //VO对象   value object
        ProductDetailVo productDetailVo = this.assembleProductDetailVo(product);

        return null;
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setPrice(product.getPrice());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("http://img.happymmall.com/", "http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToString(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToString(product.getUpdateTime()));

        return productDetailVo;
    }
}
