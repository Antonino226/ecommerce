package com.youtube.ecommerce.service;

import com.youtube.ecommerce.entity.SpecialOffer;
import com.youtube.ecommerce.dao.SpecialOfferDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialOfferService {

    @Autowired
    private SpecialOfferDao specialOfferDao;

    public SpecialOffer addSpecialOffer(SpecialOffer specialOffer) {
        return specialOfferDao.save(specialOffer);
    }

    public List<SpecialOffer> getAllSpecialOffers() {
        return specialOfferDao.findAll();
    }

    public SpecialOffer getSpecialOfferById(Integer offerId) {
        return specialOfferDao.findById(offerId).orElse(null);
    }

    public void deleteSpecialOffer(Integer offerId) {
    	specialOfferDao.deleteById(offerId);
    }
}
