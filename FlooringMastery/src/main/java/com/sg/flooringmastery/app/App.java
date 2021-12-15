/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.app;

import com.sg.flooringmastery.controller.FlooringMasteryController;
import com.sg.flooringmastery.dao.FlooringMasteryOrderDao;
import com.sg.flooringmastery.dao.FlooringMasteryOrderDaoFileImpl;

import com.sg.flooringmastery.dao.FlooringMasteryProductDao;
import com.sg.flooringmastery.dao.FlooringMasteryProductDaoFileImpl;
import com.sg.flooringmastery.dao.FlooringMasteryStateDao;
import com.sg.flooringmastery.dao.FlooringMasteryStateDaoFileImpl;
import com.sg.flooringmastery.service.FlooringMasteryDataValidationException;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;
import java.time.LocalDate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @Sweetlana Protsenko
 */
public class App {
    public static void main(String[] args) throws FlooringMasteryPersistenceException, FlooringMasteryDataValidationException {
        /*UserIO myIo = new UserIOConsoleImpl();
        FlooringMasteryView myView = new FlooringMasteryView(myIo);
        FlooringMasteryOrderDao myOrderDao = new FlooringMasteryOrderDaoFileImpl();
        FlooringMasteryProductDao myProductDao = new FlooringMasteryProductDaoFileImpl();
        FlooringMasteryStateDao myStateDao = new FlooringMasteryStateDaoFileImpl();
        FlooringMasteryServiceLayer myService = new FlooringMasteryServiceLayerImpl(myOrderDao, myProductDao, myStateDao);
        FlooringMasteryController controller =new FlooringMasteryController(myService, myView);
        */
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
              
        FlooringMasteryController controller = ctx.getBean("controller", FlooringMasteryController.class);
        controller.run();
    }
    
}
