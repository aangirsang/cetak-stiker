package com.girsang.server.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class DataUMKM (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var namaPemilik:  String,
    var namaUsaha:  String,
    var whatsApp:  String,
    var instagram:  String,
    var alamat:  String
)