package com.girsang.client.controller.UI

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import java.net.URL
import java.util.ResourceBundle

class MainClientAppController : Initializable {

    @FXML private lateinit var lblStatusServer: Label
    @FXML public lateinit var lblWaktu: Label

    override fun initialize(p0: URL?, p1: ResourceBundle?) {

    }
}