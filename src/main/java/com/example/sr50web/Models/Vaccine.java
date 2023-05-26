package com.example.sr50web.Models;

public class Vaccine {
        private int id;
        private String name;
        private int available;
        private Manufacturer manufacturer;

    private int manufacturerId;

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Vaccine(int id, String name, int available, Manufacturer manufacturer) {
        this.id = id;
        this.name = name;
        this.available = available;
        this.manufacturer = manufacturer;
    }

    public Vaccine() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Vaccine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", available=" + available +
                ", manufacturer=" + manufacturer +
                '}';
    }
};
