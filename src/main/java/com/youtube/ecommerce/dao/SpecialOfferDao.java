package com.youtube.ecommerce.dao;

import com.youtube.ecommerce.entity.SpecialOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialOfferDao extends JpaRepository<SpecialOffer, Integer> {
}
