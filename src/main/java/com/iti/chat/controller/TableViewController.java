package com.iti.chat.controller;

import com.iti.chat.dao.impl.UserDAOImpl;
import com.iti.chat.util.JFXCountryComboBox;
import com.iti.chat.model.User;
import com.iti.chat.util.RegisterValidation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class TableViewController implements Initializable {
@FXML
private TableView<User> tableView;
@FXML
private TableColumn<User, String> firstNameCol;
@FXML
private TableColumn<User,String> lastNameCol ;
@FXML
private  TableColumn<User,String> emailCol ;
@FXML
private TableColumn<User,String> countryCol ;
@FXML
private TableColumn<User,String> phoneCol;
@FXML
TableColumn<User,String> bdateCol;
@FXML
 JFXButton updateButton ;
@FXML
JFXTextField firstName ;
@FXML
JFXTextField lastName ;
@FXML
JFXTextField email ;
@FXML
JFXTextField phone ;
@FXML
JFXButton saveButton;
@FXML
JFXButton deleteButton;
@FXML
ComboBox countryCombo ;
@FXML
Text firstError;
@FXML
Text lastError;
@FXML
Text phoneError;
@FXML
Text emailError;
    @FXML
    Text errorSubmit;


    UserDAOImpl dao = UserDAOImpl.getInstance();
    List<User> users = dao.getAllUsers();

    public TableViewController() throws SQLException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addCountries();


        ObservableList<User> oblist  = FXCollections.observableArrayList(users);

        firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        countryCol.setCellValueFactory(new PropertyValueFactory<User, String>("country"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<User, String>("phone"));
        bdateCol.setCellValueFactory(new PropertyValueFactory<User, String>("birthDate"));

        tableView.setItems(oblist);



           updateButton.setOnAction(ae -> {

            if (tableView.getSelectionModel().selectedItemProperty().getValue() == null)
            {
                errorSubmit.setText("No Data to Update");
                return;
            }
            String firstname =  tableView.getSelectionModel().selectedItemProperty().getValue().getFirstName();
            String lastname =  tableView.getSelectionModel().selectedItemProperty().getValue().getLastName();
            String emailuser =  tableView.getSelectionModel().selectedItemProperty().getValue().getEmail();
            String coutry = tableView.getSelectionModel().selectedItemProperty().getValue().getCountry();

            firstName.setText(firstname);
            lastName.setText(lastname);
            email.setText(emailuser);
            countryCombo.setValue(coutry);


        });
        saveButton.setOnAction(ae -> {
            RegisterValidation validator = new RegisterValidation();
            boolean firstNameValidation = validator.checkName(firstName.getText());
            boolean lastNameValidation = validator.checkName(lastName.getText());
            int emailValdiation = validator.checkEmail(email.getText());

            boolean submit = true;


            if (!firstNameValidation  ) {
                firstError.setText("invalid Name");
                submit = false;
            }

            if (!lastNameValidation) {
                lastError.setText("invalid Name");
                submit = false;
            }

            if (emailValdiation == validator.INVALID) {
                emailError.setText("invalid email");
                submit = false;
            }




            if (submit == true) {
                User userupdated =  tableView.getSelectionModel().selectedItemProperty().getValue();
                userupdated.setFirstName(firstName.getText());
                userupdated.setLastName(lastName.getText());
                userupdated.setEmail(email.getText());
                userupdated.setCountry(countryCombo.getValue().toString());

                firstError.setText("");
                lastError.setText("");
                emailError.setText("");
                errorSubmit.setText("");
                dao.updateInfo(userupdated);

            }
            else {
                errorSubmit.setText("failed update");

            }
            tableView.refresh();


        });

        deleteButton.setOnAction(ae -> {
            User userSelected =  tableView.getSelectionModel().selectedItemProperty().getValue();

            try {
                dao.deleteUser(userSelected);
                //tableView.refresh();
                List<User> newUsers = dao.getAllUsers();
                ObservableList<User> oblistNew  = FXCollections.observableArrayList(newUsers);

                tableView.setItems(oblistNew);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }
    private void addCountries() {
        JFXCountryComboBox country = new JFXCountryComboBox();
        country.addCountries(countryCombo);
    }
}
