package com.example.registrationlogindemo.model;


import com.example.registrationlogindemo.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Portafolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portafolioId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Crypto crypto;

    @Column(nullable = false)
    private double quantity;




}
