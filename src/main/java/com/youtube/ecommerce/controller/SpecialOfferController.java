package com.youtube.ecommerce.controller;

import com.youtube.ecommerce.entity.SpecialOffer;
import com.youtube.ecommerce.service.SpecialOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specialoffers")
public class SpecialOfferController {

    @Autowired
    private SpecialOfferService specialOfferService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/add")
    public ResponseEntity<SpecialOffer> addSpecialOffer(@RequestBody SpecialOffer specialOffer) {
        try {
            SpecialOffer savedOffer = specialOfferService.addSpecialOffer(specialOffer);
            return new ResponseEntity<>(savedOffer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<SpecialOffer>> getAllSpecialOffers() {
        try {
            List<SpecialOffer> offers = specialOfferService.getAllSpecialOffers();
            return new ResponseEntity<>(offers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<SpecialOffer> getSpecialOfferById(@PathVariable Integer offerId) {
        try {
            SpecialOffer offer = specialOfferService.getSpecialOfferById(offerId);
            return new ResponseEntity<>(offer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteSpecialOffer(@PathVariable Integer offerId) {
        try {
            specialOfferService.deleteSpecialOffer(offerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
