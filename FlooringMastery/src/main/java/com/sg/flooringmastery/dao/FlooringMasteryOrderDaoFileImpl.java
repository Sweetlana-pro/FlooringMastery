/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.State;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Double.parseDouble;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @Sweetlana Protsenko
 */
public class FlooringMasteryOrderDaoFileImpl implements FlooringMasteryOrderDao {
   private int lastOrderNumber;
    private final String HEADER = "OrderNumber,CustomerName,State,TaxRate,"
            + "ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,"
            + "MaterialCost,LaborCost,Tax,Total";
    private final String DELIMITER = ",";
    private String dataFolder = "Orders/";

    public FlooringMasteryOrderDaoFileImpl() {
    }

    public FlooringMasteryOrderDaoFileImpl(String dataFolder) {
        this.dataFolder = dataFolder;
    }
            
    private int getLastOrderNumber(Order ord) throws FlooringMasteryPersistenceException {
        List<Order> orders = loadOrders(ord.getDate());        
        
            int numInc = orders.stream()
                    .mapToInt((o) -> 
                            o.getOrderNumber()).max().orElse(0);
            lastOrderNumber = numInc + 1; 
        
        
        ord.setOrderNumber(lastOrderNumber);
        
        return lastOrderNumber;
    }        
        

    @Override
    public List<Order> getOrders(LocalDate chosenDate) throws FlooringMasteryPersistenceException {
        return loadOrders(chosenDate);
    }

    @Override
    public Order addOrder(Order o) throws FlooringMasteryPersistenceException {
        //Checks input for commas
        o = cleanFields(o);
        getLastOrderNumber(o);
        
        o.setOrderNumber(lastOrderNumber);
        
        List<Order> orders = loadOrders(o.getDate());
        orders.add(o);
        writeOrders(orders, o.getDate());

        return o;
       
    }

    @Override
    public Order editOrder(Order editedOrder)
            throws FlooringMasteryPersistenceException {
        //Checks input for commas
        editedOrder = cleanFields(editedOrder);
        int orderNumber = editedOrder.getOrderNumber();

        List<Order> orders = loadOrders(editedOrder.getDate());
        Order chosenOrder = orders.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);

        if (chosenOrder != null) {
            int index = orders.indexOf(chosenOrder);
            orders.set(index, editedOrder);
            writeOrders(orders, editedOrder.getDate());
            return editedOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order removeOrder(Order chosenOrder)
            throws FlooringMasteryPersistenceException {

        int orderNumber = chosenOrder.getOrderNumber();

        List<Order> orders = loadOrders(chosenOrder.getDate());
        Order removedOrder = orders.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);

        if (removedOrder != null) {
            orders.remove(removedOrder);
            writeOrders(orders, chosenOrder.getDate());
            return removedOrder;
        } else {
            return null;
        }

    }

    private Order cleanFields(Order order) {
        //Dao does not know what the other layers are doing so its clearing delimiters
        String cleanCustomerName = order.getCustomerName().replace(DELIMITER, "");
        String cleanStateAbbr = order.getStateAbbr().replace(DELIMITER, "");
        String cleanProductType = order.getProductType().replace(DELIMITER, "");

        order.setCustomerName(cleanCustomerName);
        order.setStateAbbr(cleanStateAbbr);
        order.setProductType(cleanProductType);

        return order;
    }
    
    private List<Order> loadOrders(LocalDate chosenDate) throws FlooringMasteryPersistenceException {
        //Loads one file for a specific date
        Scanner scanner;
        String fileDate = chosenDate.format(DateTimeFormatter.ofPattern("MMddyyyy"));

        File f = new File(String.format(dataFolder + "Orders_%s.txt", fileDate));

        List<Order> orders = new ArrayList<>();

        if (f.isFile()) {
            try {
                scanner = new Scanner(
                        new BufferedReader(
                                new FileReader(f)));
            } catch (FileNotFoundException e) {
                throw new FlooringMasteryPersistenceException(
                        "-_- Could not load order data into memory.", e);
            }
            String currentLine;
            String[] currentTokens;
            scanner.nextLine();//Skips scanning document headers
            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                currentTokens = currentLine.split(DELIMITER);
                if (currentTokens.length == 12) {
                    Order currentOrder = new Order();
                    currentOrder.setDate(LocalDate.parse(fileDate,
                            DateTimeFormatter.ofPattern("MMddyyyy")));
                    currentOrder.setOrderNumber(Integer.parseInt(currentTokens[0]));
                    currentOrder.setCustomerName(currentTokens[1]);
                    currentOrder.setStateAbbr(currentTokens[2]);
                    currentOrder.setTaxRate(new BigDecimal(currentTokens[3]));
                    currentOrder.setProductType(currentTokens[4]);
                    currentOrder.setArea(new BigDecimal(currentTokens[5]));
                    currentOrder.setMaterialCostPerSquareFoot(new BigDecimal(currentTokens[6]));
                    currentOrder.setLaborCostPerSquareFoot(new BigDecimal(currentTokens[7]));
                    currentOrder.setMaterialCost(new BigDecimal(currentTokens[8]));
                    currentOrder.setLaborCost(new BigDecimal(currentTokens[9]));
                    currentOrder.setTax(new BigDecimal(currentTokens[10]));
                    currentOrder.setTotal(new BigDecimal(currentTokens[11]));
                    orders.add(currentOrder);
                } else {
                }
            }
            scanner.close();
            return orders;
        } else {
            return orders;
        }
    }
    
    private void writeOrders(List<Order> orders, LocalDate orderDate)
            throws FlooringMasteryPersistenceException {

        PrintWriter out;

        String fileDate = orderDate.format(DateTimeFormatter.
                ofPattern("MMddyyyy"));

        File f = new File(String.format(dataFolder + "Orders_%s.txt", fileDate));

        try {
            out = new PrintWriter(new FileWriter(f));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException(
                    "Could not save order data.", e);
        }

        // Write out the Order objects to the file.
        out.println(HEADER);
        //String orderAsText;
        
        for (Order currentOrder : orders) {
            //orderAsText = marshallOrder(currentOrder);
            out.println(currentOrder.getOrderNumber() + DELIMITER
                + currentOrder.getCustomerName() + DELIMITER
                + currentOrder.getStateAbbr() + DELIMITER
                + currentOrder.getTaxRate() + DELIMITER
                + currentOrder.getProductType() + DELIMITER
                + currentOrder.getArea() + DELIMITER
                + currentOrder.getMaterialCostPerSquareFoot() + DELIMITER
                + currentOrder.getLaborCostPerSquareFoot() + DELIMITER
                + currentOrder.getMaterialCost() + DELIMITER
                + currentOrder.getLaborCost() + DELIMITER
                + currentOrder.getTax() + DELIMITER
                + currentOrder.getTotal());
            //out.println(orderAsText);
            out.flush();
        }
        out.close();
    }
    
    
     @Override
    public String exportOrders() throws FlooringMasteryPersistenceException {
        String exportFile = "Export_orders.txt";
        File ordersFolder = new File("Orders/");
        File[] orderFiles = ordersFolder.listFiles();
        Scanner exportInput;
        PrintWriter exportOutput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        try {
            exportOutput = new PrintWriter(new FileWriter(exportFile));
        } catch (IOException e) {
            throw new FlooringMasteryPersistenceException("ERROR: can't write to file", e);
        }

        for (File file : orderFiles) {
            String fileName = file.getName();
            String stringDate = fileName.substring(7, 15);
            LocalDate ordersDate = LocalDate.parse(stringDate, formatter);
            try {
                exportInput = new Scanner(new BufferedReader(new FileReader("Orders/" + fileName)));
            } catch (FileNotFoundException e) {
                throw new FlooringMasteryPersistenceException("Could not find data file for that date " + ordersDate, e);
            }
            if (exportInput.hasNextLine()) {
                exportOutput.println("Date " + ordersDate);
                exportOutput.println();
            } else {
                
                continue;
            }
            
            while (exportInput.hasNextLine()) {
                String orderRecord = exportInput.nextLine();
                exportOutput.println(orderRecord);
                exportOutput.flush();
            }
            
            exportOutput.println();
           
        }
        
        exportOutput.close();
        
        return exportFile;
    }

}
