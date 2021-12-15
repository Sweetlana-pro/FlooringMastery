/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;
import com.sg.flooringmastery.dao.FlooringMasteryOrderDao;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sweetlana Protsenko
 */
public class FlooringMasteryOrderDaoStubImpl implements FlooringMasteryOrderDao {
    private Order onlyOrder;
    private List<Order> ordersList = new ArrayList<>();
    
    public FlooringMasteryOrderDaoStubImpl() {

        onlyOrder = new Order();
        onlyOrder.setDate(LocalDate.parse("04302022",
                DateTimeFormatter.ofPattern("MMddyyyy")));
        onlyOrder.setOrderNumber(1);
        onlyOrder.setCustomerName("My Company");
        onlyOrder.setStateAbbr("TX");
        onlyOrder.setTaxRate(new BigDecimal("6.00"));
        onlyOrder.setProductType("Laminate");
        onlyOrder.setArea(new BigDecimal("100"));
        onlyOrder.setMaterialCostPerSquareFoot(new BigDecimal("1.75"));
        onlyOrder.setLaborCostPerSquareFoot(new BigDecimal("2.11"));
        onlyOrder.setMaterialCost(onlyOrder.getMaterialCostPerSquareFoot()
                .multiply(onlyOrder.getArea()).setScale(2, RoundingMode.HALF_UP));
        onlyOrder.setLaborCost(onlyOrder.getLaborCostPerSquareFoot().multiply(onlyOrder.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        onlyOrder.setTax(onlyOrder.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((onlyOrder.getMaterialCost().add(onlyOrder.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        onlyOrder.setTotal(onlyOrder.getMaterialCost().add(onlyOrder.getLaborCost())
                .add(onlyOrder.getTax()));

        ordersList.add(onlyOrder);

    }
    //public FlooringMasteryOrderDaoStubImpl(Order testOrder){
    //    this.onlyOrder = testOrder;
    //}

    @Override
    public List<Order> getOrders(LocalDate dateChoice) throws FlooringMasteryPersistenceException {
        if (dateChoice.equals(onlyOrder.getDate())) {
            return ordersList;
        } else {
            //Should return an empty list like the dao does.
            return new ArrayList<>();
        }
    }
   

    @Override
    public Order addOrder(Order o) throws FlooringMasteryPersistenceException {
        ordersList.add(o);
        return o;
    }

    @Override
    public Order editOrder(Order editedOrder) throws FlooringMasteryPersistenceException{
        if (editedOrder.getOrderNumber() == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order removeOrder(Order o) throws FlooringMasteryPersistenceException {
        if (o.equals(onlyOrder)) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public String exportOrders() throws FlooringMasteryPersistenceException {
        return null;
    }

}
