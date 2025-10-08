package com.girsang.client.controller.UI

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import java.net.URL
import java.util.ResourceBundle

class PenggunaController : Initializable{

    @FXML private lateinit var txtNamaPengguna: TextField
    @FXML private lateinit var txtNamaAkun: TextField
    @FXML private lateinit var txtPassword: TextField
    @FXML private lateinit var txtUlangPassword: TextField
    @FXML private lateinit var btnTutup: Button

    private var parentController: MainClientAppController? = null

    fun setParentController(controller: MainClientAppController) {
        this.parentController = controller
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        bersih()
        btnTutup.setOnAction {
            parentController?.tutupForm()
        }
    }
    fun bersih(){
        txtNamaPengguna.clear()
        txtNamaAkun.clear()
        txtPassword.clear()
        txtUlangPassword.clear()
    }
    fun tutup(){
        parentController?.tutupForm()

    }
}