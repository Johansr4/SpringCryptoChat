package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.model.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoRepository extends JpaRepository<Crypto, String> {


}
