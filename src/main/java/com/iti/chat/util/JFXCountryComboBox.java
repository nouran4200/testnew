package com.iti.chat.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.Locale;

public class JFXCountryComboBox {

    public void addCountries(ComboBox countryField) {
        ObservableList<String> cities = FXCollections.observableArrayList();
        String[] locales = Locale.getISOCountries();
        for (String countryList : locales) {
            Locale obj = new Locale("", countryList);
            String[] city = {obj.getDisplayCountry()};
            for (int x = 0; x < city.length; x++) {
                cities.add(obj.getDisplayCountry());
            }
        }
        countryField.setItems(cities);
    }
}
