package com.iti.chat.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class NewContactController implements Initializable {
    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField confirmPasswordTextField;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    //error labels
    @FXML
    private Text firstNameError;

    @FXML
    private Text lastNameError;

    @FXML
    private Text phoneNumberError;

    @FXML
    private Text passwordError;

    @FXML
    private Text genderError;

    @FXML
    private Text confirmPasswordError;

    @FXML
    public void maleRadioButtonActionHandler(ActionEvent event){
        event.consume();
        maleRadioButton.selectedProperty().setValue(true);
        femaleRadioButton.selectedProperty().setValue(false);

    }
    @FXML
    public void femaleRadioButtonActionHandler(ActionEvent event){
        event.consume();
        maleRadioButton.selectedProperty().setValue(false);
        femaleRadioButton.selectedProperty().setValue(true);
    }
    @FXML
    public void submitButtonHandler(){

    }
    @FXML
    public void cancelButtonActionHandler(){

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
