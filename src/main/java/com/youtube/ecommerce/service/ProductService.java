package com.youtube.ecommerce.service;

import com.youtube.ecommerce.configuration.JwtRequestFilter;
import com.youtube.ecommerce.dao.CartDao;
import com.youtube.ecommerce.dao.ProductDao;
import com.youtube.ecommerce.dao.UserDao;
import com.youtube.ecommerce.entity.Cart;
import com.youtube.ecommerce.entity.Category;
import com.youtube.ecommerce.entity.Product;
import com.youtube.ecommerce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CartDao cartDao;
    
    @Autowired
    private CategoryService categoryService;
    
    public void initProduct() {
        Category categoryMaglie = categoryService.getByCategoryId(1); // esempio di recupero della categoria
        Category categoryFelpa = categoryService.getByCategoryId(2);
        
        Product p1 = new Product();
        p1.setProductName("Maglia Rossa");
        p1.setProductDescription("Una maglia rossa di alta qualità");
        p1.setProductDiscountedPrice(19.99);
        p1.setProductActualPrice(29.99);
        p1.setCategory(categoryMaglie);
        
        Product p2 = new Product();
        p2.setProductName("Felpa Blu");
        p2.setProductDescription("Una felpa blu comoda e calda");
        p2.setProductDiscountedPrice(29.99);
        p2.setProductActualPrice(39.99);
        p2.setCategory(categoryFelpa);
        
        // Continua ad aggiungere altri prodotti simili
        // Aggiungi altri prodotti qui...
        
        productDao.save(p1);
        productDao.save(p2);
        // Salva anche gli altri prodotti...
    }

    public Product addNewProduct(Product product) throws Exception {
          return productDao.save(product);
    }

    public Page<Product> getAllProducts(Pageable pageable, String searchKey) {
        if (searchKey == null || searchKey.isEmpty()) {
            return productDao.findAll(pageable);
        } else {
            return productDao.findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
                searchKey, searchKey, pageable
            );
        }
    }

    public Product getProductDetailsById(Integer productId) {
        return productDao.findById(productId).get();
    }

    public void deleteProductDetails(Integer productId) {
        productDao.deleteById(productId);
    }

    public List<Product> getProductDetails(boolean isSingleProductCheckout, Integer productId) {
        if(isSingleProductCheckout && productId != 0) {
            // we are going to buy a single product

            List<Product> list = new ArrayList<>();
            Product product = productDao.findById(productId).get();
            list.add(product);
            return list;
        } else {
            // we are going to checkout entire cart
            String username = JwtRequestFilter.CURRENT_USER;
            User user = userDao.findById(username).get();
            List<Cart> carts = cartDao.findByUser(user);

            return carts.stream().map(x -> x.getProduct()).collect(Collectors.toList());
        }
    }

    public List<Product> getProductsByCategory(String categoryName) {
        return productDao.findByCategory_CategoryName(categoryName);
    }

    public void deleteProduct(Integer productId) {
    	productDao.deleteById(productId);
    }

    @Transactional
    public Product updateProduct(Integer productId, Product productDetails) {
        Product product = productDao.findById(productId).orElseThrow(() -> new RuntimeException("Prodotto non trovato"));
        product.setProductName(productDetails.getProductName());
        product.setProductDescription(productDetails.getProductDescription());
        product.setProductDiscountedPrice(productDetails.getProductDiscountedPrice());
        product.setProductActualPrice(productDetails.getProductActualPrice());
        product.setCategory(productDetails.getCategory());
        return productDao.save(product);
    }
    
    public List<Product> getRandomProducts(int count) {
        // Ottieni il numero totale di prodotti disponibili
        long totalProducts = productDao.count();
        
        // Se count è maggiore del numero di prodotti disponibili, regola count
        if (count > totalProducts) {
            count = (int) totalProducts;
        }
        
        // Restituisci i prodotti casuali
        return count > 0 ? productDao.findRandomProducts(count) : Collections.emptyList();
    }
}
