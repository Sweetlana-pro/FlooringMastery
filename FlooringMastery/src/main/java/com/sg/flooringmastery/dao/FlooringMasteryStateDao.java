/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.State;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import java.math.BigDecimal;

/**
 *
 * @author pro
 */
public interface FlooringMasteryStateDao {
    State getState(String stateAbbr) throws FlooringMasteryPersistenceException;

}

