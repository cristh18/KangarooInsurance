package com.kangarooinsurance.kiapp.controller;

import com.kangarooinsurance.kiapp.model.Brand;
import com.kangarooinsurance.kiapp.model.DefaultResponse;
import com.kangarooinsurance.kiapp.service.AppService;
import com.kangarooinsurance.kiapp.service.impl.ServiceImplementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestAppController {

    @Autowired
    AppService appService;

    /**
     * HTTP GET - Get all employees
     */
//    @RequestMapping(value = "/kiapp", method = RequestMethod.GET)
//    public ResponseEntity<List<Brand>> getAllEmployeesJSON() {
//        List<Brand> brands = getBrands();
//        return new ResponseEntity<>(brands, HttpStatus.OK);
//    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Brand>> getBrandsRFService() {
//        List<Brand> brands = getBrands();
        List<Brand> brands = ServiceImplementer.brandList;
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @RequestMapping(value = "/year/{brandId}", method = RequestMethod.GET)
    public ResponseEntity<List<DefaultResponse>> getBrandYearRFService(@PathVariable String brandId) {
        appService.getBrandYears(brandId);
        return new ResponseEntity<>(ServiceImplementer.yearList, HttpStatus.OK);
    }

    @RequestMapping(value = "/reference/{brandId}/{yearId}", method = RequestMethod.GET)
    public ResponseEntity<List<DefaultResponse>> getBrandYearRFService(@PathVariable String brandId, @PathVariable String yearId) {
        appService.getReference(brandId, yearId);
        return new ResponseEntity<>(ServiceImplementer.referenceList, HttpStatus.OK);
    }


}