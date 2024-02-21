package com.example.registrationlogindemo.service;
import com.example.registrationlogindemo.model.Crypto;
import com.example.registrationlogindemo.repository.CryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoService {

    @Autowired
    private CryptoRepository cryptoRepository;

    public List<Crypto> getAllCryptos() {
        return cryptoRepository.findAll();
    }
}
