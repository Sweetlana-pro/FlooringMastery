/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Sweetalana Protsenko
 */
public class FlooringMasteryProductDaoFileImplTest {
    
    FlooringMasteryProductDao testProductDao;
    
    public FlooringMasteryProductDaoFileImplTest() {
    }
    
    
    @BeforeEach
    public void setUp() throws Exception {
        String testProductFile = "testProducts";
        new FileWriter(testProductFile);
        testProductDao = new FlooringMasteryProductDaoFileImpl(testProductFile);
       
    PrintWriter out;

	    try {
	        out = new PrintWriter(new FileWriter(testProductFile));
	    } catch (IOException e) {
	        throw new FlooringMasteryPersistenceException(
	                "Could not save data.", e);
	    }
            out.println("Carpet,2.25,2.11");
            out.println("Laminate,1.75,2.11");
            //out.println("Tile,3.50,4.15");
           // out.println("Wood,5.15,4.75");
            out.flush();
            out.close();
        
    }
        
    @Test
    public void testGetProduct() throws Exception {
        String productType = "Carpet";
        Product product = new Product(productType);
        product.setMaterialCostPerSquareFoot(BigDecimal.valueOf(2.25));
        product.setLaborCostPerSquareFoot(BigDecimal.valueOf(2.11));
        
                       
        //Get the Product from the DAO
        Product retrievedProduct = testProductDao.getProduct(productType);
        
        //Check the data is equal
        assertEquals(product.getProductType(),
                    retrievedProduct.getProductType(),
                    "Checking a product type.");
        assertEquals(product.getMaterialCostPerSquareFoot(),
                    retrievedProduct.getMaterialCostPerSquareFoot(),
                    "Checking price for material");
        assertEquals(product.getLaborCostPerSquareFoot(),
                    retrievedProduct.getLaborCostPerSquareFoot(),
                    "Checking price for labor");
        
    }
    @Test
    public void testGetProducts() throws Exception {
        //Arragde: Create first Product
        Product firstProduct = new Product("Carpet");
        firstProduct.setMaterialCostPerSquareFoot(BigDecimal.valueOf(2.25));
        firstProduct.setLaborCostPerSquareFoot(BigDecimal.valueOf(2.11));
        
        //Arrange: Create second product
        Product secondProduct = new Product("Laminate");
        secondProduct.setMaterialCostPerSquareFoot(BigDecimal.valueOf(1.75));
        secondProduct.setLaborCostPerSquareFoot(BigDecimal.valueOf(2.11));
        
        //Act: Retrive the list of products within the DAO
        List<Product> allProducts = testProductDao.getProducts();
        
        //Assert: check the general contents of the list
        assertNotNull(allProducts, "The list shouls not be null");
        assertEquals(2, allProducts.size(), "List of products should have 3 products.");
        
        //Assert: check cpecifics
        assertTrue(testProductDao.getProducts().contains(firstProduct), "List should include Carpet.");
        assertTrue(testProductDao.getProducts().contains(secondProduct), "List should include Laminate.");
        
        
    }
}
