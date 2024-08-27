package com.youtube.ecommerce.controller;

import com.youtube.ecommerce.entity.Category;
import com.youtube.ecommerce.entity.ImageModel;
import com.youtube.ecommerce.entity.Product;
import com.youtube.ecommerce.service.CategoryService;
import com.youtube.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @PostConstruct
    public void initRoleAndUser() {
    	productService.initProduct();
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping(value = {"/addNewProduct"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Product> addNewProduct(@RequestPart("product") Product product,
                                                 @RequestPart("categoryId") Integer categoryId,
                                                 @RequestPart("imageFile") MultipartFile[] file) {
        try {
            Set<ImageModel> images = uploadImage(file);
            product.setProductImages(images);
            Category category = categoryService.getByCategoryId(categoryId);
            product.setCategory(category);
            Product savedProduct = productService.addNewProduct(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();

        for (MultipartFile file: multipartFiles) {
            ImageModel imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }

        return imageModels;
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(defaultValue = "") String searchKey) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Product> products = productService.getAllProducts(pageable, searchKey);
            return new ResponseEntity<>(products.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/getProductDetailsById/{productId}"})
    public Product getProductDetailsById(@PathVariable("productId") Integer productId) {
        return productService.getProductDetailsById(productId);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping({"/deleteProductDetails/{productId}"})
    public void deleteProductDetails(@PathVariable("productId") Integer productId) {
        productService.deleteProductDetails(productId);
    }

    @PreAuthorize("hasRole('User')")
    @GetMapping({"/getProductDetails/{isSingleProductCheckout}/{productId}"})
    public List<Product> getProductDetails(@PathVariable(name = "isSingleProductCheckout" ) boolean isSingleProductCheckout,
                                           @PathVariable(name = "productId")  Integer productId) {
        return productService.getProductDetails(isSingleProductCheckout, productId);
    }
    
    @GetMapping("/getProductsByCategory/{categoryName}")
    public ResponseEntity<List<Product>> getProductsByCategory(
        @PathVariable String categoryName,
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(defaultValue = "") String searchKey) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Product> products = productService.getProductsByCategory(categoryName, searchKey, pageable);
            return new ResponseEntity<>(products.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Integer productId,
                                                  @RequestBody Product productDetails) {
        try {
            // Assuming that productService.updateProduct() will handle the update based on the productId
            Product updatedProduct = productService.updateProduct(productId, productDetails);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println("Errore durante l'eliminazione del prodotto: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getRandomProducts")
    public ResponseEntity<List<Product>> getRandomProducts(@RequestParam(defaultValue = "5") int count) {
        try {
            List<Product> randomProducts = productService.getRandomProducts(count);
            return new ResponseEntity<>(randomProducts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
