package com.girsang.server.controller.model

import com.girsang.server.model.Product
import com.girsang.server.repository.ProductRepository
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/products")
class ProductController(private val repo: ProductRepository) {
    @GetMapping
    fun list(): List<Product> = repo.findAll()


    @PostMapping
    fun create(@RequestBody p: Product): Product = repo.save(p)


    @GetMapping("/ping")
    fun ping() = mapOf("status" to "ok")
}