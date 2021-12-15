/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.State;
import com.sg.flooringmastery.service.FlooringMasteryPersistenceException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author 
 */
public class FlooringMasteryStateDaoFileImpl implements FlooringMasteryStateDao {
    private final String STATE_FILE;
    public static final String DELIMITER = ",";
    
    public FlooringMasteryStateDaoFileImpl() {
        STATE_FILE = "Taxes.txt";
    }
    
    public FlooringMasteryStateDaoFileImpl(String taxesTextFile) {
        STATE_FILE = taxesTextFile;
    }

    //private List<State> states = new ArrayList();

    

    @Override
    public State getState(String stateAbbr) throws FlooringMasteryPersistenceException {
        List<State> states = loadStates();
        if (states == null) {
            return null;
        } else {
            State chosenState = states.stream()
                    .filter(s -> s.getStateAbbr().equalsIgnoreCase(stateAbbr))
                    .findFirst().orElse(null);
            return chosenState;
        }
    }

    private List<State> loadStates() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        List<State> states = new ArrayList<>();

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(STATE_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException(
                    "-_- Could not load states data into memory.", e);
        }

        String currentLine;
        String[] currentTokens;
        
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            if (currentTokens.length == 3) {
                State currentState = new State();
                currentState.setStateAbbr(currentTokens[0]);
                currentState.setStateName(currentTokens[1]);
                currentState.setTaxRate(new BigDecimal(currentTokens[2]));
                // Put currentState into the map using stateAbbr as the key
                states.add(currentState);
            } else {
                //Ignores line if delimited wrong or empty.
            }
        }
        scanner.close();

        if (!states.isEmpty()) {
            return states;
        } else {
            return null;
        }
    }
}
