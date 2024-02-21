package com.example.registrationlogindemo.model;
// CryptoDto.java
public class CryptoDto {

    private String id;
    private String symbol;
    private String name;
    private double currentPrice;
    private String image;

    // Constructor, getters y setters

    public CryptoDto() {
        // Constructor vacío necesario para Thymeleaf
    }

    public CryptoDto(String id, String symbol, String name, double currentPrice, String image) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.image = image;
    }

    // Getters y Setters

    // Otros métodos según sea necesario
}
