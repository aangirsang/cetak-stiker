package com.girsang.client.controller.UI

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import java.net.URL
import java.util.*

class MainClientAppController : Initializable {

    @FXML private lateinit var mainPane: BorderPane
    @FXML lateinit var lblWaktu: Label
    @FXML private lateinit var lblStatusServer: Label
    @FXML private lateinit var mnPengguna: MenuItem

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        mnPengguna.setOnAction { tampilFormPengguna() }
    }

    private fun tampilFormPengguna() {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/fxml/pengguna.fxml"))
        val content: AnchorPane = fxmlLoader.load()
        val controller = fxmlLoader.getController<PenggunaController>()
        controller.setParentController(this) // kirim parent controller
        mainPane.center = content
    }
    fun tutupForm() {
        mainPane.center = null
    }
}
