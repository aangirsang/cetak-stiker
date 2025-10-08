package com.girsang.client.controller.UI

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import kotlin.concurrent.thread

class MainClientAppController : Initializable {

    private val client = HttpClient.newBuilder().build()
    private val json = Json { ignoreUnknownKeys = true }

    @FXML private lateinit var mainPane: BorderPane
    @FXML lateinit var lblWaktu: Label
    @FXML private lateinit var lblStatusServer: Label
    @FXML private lateinit var mnPengguna: MenuItem

    val user = "admin"
    val pass = "secret"
    val url = "http://localhost:8080"

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        mnPengguna.setOnAction { tampilFormPengguna() }
        pingServer()
    }

    private fun tampilFormPengguna() {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/fxml/pengguna.fxml"))
        val content: AnchorPane = fxmlLoader.load()
        val controller = fxmlLoader.getController<PenggunaController>()
        controller.setParentController(this)     // sudah ada ✅
        controller.setClienController(this)  // kirim parent controller
        mainPane.center = content
    }
    fun tutupForm() {
        mainPane.center = null
    }

    fun pingServer(){
        val url = url.trim().removeSuffix("/")
        lblStatusServer.text = "Pinging..."
        thread {
            try {
                val builder = HttpRequest.newBuilder()
                    .uri(URI.create("$url/api/pengguna/ping"))
                    .GET()
                buildAuthHeader()?.let { builder.header("Authorization", it) }
                val req = builder.build()
                val resp = makeRequest(req)
                Platform.runLater { lblStatusServer.text = "Aktif - Ping: ${resp.statusCode()} - ${resp.body()}" }
            } catch (ex: Exception) {
                Platform.runLater {
                    lblStatusServer.text = "Ping failed"
                    showError(ex.message ?: "Error")
                }
            }
        }
    }

    fun buildAuthHeader(): String? {
        val token = Base64.getEncoder().encodeToString("$user:$pass".toByteArray())
        return "Basic $token"
    }

    fun makeRequest(req: HttpRequest): HttpResponse<String> =
        client.send(req, HttpResponse.BodyHandlers.ofString())

    fun showError(msg: String) {
        Platform.runLater {
            Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait()
        }
    }
}
