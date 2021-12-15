/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryStateDao;
import com.sg.flooringmastery.dto.State;
import java.math.BigDecimal;

/**
 * @author Sweetlana Protsenko 
 * email: svitprotsenko@gmail.com
 */
public class FlooringMasteryStateDaoStubImpl implements FlooringMasteryStateDao {
    
    public State onlyState;
    
    public FlooringMasteryStateDaoStubImpl() {
        //String stateAbbr = "TX";
        State onlystate = new State();
        onlystate.setStateAbbr("TX");
        onlystate.setStateName("Texas");
        onlystate.setTaxRate(BigDecimal.valueOf(4.45));
        
    }
    public FlooringMasteryStateDaoStubImpl (State testState) {
        this.onlyState = testState;
        
    }

    @Override
    public State getState(String stateAbbr) throws FlooringMasteryPersistenceException {
        if(stateAbbr.equals(onlyState.getStateAbbr())) {
            return onlyState;
        }else {
            return null;
        }
    }
    
}
