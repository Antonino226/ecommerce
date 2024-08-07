package com.youtube.ecommerce.dao;

import com.youtube.ecommerce.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends PagingAndSortingRepository<Category, Integer> {
    Category findByCategoryName(String categoryName);

	Category findByCategoryId(Integer categoryId);
	
	Page<Category> findAll(Pageable pageable);

}