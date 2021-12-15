/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import java.util.List;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import java.time.LocalDate;

/**
 *
 * @author pro
 */
public interface FlooringMasteryOrderDao {
   

    List<Order> getOrders(LocalDate dateChoice) throws FlooringMasteryPersistenceException;

    Order addOrder(Order o) throws FlooringMasteryPersistenceException;

    Order editOrder(Order editedOrder) throws FlooringMasteryPersistenceException;

    Order removeOrder(Order o) throws FlooringMasteryPersistenceException;
    public String exportOrders() throws FlooringMasteryPersistenceException;


    
}
