/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryProductDao;
import com.sg.flooringmastery.dto.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sweetlana Protsenko
 * email: svitprotsenko@gmail.com
 */
public class FlooringMasteryProductDaoStubImpl implements FlooringMasteryProductDao {
    
    public Product onlyProduct;
    
    public FlooringMasteryProductDaoStubImpl() {
        String productType = "Carpet";
        Product onlyProduct = new Product(productType);
        onlyProduct.setMaterialCostPerSquareFoot(BigDecimal.valueOf(2.25));
        onlyProduct.setLaborCostPerSquareFoot(BigDecimal.valueOf(2.11));
    }
    
    public FlooringMasteryProductDaoStubImpl(Product testProduct) {
        this.onlyProduct = testProduct;
    }

    @Override
    public Product getProduct(String productType) throws FlooringMasteryPersistenceException {
        if(productType.equals(onlyProduct.getProductType())) {
            return onlyProduct;
        }else {
            return null;
        }
    }

    @Override
    public List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(onlyProduct);
        return productList;
    }
    
}
