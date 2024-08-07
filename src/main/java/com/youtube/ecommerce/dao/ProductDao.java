package com.youtube.ecommerce.dao;

import com.youtube.ecommerce.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends PagingAndSortingRepository<Product, Integer> {
	Page<Product> findAll(Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
        String name, String description, Pageable pageable
    );
    
    List<Product> findByCategory_CategoryName(String categoryName);

	public void deleteByProductId(Long productId);
	
	List<Product> findByCategory_CategoryId(Integer categoryId);
	
	@Query(value = "SELECT * FROM product ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Product> findRandomProducts(@Param("limit") int limit);

    // Metodo per contare il numero totale di prodotti
    long count();
}
