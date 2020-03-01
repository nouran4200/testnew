/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iti.chat.controller;

import com.iti.chat.service.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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

    ServiceManager serviceManager;

    @FXML
    public void manageService() {
        if (serviceButton.isSelected()) {
            try {
                //serviceButton.setDisable(true);
                serviceManager.runServer();
                serviceButton.setDisable(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }else if(!serviceButton.isSelected()){
            try {
                //serviceButton.setDisable(true);
                serviceManager.closeServer();
                serviceButton.setDisable(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    }


        @Override
        public void initialize (URL url, ResourceBundle rb){
            serviceButton.setSelected(true);
            serviceManager = ServiceManager.getInstance();
        }

    }
