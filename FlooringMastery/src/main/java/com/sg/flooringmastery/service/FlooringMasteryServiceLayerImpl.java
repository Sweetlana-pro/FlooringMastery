/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;


import com.sg.flooringmastery.dao.FlooringMasteryOrderDao;
import com.sg.flooringmastery.dao.FlooringMasteryProductDao;
import com.sg.flooringmastery.dao.FlooringMasteryStateDao;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.State;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
//import com.sg.flooringmaster.service.FlooringMasterPersistenceException;

/**
 *
 * @author pro
 */
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {
    FlooringMasteryOrderDao orderDao;
    FlooringMasteryProductDao productsDao;
    FlooringMasteryStateDao stateDao;

    public FlooringMasteryServiceLayerImpl(
            FlooringMasteryOrderDao orderDao, FlooringMasteryProductDao productDao, FlooringMasteryStateDao stateDao) {
        this.orderDao = orderDao;
        this.productsDao = productDao;
        this.stateDao = stateDao;
    }
    
    @Override
    public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
        return productsDao.getProducts();
    }

    @Override
    public List<Order> getOrders(LocalDate chosenDate) throws InvalidOrderNumberException,
            FlooringMasteryPersistenceException, FlooringMasteryPersistenceException,FlooringMasteryPersistenceException {
        List<Order> ordersByDate = orderDao.getOrders(chosenDate);
        if (!ordersByDate.isEmpty()) {
            return ordersByDate;
        } else {
            throw new InvalidOrderNumberException("SORRY: No orders exist on your date. ");
                   
        }
    }

    @Override
    public Order getOrder(LocalDate chosenDate, int orderNumber) throws
            FlooringMasteryPersistenceException, InvalidOrderNumberException, FlooringMasteryPersistenceException, FlooringMasteryPersistenceException {
        List<Order> orders = getOrders(chosenDate);
        Order chosenOrder = orders.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);
        if (chosenOrder != null) {
            return chosenOrder;
        } else {
            throw new InvalidOrderNumberException("SORRY: There are no orders with that number on that date ");
                    
        }
    }

    @Override
    public Order calculateOrder(Order o) throws FlooringMasteryPersistenceException,
            FlooringMasteryDataValidationException, StateValidationException, ProductValidationException {

        validateOrder(o);
        calculateTax(o);
        calculateMaterial(o);
        calculateTotal(o);

        return o;

    }
    
     private void calculateMaterial(Order o) throws FlooringMasteryPersistenceException,
            ProductValidationException {
        
        Product chosenProduct = productsDao.getProduct(o.getProductType());
        
        
        if (chosenProduct == null) {
            throw new ProductValidationException("SORRY: We do not sell this product ");
                   
        }
        o.setProductType(chosenProduct.getProductType());
        o.setMaterialCostPerSquareFoot(chosenProduct.getMaterialCostPerSquareFoot());
        o.setLaborCostPerSquareFoot(chosenProduct.getLaborCostPerSquareFoot());
    }

    private void calculateTax(Order o) throws FlooringMasteryPersistenceException,
            StateValidationException {
        //Set state information in order.
        State chosenState = stateDao.getState(o.getStateAbbr());
        if (chosenState == null) {
            throw new StateValidationException("SORRY: we do not serve this state");
                    
        }
        o.setStateAbbr(chosenState.getStateAbbr());
        o.setTaxRate(chosenState.getTaxRate());
    }

    private void calculateTotal(Order o) {
        o.setMaterialCost(o.getMaterialCostPerSquareFoot().multiply(o.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        o.setLaborCost(o.getLaborCostPerSquareFoot().multiply(o.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        o.setTax(o.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((o.getMaterialCost().add(o.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        o.setTotal(o.getMaterialCost().add(o.getLaborCost()).add(o.getTax()));
    }

    private void validateOrder(Order o) throws FlooringMasteryDataValidationException {
        String message = "";
        if (o.getCustomerName().trim().isEmpty() || o.getCustomerName() == null) {
            message += "Customer name is required.\n";
        }
        if (o.getStateAbbr().trim().isEmpty() || o.getStateAbbr() == null) {
            message += "State is required.\n";
        }
        if (o.getProductType().trim().isEmpty() || o.getProductType() == null) {
            message += "Product type is required.\n";
        }
        if (o.getArea().compareTo(BigDecimal.ZERO) == 0 || o.getArea() == null) {
            message += "Area square footage is required.\n";
        }else if (o.getArea().compareTo(new BigDecimal("100")) < 0) {
            message += "Area should be min 100 sqft.\n";
        }
       
        if (!message.isEmpty()) {
            throw new FlooringMasteryDataValidationException(message);
        }
    }

    @Override
    public Order addOrder(Order o) throws FlooringMasteryPersistenceException {
        orderDao.addOrder(o);
        
        return o;
    }

    @Override
    public Order compareOrders(Order savedOrder, Order editedOrder)
            throws FlooringMasteryPersistenceException, StateValidationException,
            ProductValidationException {

        //This will only update the already saved order's fields
        if (editedOrder.getCustomerName() == null
                || editedOrder.getCustomerName().trim().equals("")) {
            //No change
        } else {
            savedOrder.setCustomerName(editedOrder.getCustomerName());
        }

        if (editedOrder.getStateAbbr() == null
                || editedOrder.getStateAbbr().trim().equals("")) {
        } else {
            savedOrder.setStateAbbr(editedOrder.getStateAbbr());
            calculateTax(savedOrder);
        }

        if (editedOrder.getProductType() == null
                || editedOrder.getProductType().equals("")) {
        } else {
            savedOrder.setProductType(editedOrder.getProductType());
            calculateMaterial(savedOrder);
        }

        if (editedOrder.getArea() == null
                || (editedOrder.getArea().compareTo(new BigDecimal("100"))) < 0
                    ||editedOrder.getArea().equals("")) {
        } else {
            savedOrder.setArea(editedOrder.getArea());
        }

        calculateTotal(savedOrder);

        return savedOrder;
    }

    @Override
    public Order editOrder(Order updatedOrder) throws FlooringMasteryPersistenceException,
            InvalidOrderNumberException {
        updatedOrder = orderDao.editOrder(updatedOrder);
        if (updatedOrder != null) {
            
            return updatedOrder;
        } else {
            throw new InvalidOrderNumberException("SORRY: There are no orders with that number on that date ");
        }
        
    }

    @Override
    public Order removeOrder(Order removedOrder) throws FlooringMasteryPersistenceException,
            InvalidOrderNumberException {
        removedOrder = orderDao.removeOrder(removedOrder);
        if (removedOrder != null) {
            
            return removedOrder;
        } else {
            throw new InvalidOrderNumberException("SORRY: There are no orders with that number on that date ");
        }
        
    }
    public String exportOrders() throws FlooringMasteryPersistenceException {
      return orderDao.exportOrders();
    }

}
