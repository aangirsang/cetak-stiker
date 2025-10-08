package com.girsang.client.controller.UI

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.util.ResourceBundle

@Serializable
data class PenggunaDTO(
    val id: Long? = null,
    val namaPengguna: String,
    val namaAkun: String,
    val password: String
)
class PenggunaController : Initializable{

    private val client = HttpClient.newBuilder().build()
    private val json = Json { ignoreUnknownKeys = true }

    @FXML private lateinit var txtNamaPengguna: TextField
    @FXML private lateinit var txtNamaAkun: TextField
    @FXML private lateinit var txtPassword: TextField
    @FXML private lateinit var txtUlangPassword: TextField
    @FXML private lateinit var btnTutup: Button
    @FXML private lateinit var btnSimpan: Button

    private var clientController: MainClientAppController? = null
    private var parentController: MainClientAppController? = null

    fun setClienController(controller: MainClientAppController) {
        this.clientController = controller
    }
fun setParentController(controller: MainClientAppController) {
        this.parentController = controller
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        bersih()
        btnTutup.setOnAction {tutup()}
        btnSimpan.setOnAction{simpanPengguna()}
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
    fun simpanPengguna(){
        println("awal Simpan")
        val namaPengguna = txtNamaPengguna.text.trim()
        val namaAkun = txtNamaAkun.text.trim()
        val password = txtPassword.text.trim()
        try{
            val dto = PenggunaDTO(namaPengguna = namaPengguna,
                namaAkun = namaAkun,
                password = password)
            val body = json.encodeToString(dto)
            val builder = HttpRequest.newBuilder()
                .uri(URI.create("${clientController?.url}/api/pengguna"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
            clientController?.buildAuthHeader()?.let { builder.header("Authorization", it) }
            val req = builder.build()
            val resp = clientController?.makeRequest(req)
            println("mulai Simpan")
            if(resp?.statusCode() in 200..299){
                Platform.runLater {
                    txtNamaPengguna.clear()
                    txtNamaAkun.clear()
                    txtPassword.clear()
                    txtUlangPassword.clear()
                }

            }else {
                Platform.runLater {
                    clientController?.showError("Server returned ${resp?.statusCode()} : ${resp?.body()}")
                }
            }
        }catch (ex: Exception) {
            Platform.runLater {
                clientController?.showError(ex.message ?: "Error")
                println("error Simpan")
            }
        }
    }
}