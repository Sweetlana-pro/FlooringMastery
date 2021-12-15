/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import java.util.List;

/**
 *
 * @author pro
 */
public interface FlooringMasteryProductDao {
  
    Product getProduct(String productType) throws FlooringMasteryPersistenceException;
    List<Product> getProducts();

}
