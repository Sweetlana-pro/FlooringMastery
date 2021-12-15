/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Order;
import com.sg.flooringmastery.dto.Order;

/**
 *
 * @author Sweetlana Protsenko
 */
public class FlooringMasteryOrderDaoFileImplTest {
    FlooringMasteryOrderDao ordersDao; 
    
    
    public FlooringMasteryOrderDaoFileImplTest () {
    
    }
    @BeforeEach
    public void setUp () throws Exception {
    String dataFolder = "TestOrders/testOrders_";
    ordersDao = new FlooringMasteryOrderDaoFileImpl(dataFolder);
    }

    
    @Test
    public void testAddGetOrders() throws Exception {
        //Arrange
        LocalDate date = LocalDate.parse("07312022",
                DateTimeFormatter.ofPattern("MMddyyyy"));
        List<Order> initialOrders = ordersDao.getOrders(date);

        Order order = new Order();
        order.setDate(date);
        order.setCustomerName("Amazon");
        order.setStateAbbr("IN");
        order.setTaxRate(new BigDecimal("6.00"));
        order.setProductType("Laminate");
        order.setArea(new BigDecimal("100"));
        order.setMaterialCostPerSquareFoot(new BigDecimal("1.75"));
        order.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order.setMaterialCost(order.getMaterialCostPerSquareFoot()
                .multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(order.getLaborCostPerSquareFoot().multiply(order.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order.setTax(order.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order.getMaterialCost().add(order.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost())
                .add(order.getTax()));

        order = ordersDao.addOrder(order);
        //Act
        List<Order> fromDao = ordersDao.getOrders(order.getDate());
        //Assert
        //Testing to see if a row was added to the test file.
        assertEquals(1, (fromDao.size() - initialOrders.size()));

    }

    /**
     * Test of editOrder method, of class OrdersProdDaoFileImpl.
     */
    @Test
    public void testEditOrder() throws Exception {

        LocalDate date = LocalDate.parse("11302021",
                DateTimeFormatter.ofPattern("MMddyyyy"));

        Order order = new Order();
        order.setDate(date);
        order.setCustomerName("Cool Inc.");
        order.setStateAbbr("IN");
        order.setTaxRate(new BigDecimal("6.00"));
        order.setProductType("Laminate");
        order.setArea(new BigDecimal("100"));
        order.setMaterialCostPerSquareFoot(new BigDecimal("1.75"));
        order.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order.setMaterialCost(order.getMaterialCostPerSquareFoot()
                .multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(order.getLaborCostPerSquareFoot().multiply(order.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order.setTax(order.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order.getMaterialCost().add(order.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost())
                .add(order.getTax()));

        order = ordersDao.addOrder(order);

        Order editedOrder = order;
        editedOrder.setCustomerName("Cool Dudes & Co.");

        editedOrder = ordersDao.editOrder(editedOrder);

        List<Order> orders = ordersDao.getOrders(date);
        int orderNumber = editedOrder.getOrderNumber();

        Order chosenOrder = orders.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);

        assertEquals(editedOrder, chosenOrder);

    }

    @Test
    public void testEditOrderFail() throws Exception {

        LocalDate date = LocalDate.parse("10292021",
                DateTimeFormatter.ofPattern("MMddyyyy"));

        Order order = new Order();
        order.setDate(date);
        order.setCustomerName("Solomon");
        order.setStateAbbr("IN");
        order.setTaxRate(new BigDecimal("6.00"));
        order.setProductType("Laminate");
        order.setArea(new BigDecimal("100"));
        order.setMaterialCostPerSquareFoot(new BigDecimal("1.75"));
        order.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order.setMaterialCost(order.getMaterialCostPerSquareFoot()
                .multiply(order.getArea()).setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(order.getLaborCostPerSquareFoot().multiply(order.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order.setTax(order.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order.getMaterialCost().add(order.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost())
                .add(order.getTax()));

        order = ordersDao.addOrder(order);

        Order editedOrder = order;
        //Using arbitrary order number to force null
        editedOrder.setOrderNumber(0);
        editedOrder.setCustomerName("SHOULD NOT SEE ME");

        editedOrder = ordersDao.editOrder(editedOrder);

        //Testing that null comes back if unable to edit order.
        assertNull(editedOrder);

    }

    /**
     * Test of removeOrder method, of class OrdersProdDaoFileImpl.
     */
    @Test
    public void testRemoveOrder() throws Exception {

        LocalDate date = LocalDate.parse("11112021",
                DateTimeFormatter.ofPattern("MMddyyyy"));

        Order order1 = new Order();
        order1.setDate(date);
        order1.setCustomerName("My Company");
        order1.setStateAbbr("IN");
        order1.setTaxRate(new BigDecimal("6.00"));
        order1.setProductType("Laminate");
        order1.setArea(new BigDecimal("100"));
        order1.setMaterialCostPerSquareFoot(new BigDecimal("1.75"));
        order1.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order1.setMaterialCost(order1.getMaterialCostPerSquareFoot()
                .multiply(order1.getArea()).setScale(2, RoundingMode.HALF_UP));
        order1.setLaborCost(order1.getLaborCostPerSquareFoot().multiply(order1.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order1.setTax(order1.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order1.getMaterialCost().add(order1.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order1.setTotal(order1.getMaterialCost().add(order1.getLaborCost())
                .add(order1.getTax()));

        ordersDao.addOrder(order1);

        Order order2 = new Order();
        order2.setDate(date);
        order2.setCustomerName("CNC Repair");
        order2.setStateAbbr("IN");
        order2.setTaxRate(new BigDecimal("6.00"));
        order2.setProductType("Laminate");
        order2.setArea(new BigDecimal("100"));
        order2.setMaterialCostPerSquareFoot(new BigDecimal("1.75"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order2.setMaterialCost(order2.getMaterialCostPerSquareFoot()
                .multiply(order2.getArea()).setScale(2, RoundingMode.HALF_UP));
        order2.setLaborCost(order2.getLaborCostPerSquareFoot().multiply(order2.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order2.setTax(order2.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order2.getMaterialCost().add(order2.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order2.setTotal(order2.getMaterialCost().add(order2.getLaborCost())
                .add(order2.getTax()));

        order2 = ordersDao.addOrder(order2);

        List<Order> initialOrders = ordersDao.getOrders(date);

        ordersDao.removeOrder(order2);

        List<Order> fromDao = ordersDao.getOrders(date);

        //Testing to see if a row was removed from the test file.
        assertEquals(-1, (fromDao.size() - initialOrders.size()));

    }

   
}
