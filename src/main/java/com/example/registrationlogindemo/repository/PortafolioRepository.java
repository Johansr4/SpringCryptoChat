package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.model.Crypto;
import com.example.registrationlogindemo.model.Portafolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortafolioRepository extends JpaRepository<Portafolio, Long> {

    public List<Portafolio> findByUser(User user);


}
