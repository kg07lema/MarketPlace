package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.CategoryCreationDto
import com.invictoprojects.marketplace.dto.CategoryDto
import com.invictoprojects.marketplace.dto.MappingUtils
import com.invictoprojects.marketplace.dto.ProductDto
import com.invictoprojects.marketplace.service.CategoryService
import com.invictoprojects.marketplace.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService,
    private val productService: ProductService
) {

    @GetMapping("/{id}")
    @ResponseBody
    fun getCategory(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val category = categoryService.findById(id)
            val result = MappingUtils.convertToDto(category)
            ResponseEntity(result, HttpStatus.OK)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping
    @ResponseBody
    fun getAllCategories(@RequestParam(defaultValue = "0") page: Int,
                         @RequestParam(name = "per_page", defaultValue = "30") perPage: Int): ResponseEntity<List<CategoryDto>> {
        val categories = categoryService.findAllPageable(page, perPage)
            .map { category -> MappingUtils.convertToDto(category) }
            .toList()
        return ResponseEntity.ok()
            .body(categories)
    }

    @GetMapping("/{id}/products")
    @ResponseBody
    fun getCategoryProducts(@PathVariable id: Long): ResponseEntity<List<ProductDto>> {
        val products = productService.findByCategoryId(id)
            .map { product -> MappingUtils.convertToDto(product) }
            .toList()

        return ResponseEntity.ok()
            .body(products)
    }

    @PostMapping
    @ResponseBody
    fun createCategory(@Valid @RequestBody categoryCreationDto: CategoryCreationDto): ResponseEntity<Any> {
        return try {
            val category = MappingUtils.convertToEntity(categoryCreationDto)
            val createdCategory = categoryService.create(category)
            val result = MappingUtils.convertToDto(createdCategory)
            ResponseEntity(result, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.CONFLICT)
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun updateCategory(@PathVariable id: Long, @RequestBody categoryCreationDto: CategoryCreationDto): ResponseEntity<Any> {
        return try {
            val category = MappingUtils.convertToEntity(categoryCreationDto)
            category.id = id
            val updatedCategory = categoryService.update(category)
            val result = MappingUtils.convertToDto(updatedCategory)
            ResponseEntity(result, HttpStatus.OK)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            categoryService.deleteById(id)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.NOT_FOUND)
        }
    }

}
