package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.model.Crypto;
import com.example.registrationlogindemo.model.Portafolio;
import com.example.registrationlogindemo.repository.PortafolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PortafolioService {

    @Autowired
    private PortafolioRepository portafolioRepository;

    @Autowired
    private CoinGeckoService coinGeckoService;

    @Transactional
    public void updatePortafolio(Long portafolioId, double newQuantity) {
        Portafolio portafolio = findById(portafolioId);
        portafolio.setQuantity(newQuantity);
        save(portafolio);
    }

    @Transactional
    public void deletePortafolio(Long portafolioId) {
        portafolioRepository.deleteById(portafolioId);
    }

    public Portafolio findById(Long portafolioId) {
        Optional<Portafolio> optionalPortafolio = portafolioRepository.findById(portafolioId);

        if (optionalPortafolio.isPresent()) {
            return optionalPortafolio.get();
        } else {
            throw new RuntimeException("Portafolio no encontrado!");
        }
    }

    public List<Portafolio> findByUser(User user) {
        return portafolioRepository.findByUser(user);

    }

    @Transactional
    public void addCryptoToPortafolio(Long portafolioId, String cryptoId, double quantity) {
        Portafolio portafolio = findById(portafolioId);

        // Obtener todos los cryptos almacenados en la base de datos
        List<Crypto> allCryptos = coinGeckoService.getCachedCryptosFromDatabase();

        Crypto crypto = coinGeckoService.findCryptoByIdInList(allCryptos, cryptoId);

        portafolio.setCrypto(crypto);
        portafolio.setQuantity(quantity);
        save(portafolio);
    }

    public void save(Portafolio portafolio) {
        portafolioRepository.save(portafolio);
    }


}