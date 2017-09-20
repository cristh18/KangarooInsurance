package com.kangarooinsurance.kiapp.service;

import com.kangarooinsurance.kiapp.model.VehicleRequest;

public interface AppService {
    void getHomeService();

    void getBrandYears(String brand);

    void getReference(String brand, String year);

    void sendVehicleData(VehicleRequest vehicleRequest);
}
