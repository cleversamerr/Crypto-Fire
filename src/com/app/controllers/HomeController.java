package com.app.controllers;

import com.app.models.Data;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    Label welcomingMsg, balance, currenciesNum, actDealsNum, finishedDealsNum, reportsNum;

    @FXML
    Button refresh;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Data.labels[0] = welcomingMsg;
        Data.labels[1] = balance;
        Data.labels[2] = currenciesNum;
        Data.labels[3] = actDealsNum;
        Data.labels[4] = finishedDealsNum;
        Data.labels[5] = reportsNum;
    }
}
