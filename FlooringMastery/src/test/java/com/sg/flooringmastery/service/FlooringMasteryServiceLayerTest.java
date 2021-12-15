/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryOrderDao;
import com.sg.flooringmastery.dao.FlooringMasteryOrderDaoFileImpl;
import com.sg.flooringmastery.dao.FlooringMasteryProductDao;
import com.sg.flooringmastery.dao.FlooringMasteryProductDaoFileImpl;
import com.sg.flooringmastery.dao.FlooringMasteryStateDao;
import com.sg.flooringmastery.dao.FlooringMasteryStateDaoFileImpl;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.service.FlooringMasteryOrderDaoStubImpl;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.sg.flooringmastery.service.FlooringMasteryProductDaoStubImpl;
import com.sg.flooringmastery.service.FlooringMasteryStateDaoStubImpl;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author pro
 */
public class FlooringMasteryServiceLayerTest {
    
    
    private FlooringMasteryServiceLayer service;

    public FlooringMasteryServiceLayerTest() {
        /*FlooringMasteryOrderDao ordersDao = new FlooringMasteryOrderDaoStubImpl();
        FlooringMasteryProductDao productsDao = new FlooringMasteryProductDaoStubImpl();
        FlooringMasteryStateDao statesDao = new FlooringMasteryStateDaoStubImpl();
        service = new FlooringMasteryServiceLayerImpl(ordersDao, productsDao, statesDao);
        */
        
        //wire the Service layer with stub implementations of the Dao with Spring DI
        ApplicationContext ctx = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        service = ctx.getBean("serviceLayer", FlooringMasteryServiceLayer.class);
    }

    /**
     * Test of getOrders method, of class FloorServiceImpl.
     */
    @Test
    public void testGetOrders() throws Exception {

        assertEquals(1, service.getOrders(LocalDate.of(2022, 04, 30)).size());

        try {
            List<Order> orders = service.getOrders(LocalDate.of(1990, 01, 01));
            fail("Expected InvalidOrderNumberException was not thrown.");
        } catch (InvalidOrderNumberException e) {
        }
    }

    /**
     * Test of getOrder method, of class FloorServiceImpl.
     */
    @Test
    public void testGetOrder() throws Exception {
        Order order = service.getOrder(LocalDate.of(2022, 04, 30), 1);
        assertNotNull(order);

        try {
            order = service.getOrder(LocalDate.of(2022, 04, 30), 0);
            fail("Expected InvalidOrderNumberException was not thrown.");
        } catch (InvalidOrderNumberException e) {
        }

        try {
            service.getOrder(LocalDate.of(1990, 01, 01), 1);
            fail("Expected InvalidOrderNumberException was not thrown.");
        } catch (InvalidOrderNumberException e) {
        }

    }
    
    @Test
    public void testNewOrderValidDate() throws FlooringMasteryPersistenceException {
        //ARRANGE
        int orderNumber = 1;
        
        Order newOrder = new Order();
        newOrder.setCustomerName("My company");
        String stateAbbr = "TX";
        BigDecimal stateTaxRate = new BigDecimal("6.00");
        String productName = "Laminate";
        BigDecimal MaterialCostPerSquareFoot = new BigDecimal("1.75");
        BigDecimal LaborCostPerSquareFoot = new BigDecimal("2.11");
        BigDecimal area = new BigDecimal("100");
        
        //ACT&ASSERT
        LocalDate date = LocalDate.now().plusDays(1);
        service.addOrder(newOrder);

    }
    
    @Test
    public void testNewOrderInvalidDate() throws FlooringMasteryPersistenceException {
        
        //ARRANGE
        int orderNumber = 1;
        Order newOrder = new Order();
        newOrder.setCustomerName("My company");
        String stateAbbr = "TX";
        BigDecimal stateTaxRate = new BigDecimal("6.00");
        String productName = "Laminate";
        BigDecimal MaterialCostPerSquareFoot = new BigDecimal("1.75");
        BigDecimal LaborCostPerSquareFoot = new BigDecimal("2.11");
        BigDecimal area = new BigDecimal("100");
        
        //ACT&ASSERT
        LocalDate date = LocalDate.now();
        service.addOrder(newOrder);
    }

    

    /**
     * Test of addOrder method, of class FloorServiceImpl.
     */
    
    @Test
    public void testAddOrder() throws Exception {

        Order order = new Order();
        order.setCustomerName("Place LLC");
        order.setStateAbbr("MI");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100"));
        service.addOrder(order);

        assertEquals(order, service.addOrder(order));

    }

    /**
     * Test of compareOrders method, of class FloorServiceImpl.
     */
    @Test
    public void testCompareOrders() throws Exception {

        Order savedOrder = service.getOrder(LocalDate.of(2022, 04, 30), 1);

        Order editedOrder = new Order();
        editedOrder.setCustomerName("Peanut Butter LLC");

        Order updatedOrder = service.compareOrders(savedOrder, editedOrder);

        assertEquals(updatedOrder, savedOrder);

    }

    /**
     * Test of editOrder method, of class FloorServiceImpl.
     */
    @Test
    public void testEditOrder() throws Exception {

        Order savedOrder = service.getOrder(LocalDate.of(2022, 04, 30), 1);
        assertNotNull(savedOrder);

        try {
            savedOrder = service.getOrder(LocalDate.of(2022, 04, 30), 0);
            fail("Expected InvalidOrderNumberException was not thrown.");
        } catch (InvalidOrderNumberException e) {
        }

    }

    /**
     * Test of removeOrder method, of class FloorServiceImpl.
     */
    @Test
    public void testRemoveOrder() throws Exception {

        Order removedOrder = service.getOrder(LocalDate.of(2022, 04, 30), 1);
        assertNotNull(removedOrder);

        try {
            removedOrder = service.getOrder(LocalDate.of(2022, 04, 30), 0);
            fail("Expected InvalidOrderNumberException was not thrown.");
        } catch (InvalidOrderNumberException e) {
        }

    }
    @Test
    public void testGetAllProducts() throws Exception{
        //ARRANGE
        String firstProductName = "Carpet";
        BigDecimal firstProductMaterialCostPerSquareFoot = new BigDecimal("2.25").setScale(2, RoundingMode.HALF_UP);
        BigDecimal firstProductLaborCostPerSquareFoot = new BigDecimal("2.11").setScale(2, RoundingMode.HALF_UP);
        Product firstProduct = new Product(firstProductName);
        
              
        //ACT
        List<Product> returnedProducts = service.getAllProducts();
        //ASSERT
        assertFalse(returnedProducts.isEmpty(), "Returned productss must not be empty");
        assertEquals(returnedProducts.size(), 1, "Must have 1 product in returned products");
        
    }
}
