package com.kangarooinsurance.kiapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangarooinsurance.kiapp.model.Brand;
import com.kangarooinsurance.kiapp.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestAppController {

    @Autowired
    AppService appService;

    private List<Brand> brands;

    public List<Brand> getBrands() {
        brands = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Brand brand = new Brand(("Adidas ".concat(String.valueOf(i))).toLowerCase(),
                    ("Adidas ".concat(String.valueOf(i))).toUpperCase());
            brands.add(brand);
        }
        return brands;
    }

    //    @RequestMapping("")
//    public List<Brand> getBrands() {
//        return appService.getHomeService();
//    }

    /**
     * HTTP GET - Get all employees
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Brand>> getAllEmployeesJSON() {
        List<Brand> brands = getBrands();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }


}