package com.project.app.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.*;

import java.io.Serializable;
import java.util.Objects;

public class PhoneBook implements Serializable {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("phoneNumber")
    private String phoneNumber;

    @Expose
    @SerializedName("address")
    private String address;
    public PhoneBook() {

    }
    public PhoneBook(String name, String phoneNumber, String address)  {
        setName(name);
        setPhoneNumber(phoneNumber);
        setAddress(address);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address)  {
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

      PhoneBook phoneBook = (PhoneBook) o;

        return Objects.equals(name, phoneBook.name) &&
                Objects.equals(phoneNumber, phoneBook.phoneNumber) &&
                Objects.equals(address, phoneBook.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber, address);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
