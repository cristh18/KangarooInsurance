package com.kangarooinsurance.kiapp.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kangarooinsurance.kiapp.model.Brand;
import com.kangarooinsurance.kiapp.model.DefaultResponse;
import com.kangarooinsurance.kiapp.model.VehicleRequest;
import com.kangarooinsurance.kiapp.service.AppService;
import com.kangarooinsurance.kiapp.service.impl.ServiceImplementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestAppController {

    @Autowired
    AppService appService;

    @RequestMapping(value = "/brands", method = RequestMethod.GET)
    public ResponseEntity<List<Brand>> getBrandsRFService() {
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

    @RequestMapping(value = "/vehicle", method = RequestMethod.POST)
    @ResponseBody
    public int sendVehicleData(@RequestBody String request) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(request).getAsJsonObject();
        VehicleRequest vehicleRequest = new VehicleRequest(jsonObject);
        appService.sendVehicleData(vehicleRequest);
        return 200;
    }


}