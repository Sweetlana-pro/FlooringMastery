/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author pro
 */
public interface FlooringMasteryServiceLayer {
    List<Order> getOrders(LocalDate dateChoice) throws InvalidOrderNumberException,
            FlooringMasteryPersistenceException;
    List<Product> getAllProducts () throws FlooringMasteryPersistenceException;

    Order calculateOrder(Order o) throws FlooringMasteryPersistenceException,
            FlooringMasteryDataValidationException, StateValidationException, ProductValidationException;

    Order getOrder(LocalDate dateChoice, int orderNumber) throws
            FlooringMasteryPersistenceException, InvalidOrderNumberException;

    Order addOrder(Order o) throws FlooringMasteryPersistenceException;

    Order compareOrders(Order savedOrder, Order editedOrder)
            throws FlooringMasteryPersistenceException, StateValidationException,
            ProductValidationException;

    Order editOrder(Order updatedOrder) throws FlooringMasteryPersistenceException,
            InvalidOrderNumberException;

    Order removeOrder(Order removedOrder) throws FlooringMasteryPersistenceException,
            InvalidOrderNumberException;
    String exportOrders() throws FlooringMasteryPersistenceException;
}
