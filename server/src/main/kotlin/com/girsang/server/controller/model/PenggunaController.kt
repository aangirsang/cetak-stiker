package com.girsang.server.controller.model

import com.girsang.server.model.Pengguna
import com.girsang.server.repository.PenggunaRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/pengguna")
class PenggunaController (private val repo: PenggunaRepository){
    @GetMapping fun semuaPengguna(): List<Pengguna> = repo.findAll()
    @PostMapping fun simpan(@RequestBody pengguna: Pengguna): Pengguna = repo.save(pengguna)
    @GetMapping("/ping") fun ping() = mapOf("status" to "ok")
}