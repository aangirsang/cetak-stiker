package com.girsang.client.UI

import com.girsang.client.controller.UI.MainClientAppController
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.util.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainClientApp : Application(){
    override fun start(stage: Stage) {
        val loader = FXMLLoader(javaClass.getResource("/FXML/main-client-app.fxml"))
        val root = loader.load<javafx.scene.layout.AnchorPane>()
        val controller = loader.getController<MainClientAppController>()



        val timeLabel = controller.lblWaktu
        val formatJam = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formatTanggal = DateTimeFormatter.ofPattern("dd MMMM yyyy")

        // Timeline untuk update setiap 1 detik
        val tanggal = LocalDate.now().format(formatTanggal)
        val timeline = Timeline(
            KeyFrame(Duration.seconds(1.0), {
                val jam = LocalTime.now().format(formatJam)
                timeLabel.text = "$tanggal   $jam"
            })
        )
        timeline.cycleCount = Timeline.INDEFINITE
        timeline.play()


        stage.title = "Aplikasi Client Data Cetak Stiker"
        stage.scene = Scene(root)
        stage.isMaximized = true
        stage.show()

        javafx.application.Platform.runLater {
            Thread.sleep(2000)
            controller.lblWaktu.text = ""
        }
    }
}

fun main() = Application.launch(MainClientApp::class.java)