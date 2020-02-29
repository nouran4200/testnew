package com.iti.chat.controller;

import com.iti.chat.delegate.RegiseterDelegate;
import com.iti.chat.exception.DuplicatePhoneException;
import com.iti.chat.model.Gender;
import com.iti.chat.model.User;
import com.iti.chat.util.Hashing;
import com.iti.chat.util.RegisterValidation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
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
    private Text confirmPasswordError;

    @FXML
    private Label submitResult;

    @FXML
    public void maleRadioButtonActionHandler(ActionEvent event) {
        event.consume();
        maleRadioButton.selectedProperty().setValue(true);
        femaleRadioButton.selectedProperty().setValue(false);

    }

    @FXML
    public void femaleRadioButtonActionHandler(ActionEvent event) {
        event.consume();
        maleRadioButton.selectedProperty().setValue(false);
        femaleRadioButton.selectedProperty().setValue(true);
    }

    @FXML
    public void submitButtonHandler() {
        RegisterValidation validator = new RegisterValidation();
        boolean firstNameValidation = validator.checkName(firstNameTextField.getText());
        boolean lastNameValidation = validator.checkName(lastNameTextField.getText());
        boolean phoneValidation = validator.checkPhone(phoneTextField.getText());
        boolean passwordValidation = validator.checkPass(passwordTextField.getText());
        boolean passwordMatchValidation = passwordTextField.getText().equals(confirmPasswordTextField.getText());

        boolean submit = true;
        if (!firstNameValidation) {
            firstNameError.setText("invalid Name");
            submit = false;
        }

        if (!lastNameValidation) {
            lastNameError.setText("invalid Name");
            submit = false;
        }
        if (!phoneValidation) {
            phoneNumberError.setText("invalid Phone");
            submit = false;
        }
        if (!passwordValidation) {
            passwordError.setText("invalid password");
            submit = false;
        }
        if (!passwordMatchValidation) {
            confirmPasswordError.setText("invalid password");
            submit = false;
        }
        if (submit) {
            int gender = Gender.FEMALE;
            if (maleRadioButton.selectedProperty().getValue() == true) {
                gender = Gender.MALE;
            }
            firstNameError.setText("");
            lastNameError.setText("");
            phoneNumberError.setText("");
            passwordError.setText("");
            confirmPasswordError.setText("");

            String defaultEmail = "";
            String defaultCountry = "Egypt";
            User user = new User(firstNameTextField.getText(),
                    lastNameTextField.getText(),
                    phoneTextField.getText(),
                    defaultEmail,
                    gender,
                    defaultCountry);
            user.setIsAddedFromServer(1);
            RegiseterDelegate regiseterDelegate = new RegiseterDelegate();
            try {
                User submittedUser = regiseterDelegate.register(user, Hashing.getSecurePassword(passwordTextField.getText()));
                if (submittedUser != null) {
                    submitResult.setText("Done successfully");
                }
            } catch (DuplicatePhoneException e) {
                submitResult.setText("Duplicate Phone");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        femaleRadioButton.selectedProperty().setValue(true);
    }
}
