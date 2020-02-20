/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iti.chat.controller;

import com.iti.chat.service.SessionServiceProvider;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

/**
 *
 * @author Mac
 */
public class AnnouncmentController implements Initializable{

    @FXML
    private JFXButton sendButton;
    @FXML
    private JFXTextArea textArea;
    
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sendButton.setOnAction(ae -> {
            try {
                SessionServiceProvider ssp = SessionServiceProvider.getInstance();
                ssp.getClientService().values().forEach(client -> {;
                    try {
                        System.out.println(client);
                        client.recieveAnnouncment(textArea.getText());
                    } catch (RemoteException ex) {
                        Logger.getLogger(AnnouncmentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    textArea.clear();
                });
                
            } catch (RemoteException ex) {
                Logger.getLogger(AnnouncmentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }
}
