package com.girsang.client

import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Base64
import kotlin.concurrent.thread

@Serializable
data class ProductDTO(
    val id: Long? = null,
    val name: String,
    val price: Double
)

class MainClientApp : Application() {
    private val client = HttpClient.newBuilder().build()
    private val json = Json { ignoreUnknownKeys = true }

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Client - Product Manager"

        // --- UI Components ---
        val serverUrlField = TextField("http://localhost:8080").apply { prefWidth = 320.0 }
        val usernameField = TextField().apply { promptText = "username (admin)" }
        val passwordField = PasswordField().apply { promptText = "password (secret)" }
        usernameField.text = "admin"
        passwordField.text = "secret"
        val useAuth = CheckBox("Use Basic Auth")
        val authBox = HBox(8.0, Label("Auth:"), usernameField, passwordField, useAuth)

        val loadBtn = Button("Load Products")
        val pingBtn = Button("Ping Server")
        val addName = TextField().apply { promptText = "Product name" }
        val addPrice = TextField().apply { promptText = "Price" }
        val addBtn = Button("Add Product")
        val listView = ListView<String>()
        val statusLabel = Label("Ready")

        // --- Utility ---
        fun buildAuthHeader(): String? {
            if (!useAuth.isSelected) return null
            val user = usernameField.text.trim()
            val pass = passwordField.text
            if (user.isEmpty()) return null
            val token = Base64.getEncoder().encodeToString("$user:$pass".toByteArray())
            return "Basic $token"
        }

        fun makeRequest(req: HttpRequest): HttpResponse<String> =
            client.send(req, HttpResponse.BodyHandlers.ofString())

        // --- Load Products ---
        loadBtn.setOnAction {
            val url = serverUrlField.text.trim().removeSuffix("/")
            statusLabel.text = "Loading..."
            thread {
                try {
                    val builder = HttpRequest.newBuilder()
                        .uri(URI.create("$url/api/products"))
                        .GET()
                    buildAuthHeader()?.let { builder.header("Authorization", it) }
                    val req = builder.build()
                    val resp = makeRequest(req)

                    if (resp.statusCode() == 200) {
                        val arr = json.decodeFromString<List<ProductDTO>>(resp.body())
                        Platform.runLater {
                            listView.items.clear()
                            listView.items.addAll(arr.map { "${it.id} - ${it.name} : ${it.price}" })
                            statusLabel.text = "Loaded ${arr.size} products"
                        }
                    } else {
                        Platform.runLater {
                            statusLabel.text = "Error ${resp.statusCode()}"
                            showError("Server returned ${resp.statusCode()} : ${resp.body()}")
                        }
                    }
                } catch (ex: Exception) {
                    Platform.runLater {
                        statusLabel.text = "Error"
                        showError(ex.message ?: "Error")
                    }
                }
            }
        }

        // --- Ping Server ---
        pingBtn.setOnAction {
            val url = serverUrlField.text.trim().removeSuffix("/")
            statusLabel.text = "Pinging..."
            thread {
                try {
                    val builder = HttpRequest.newBuilder()
                        .uri(URI.create("$url/api/products/ping"))
                        .GET()
                    buildAuthHeader()?.let { builder.header("Authorization", it) }
                    val req = builder.build()
                    val resp = makeRequest(req)
                    Platform.runLater { statusLabel.text = "Ping: ${resp.statusCode()} - ${resp.body()}" }
                } catch (ex: Exception) {
                    Platform.runLater {
                        statusLabel.text = "Ping failed"
                        showError(ex.message ?: "Error")
                    }
                }
            }
        }

        // --- Add Product ---
        addBtn.setOnAction {
            val url = serverUrlField.text.trim().removeSuffix("/")
            val name = addName.text.trim()
            val price = addPrice.text.trim().toDoubleOrNull()

            if (name.isEmpty() || price == null) {
                showError("Masukkan nama dan harga yang valid")
                return@setOnAction
            }

            statusLabel.text = "Adding..."
            thread {
                try {
                    val dto = ProductDTO(name = name, price = price)
                    val body = json.encodeToString(dto)
                    val builder = HttpRequest.newBuilder()
                        .uri(URI.create("$url/api/products"))
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .header("Content-Type", "application/json")
                    buildAuthHeader()?.let { builder.header("Authorization", it) }
                    val req = builder.build()
                    val resp = makeRequest(req)

                    if (resp.statusCode() in 200..299) {
                        Platform.runLater {
                            addName.clear()
                            addPrice.clear()
                            statusLabel.text = "Product Added"
                            loadBtn.fire()
                        }
                    } else {
                        Platform.runLater {
                            statusLabel.text = "Error ${resp.statusCode()}"
                            showError("Server returned ${resp.statusCode()} : ${resp.body()}")
                        }
                    }
                } catch (ex: Exception) {
                    Platform.runLater {
                        statusLabel.text = "Error"
                        showError(ex.message ?: "Error")
                    }
                }
            }
        }

        // --- Layout ---
        val top = HBox(10.0, Label("Server URL:"), serverUrlField, loadBtn, pingBtn)
        val addRow = HBox(10.0, addName, addPrice, addBtn)
        val vbox = VBox(10.0, top, authBox, addRow, listView, statusLabel)
        vbox.padding = Insets(12.0)

        primaryStage.scene = Scene(vbox, 820.0, 500.0)
        primaryStage.show()
    }

    private fun showError(msg: String) {
        Platform.runLater {
            Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait()
        }
    }
}

fun main() = Application.launch(MainClientApp::class.java)
