package com.jian.mall.service;

import com.jian.mall.common.ServerResponse;
import com.jian.mall.pojo.Product;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse setSaleStatus(Integer productId, Integer status);
    ServerResponse<Object> manageProductDetail(Integer productId);
}
