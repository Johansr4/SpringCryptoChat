    package com.example.registrationlogindemo.model;

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.Getter;


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Entity
    public class Crypto {
        @Id
        private String id;
        private String symbol;
        private String name;
        private double current_price;
        private String image;
        @Getter
        private double quantity;

        @ManyToOne
        private Portafolio portafolio;




        @JsonProperty("id")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @JsonProperty("symbol")
        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("image")
        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "Crypto{" +
                    "id='" + id + '\'' +
                    ", symbol='" + symbol + '\'' +
                    ", name='" + name + '\'' +
                    ", current_price=" + current_price +
                    ", image='" + image + '\'' +
                    '}';
        }


    }

