/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iti.chat.dao;

import java.util.Map;

/**
 *
 * @author Mac
 */
public interface StatisticsDAO {
    Map<Integer, Integer> genderStats();
    Map<String, Integer> countriesStats();
    int allUsersCount();
}
