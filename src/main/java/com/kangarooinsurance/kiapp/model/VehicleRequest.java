package com.kangarooinsurance.kiapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicleRequest implements Serializable{

    @SerializedName("referrer")
    @Expose
    private String referrer;
    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("modelYear")
    @Expose
    private String modelYear;
    @SerializedName("model")
    @Expose
    private String model;

    public VehicleRequest(String referrer, String platform, String brand, String modelYear, String model) {
        this.referrer = referrer;
        this.platform = platform;
        this.brand = brand;
        this.modelYear = modelYear;
        this.model = model;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "VehicleRequest{" +
                "referrer='" + referrer + '\'' +
                ", platform='" + platform + '\'' +
                ", brand='" + brand + '\'' +
                ", modelYear='" + modelYear + '\'' +
                ", model='" + model + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
