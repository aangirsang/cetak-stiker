package com.girsang.server.controller.model

import com.girsang.server.model.DataUMKM
import com.girsang.server.repository.DataUMKMRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/data-umkm")
class DataUMKMController (private val repo: DataUMKMRepository){
    @GetMapping fun semuaDataUMKM(): List<DataUMKM> = repo.findAll()

    @GetMapping("/{id}")
    fun cariId(@PathVariable id: Long): DataUMKM = repo.findById(id).orElseThrow()

    @PostMapping
    fun simpan(@RequestBody dataUMKM: DataUMKM): DataUMKM = repo.save(dataUMKM)

    @PutMapping("/{id}")
    fun ubah(@PathVariable id: Long, @RequestBody dataUMKM: DataUMKM): DataUMKM{
        val existing = repo.findById(id).orElseThrow()
        val ubah = existing.copy(
            namaPemilik = dataUMKM.namaPemilik,
            namaUsaha = dataUMKM.namaUsaha,
            whatsApp = dataUMKM.whatsApp,
            instagram = dataUMKM.instagram,
            alamat = dataUMKM.alamat)
        return  repo.save(ubah)
    }
    @DeleteMapping("/{id}")
    fun hapus(@PathVariable id: Long) = repo.deleteById(id)
}