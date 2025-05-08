package com.amalitech.hospitalinformationsystem.model;

public class Patient {
    private long    patientId;
    private String  firstName;
    private String  lastName;
    private String  address;
    private String  phone;

    public Patient() { }

    public Patient(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.address   = address;
        this.phone     = phone;
    }

    public Patient(long patientId, String firstName, String lastName, String address, String phone) {
        this(firstName, lastName, address, phone);
        this.patientId = patientId;
    }

    public long   getPatientId() { return patientId; }
    public void   setPatientId(long patientId) { this.patientId = patientId; }

    public String getFirstName()  { return firstName; }
    public void   setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName()   { return lastName; }
    public void   setLastName(String lastName)   { this.lastName = lastName; }

    public String getAddress()    { return address; }
    public void   setAddress(String address)     { this.address = address; }

    public String getPhone()      { return phone; }
    public void   setPhone(String phone)         { this.phone = phone; }
}