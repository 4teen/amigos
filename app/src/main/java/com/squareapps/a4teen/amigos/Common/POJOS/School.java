package com.squareapps.a4teen.amigos.Common.POJOS;


public class School {
    private String Campus_Address,
            Campus_City,
            Campus_Name,
            Campus_State,
            Campus_Zip,
            Institution_Address,
            Institution_City,
            Institution_Name,
            Institution_Phone,
            Institution_State,
            Institution_Web_Address,
            Institution_Zip;


    public School() {}


    public School(String campus_Address, String campus_City, String campus_Name, String campus_State, String campus_Zip, String institution_Address, String institution_City, String institution_Name, String institution_Phone, String institution_State, String institution_Web_Address, String institution_Zip) {
        Campus_Address = campus_Address;
        Campus_City = campus_City;
        Campus_Name = campus_Name;
        Campus_State = campus_State;
        Campus_Zip = campus_Zip;
        Institution_Address = institution_Address;
        Institution_City = institution_City;
        Institution_Name = institution_Name;
        Institution_Phone = institution_Phone;
        Institution_State = institution_State;
        Institution_Web_Address = institution_Web_Address;
        Institution_Zip = institution_Zip;
    }

    public String getCampus_Address() {
        return Campus_Address;
    }

    public void setCampus_Address(String campus_Address) {
        Campus_Address = campus_Address;
    }

    public String getCampus_City() {
        return Campus_City;
    }

    public void setCampus_City(String campus_City) {
        Campus_City = campus_City;
    }

    public String getCampus_Name() {
        return Campus_Name;
    }

    public void setCampus_Name(String campus_Name) {
        Campus_Name = campus_Name;
    }

    public String getCampus_State() {
        return Campus_State;
    }

    public void setCampus_State(String campus_State) {
        Campus_State = campus_State;
    }

    public String getCampus_Zip() {
        return Campus_Zip;
    }

    public void setCampus_Zip(String campus_Zip) {
        Campus_Zip = campus_Zip;
    }

    public String getInstitution_Address() {
        return Institution_Address;
    }

    public void setInstitution_Address(String institution_Address) {
        Institution_Address = institution_Address;
    }

    public String getInstitution_City() {
        return Institution_City;
    }

    public void setInstitution_City(String institution_City) {
        Institution_City = institution_City;
    }

    public String getInstitution_Name() {
        return Institution_Name;
    }

    public void setInstitution_Name(String institution_Name) {
        Institution_Name = institution_Name;
    }

    public String getInstitution_Phone() {
        return Institution_Phone;
    }

    public void setInstitution_Phone(String institution_Phone) {
        Institution_Phone = institution_Phone;
    }

    public String getInstitution_State() {
        return Institution_State;
    }

    public void setInstitution_State(String institution_State) {
        Institution_State = institution_State;
    }

    public String getInstitution_Web_Address() {
        return Institution_Web_Address;
    }

    public void setInstitution_Web_Address(String institution_Web_Address) {
        Institution_Web_Address = institution_Web_Address;
    }

    public String getInstitution_Zip() {
        return Institution_Zip;
    }

    public void setInstitution_Zip(String institution_Zip) {
        Institution_Zip = institution_Zip;
    }

}
