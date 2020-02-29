/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iti.chat.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Mac
 */

public class AdminbarController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    JFXToggleButton serviceButton;
    @FXML
    JFXButton StatsButton;
    @FXML
    JFXButton announceButton;
    @FXML
    JFXButton addContactButton;
    @FXML
    JFXButton crudButton;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
