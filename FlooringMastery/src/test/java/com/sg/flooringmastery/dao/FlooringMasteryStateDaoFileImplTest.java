/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.State;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Sweetlana Protsenko
 */
public class FlooringMasteryStateDaoFileImplTest {
    
    FlooringMasteryStateDao testStateDao;
    
    public FlooringMasteryStateDaoFileImplTest() {
    }
    
    
    @BeforeEach
    public void setUp() throws Exception {
        String testTaxesFile = "testTaxes.txt";
        new FileWriter (testTaxesFile);
        testStateDao = new FlooringMasteryStateDaoFileImpl(testTaxesFile);
    PrintWriter out;

	    try {
	        out = new PrintWriter(new FileWriter(testTaxesFile));
	    } catch (IOException e) {
	        throw new FlooringMasteryPersistenceException(
	                "Could not save data.", e);
	    }
            out.println("TX,Texas,4.45");
            out.println("WA,Washington,9.25");
            out.println("KY,Kentucky,6.00");
            out.println("CA,California,25.00");
            out.flush();
            out.close();
        
    }
    
    @Test
    public void getState ()throws Exception {
                
        //Arrange our method test inputs
        String stateAbbr = "TX";
        State state = new State();
        state.setStateAbbr("TX");
        state.setStateName("Texas");
        state.setTaxRate(BigDecimal.valueOf(4.45));
        
                        
        //Act: getting the state from the DAO
        State retrievedState = testStateDao.getState(stateAbbr);
        
        //Assert the data is equal
        assertEquals(state.getStateAbbr(), retrievedState.getStateAbbr(),
                "Checking matching the state Abbreviation");
        //Assert the data exists
        assertNotNull(state.getStateAbbr(), "State must not be null");
        //Assert the Abbraeviation is used        
        assertEquals(2, state.getStateAbbr().length(), 
                "Checking the abbreviation has only 2 letters");
        
        
    }
    
}
