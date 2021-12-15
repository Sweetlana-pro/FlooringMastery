/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pro
 */
public class FlooringMasteryProductDaoFileImpl implements FlooringMasteryProductDao {
    
    private final String PRODUCT_FILE;
    public  final String DELIMITER = ",";
    
    public FlooringMasteryProductDaoFileImpl() {
        PRODUCT_FILE = "Products.txt";
    }
    public FlooringMasteryProductDaoFileImpl(String productTextFile) {
        PRODUCT_FILE = productTextFile;
    }

    //private List<Product> products = new ArrayList<>();
    private Map<String, Product> productMap = new HashMap<>();
    
    @Override
    public List<Product> getProducts() {
        try {
            loadProducts();
        } catch (FlooringMasteryPersistenceException ex) {
            Logger.getLogger(FlooringMasteryProductDaoFileImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Product>(productMap.values());
    }
    
    @Override
    public Product getProduct(String productType) throws FlooringMasteryPersistenceException {
        loadProducts();
        return productMap.get(productType);
        
    }
    //Translating a line of the text into object
    private Product unmarshallProducts (String productsAsText) {
        String[] productsTokens = productsAsText.split(DELIMITER);
        String productType = productsTokens[0];
        Product productFromFile = new Product(productType);
        productFromFile.setMaterialCostPerSquareFoot(new BigDecimal(productsTokens[1]));
        productFromFile.setLaborCostPerSquareFoot(new BigDecimal (productsTokens[2]));
        
        return productFromFile;
    }

    private void loadProducts() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        List<Product> products = new ArrayList<>();

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "-_- Could not load products data into memory.", e);
        }
        
         String currentLine;
        Product currentProduct;
        //String[] currentTokens;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProducts (currentLine);
            
            productMap.put(currentProduct.getProductType(), currentProduct);
        }
        scanner.close();
        
    }
}
