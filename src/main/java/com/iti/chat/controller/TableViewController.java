package com.iti.chat.controller;

import com.iti.chat.dao.UserDAOImpl;
import com.iti.chat.model.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


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



    UserDAOImpl dao = UserDAOImpl.getInstance();
    List<User> users = dao.getAllUsers();

    public TableViewController() throws SQLException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<User> oblist  = FXCollections.observableArrayList(users);

        firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        countryCol.setCellValueFactory(new PropertyValueFactory<User, String>("country"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<User, String>("phone"));
        tableView.setItems(oblist);

//        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection != null) {
//                System.out.println(newSelection);
//            }
//        });

           updateButton.setOnAction(ae -> {
            String firstname =  tableView.getSelectionModel().selectedItemProperty().getValue().getFirstName();
            String lastname =  tableView.getSelectionModel().selectedItemProperty().getValue().getLastName();
            String emailuser =  tableView.getSelectionModel().selectedItemProperty().getValue().getEmail();
            String phoneuser =  tableView.getSelectionModel().selectedItemProperty().getValue().getPhone();

            firstName.setText(firstname);
            lastName.setText(lastname);
            email.setText(emailuser);
            phone.setText(phoneuser);


        });
        saveButton.setOnAction(ae -> {
            User userupdated =  tableView.getSelectionModel().selectedItemProperty().getValue();
            userupdated.setFirstName(firstName.getText());
            userupdated.setLastName(lastName.getText());
            userupdated.setEmail(email.getText());
            userupdated.setPhone(phone.getText());
            dao.updateInfo(userupdated);
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
}
