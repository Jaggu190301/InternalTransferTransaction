package com.example.internaltransfertransaction;

import com.google.gson.annotations.SerializedName;

public class PostDataModel {
    @SerializedName("DriverID")
    private int driverID;

    @SerializedName("Date")
    private String Date;

    @SerializedName("VehicleID")
    private int VehicleID;

    @SerializedName("From_Place")
    private String From_Place;

    @SerializedName("To_Place")
    private String To_Place;

    @SerializedName("CoilID")
    private String CoilID;

    @SerializedName("Tonnage")
    private String Tonnage;

    @SerializedName("PagerefNo")
    private String PagerefNo;

    @SerializedName("remarks")
    private String remarks;

    // Constructors, getters, and setters

    public PostDataModel(int DriverID, String date, int vehicleNo, String from, String to,
                         String coilID, String tonnage, String pageRefNo, String remarks) {
        this.driverID = DriverID;
        this.Date = date;
        this.VehicleID = vehicleNo;
        this.From_Place = from;
        this.To_Place = to;
        this.CoilID = coilID;
        this.Tonnage = tonnage;
        this.PagerefNo = pageRefNo;
        this.remarks = remarks;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getVehicleID() {
        return VehicleID;
    }

    public void setVehicleID(int vehicleID) {
        VehicleID = vehicleID;
    }

    public String getFrom_Place() {
        return From_Place;
    }

    public void setFrom_Place(String from_Place) {
        From_Place = from_Place;
    }

    public String getTo_Place() {
        return To_Place;
    }

    public void setTo_Place(String to_Place) {
        To_Place = to_Place;
    }

    public String getCoilID() {
        return CoilID;
    }

    public void setCoilID(String coilID) {
        CoilID = coilID;
    }

    public String getTonnage() {
        return Tonnage;
    }

    public void setTonnage(String tonnage) {
        Tonnage = tonnage;
    }

    public String getPagerefNo() {
        return PagerefNo;
    }

    public void setPagerefNo(String pagerefNo) {
        PagerefNo = pagerefNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
