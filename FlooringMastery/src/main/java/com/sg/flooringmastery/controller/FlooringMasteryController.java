package com.sg.flooringmastery.controller;


import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import com.sg.flooringmastery.service.FlooringMasteryDataValidationException;
import com.sg.flooringmastery.service.InvalidOrderNumberException;
import com.sg.flooringmastery.service.ProductValidationException;
import com.sg.flooringmastery.service.StateValidationException;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author pro
 */
public class FlooringMasteryController {
    private UserIO io = new UserIOConsoleImpl ();
    private FlooringMasteryView view;
    private FlooringMasteryServiceLayer service;

    public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }

    public void run() {

        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        getOrdersByDate();
                        break;
                    case 2:
                        listProdType();
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }
            exitMessage();
        } catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    private void listProdType() throws FlooringMasteryPersistenceException {
        view.displayProductsTypeBanner();
        List<Product> products = service.getAllProducts();
        view.displayProductsList(products);

    }

    private void getOrdersByDate() throws FlooringMasteryPersistenceException, FlooringMasteryPersistenceException {
        LocalDate dateChoice = view.inputDate();
        view.displayDateBanner(dateChoice);
        try {
            view.displayDateOrders(service.getOrders(dateChoice));
            view.displayContinue();
        } catch (InvalidOrderNumberException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void addOrder() throws FlooringMasteryPersistenceException, FlooringMasteryPersistenceException {
        view.displayCreateNewOrderBanner();
        try {
            Order o = service.calculateOrder(view.getOrder());
            view.displayOrder(o);
            String response = view.askSave();
            if (response.equalsIgnoreCase("Y")) {
                service.addOrder(o);
                view.displayAddOrderSuccess(true, o);
            } else if (response.equalsIgnoreCase("N")) {
                view.displayAddOrderSuccess(false, o);
            } else {
                unknownCommand();
            }
        } catch (FlooringMasteryDataValidationException
                | StateValidationException | ProductValidationException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void editOrder() throws FlooringMasteryPersistenceException {
        view.displayEditOrderBanner();
        try {
            LocalDate dateChoice = view.inputDate();
            int orderNumber = view.inputOrderNumber();
            Order savedOrder = service.getOrder(dateChoice, orderNumber);
            Order editedOrder = view.editOrderInfo(savedOrder);
            Order updatedOrder = service.compareOrders(savedOrder, editedOrder);
            view.displayEditOrderBanner();
            view.displayOrder(updatedOrder);
            String response = view.askSave();
            if (response.equalsIgnoreCase("Y")) {
                service.editOrder(updatedOrder);
                view.displayEditOrderSuccess(true, updatedOrder);
            } else if (response.equalsIgnoreCase("N")) {
                view.displayEditOrderSuccess(false, updatedOrder);
            } else {
                unknownCommand();
            }
        } catch (InvalidOrderNumberException
                | ProductValidationException | StateValidationException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void removeOrder() throws FlooringMasteryPersistenceException {
        view.displayRemoveOrderBanner();
        LocalDate dateChoice = view.inputDate();
        view.displayDateBanner(dateChoice);
        try {
            view.displayDateOrders(service.getOrders(dateChoice));
            int orderNumber = view.inputOrderNumber();
            Order o = service.getOrder(dateChoice, orderNumber);
            view.displayRemoveOrderBanner();
            view.displayOrder(o);
            String response = view.askRemove();
            if (response.equalsIgnoreCase("Y")) {
                service.removeOrder(o);
                view.displayRemoveOrderSuccess(true, o);
            } else if (response.equalsIgnoreCase("N")) {
                view.displayRemoveOrderSuccess(false, o);
            } else {
                unknownCommand();
            }
        } catch (InvalidOrderNumberException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
    private void exportData()throws FlooringMasteryPersistenceException {
        service.exportOrders();
        view.displaySucceeeExportDataBanner();
    }
}

