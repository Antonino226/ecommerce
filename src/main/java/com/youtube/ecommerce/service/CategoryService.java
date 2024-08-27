package com.youtube.ecommerce.service;

import com.youtube.ecommerce.entity.Category;
import com.youtube.ecommerce.entity.Product;
import com.youtube.ecommerce.dao.CategoryDao;
import com.youtube.ecommerce.dao.ProductDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

import javax.transaction.Transactional;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    
    @Autowired
    private ProductDao productDao;
    
    public void initCategory() {
        Category c1 = new Category();
        c1.setCategoryName("Maglie");
        c1.setCategoryDescription("Descrizione delle maglie");

        Category c2 = new Category();
        c2.setCategoryName("Felpa");
        c2.setCategoryDescription("Descrizione delle felpe");

        Category c3 = new Category();
        c3.setCategoryName("Puzzle");
        c3.setCategoryDescription("Descrizione dei puzzle");

        Category c4 = new Category();
        c4.setCategoryName("Cuscino Cuore");
        c4.setCategoryDescription("Descrizione dei cuscini cuore");

        Category c5 = new Category();
        c5.setCategoryName("Plaid");
        c5.setCategoryDescription("Descrizione dei plaid");

        Category c6 = new Category();
        c6.setCategoryName("Tazza Magica");
        c6.setCategoryDescription("Descrizione delle tazze magiche");

        categoryDao.save(c1);
        categoryDao.save(c2);
        categoryDao.save(c3);
        categoryDao.save(c4);
        categoryDao.save(c5);
        categoryDao.save(c6);
    }
    
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryDao.findAll(pageable);
    }


    public Category getByCategoryId(Integer categoryId) {
        return categoryDao.findByCategoryId(categoryId);
    }

    
    public Category addCategory(Category category) {
        return categoryDao.save(category);
    }
    
    @Transactional
    public Category updateCategory(Integer categoryId, Category categoryDetails) {
        Category category = categoryDao.findById(categoryId).orElseThrow(() -> new RuntimeException("Categoria non trovata"));
        category.setCategoryName(categoryDetails.getCategoryName());
        category.setCategoryDescription(categoryDetails.getCategoryDescription());
        return categoryDao.save(category);
    }

    @Transactional
    public void deleteCategory(Integer categoryId, boolean deleteProducts) {
        if (deleteProducts) {
            // Elimina tutti i prodotti associati alla categoria
            List<Product> products = productDao.findByCategory_CategoryId(categoryId);
            for (Product product : products) {
                productDao.delete(product);
            }
        } else {
            // Rimuovi la categoria dai prodotti senza eliminarli
            List<Product> products = productDao.findByCategory_CategoryId(categoryId);
            for (Product product : products) {
                product.setCategory(null);
                productDao.save(product);
            }
        }
        // Ora elimina la categoria
        categoryDao.deleteById(categoryId);
    }
}
