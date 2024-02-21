package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.model.Crypto;
import com.example.registrationlogindemo.model.Portafolio;
import com.example.registrationlogindemo.repository.CryptoRepository;
import com.example.registrationlogindemo.repository.PortafolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CoinGeckoService {

    private static final Logger logger = LoggerFactory.getLogger(CoinGeckoService.class);

    @Value("${coingecko.api.url}")
    private String apiUrl;

    @Autowired
    private CryptoRepository cryptoRepository;

    @Autowired
    private PortafolioRepository portafolioRepository;

    private List<Crypto> cachedCryptos;

    @Transactional
    public List<Crypto> getCachedCryptosFromDatabase() {
        if (cachedCryptos == null) {
            cachedCryptos = cryptoRepository.findAll();
            logger.info("Retrieved cryptos from the database. Number of cryptos: {}", cachedCryptos.size());
        }
        return cachedCryptos;
    }

    @Transactional
    public void saveCryptoToDatabase(Crypto crypto) {
        if (crypto.getCurrent_price() > 0.0) {
            cryptoRepository.save(crypto);
            logger.info("Saved crypto to the database: {}", crypto);
        } else {
            logger.warn("Attempted to save crypto with invalid current price: {}", crypto);
        }
    }

    @Transactional
    public void updateCryptoInDatabase(Crypto existingCrypto, Crypto newCrypto) {
        if (newCrypto.getCurrent_price() > 0.0) {
            existingCrypto.setName(newCrypto.getName());
            existingCrypto.setSymbol(newCrypto.getSymbol());
            existingCrypto.setCurrent_price(newCrypto.getCurrent_price());
            existingCrypto.setImage(newCrypto.getImage());
            cryptoRepository.save(existingCrypto);
            logger.info("Updated crypto in the database: {}", existingCrypto);
        } else {
            logger.warn("Attempted to update crypto with invalid current price: {}", newCrypto);
        }
    }

    @Transactional
    public List<Crypto> requestCoinGeckoApiAndSave(Long portafolioId, String cryptoId, double quantity) {
        List<Crypto> result = new ArrayList<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Crypto[]> responseEntity = restTemplate.getForEntity(apiUrl, Crypto[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                result = Arrays.asList(responseEntity.getBody());
                logger.info("Successfully retrieved data from CoinGecko API. Number of cryptos: {}", result.size());

                Portafolio portafolio = portafolioRepository.findById(portafolioId).orElse(null);

                if (portafolio != null) {
                    for (Crypto crypto : result) {
                        if (crypto.getId().equals(cryptoId)) {
                            portafolio.setCrypto(crypto);
                            saveCryptoToDatabase(crypto);
                        }
                    }
                    portafolioRepository.save(portafolio);
                } else {
                    logger.error("Portafolio with ID {} not found.", portafolioId);
                }
            } else {
                logger.error("Error response from CoinGecko API. Status code: {}", responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            logger.error("Error while making a request to CoinGecko API", e);
        }

        return result;
    }

    @Transactional
    public List<Crypto> synchronizeCryptosWithApi() {
        List<Crypto> result = new ArrayList<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Crypto[]> responseEntity = restTemplate.getForEntity(apiUrl, Crypto[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                result = Arrays.asList(responseEntity.getBody());
                logger.info("Successfully retrieved data from CoinGecko API. Number of cryptos: {}", result.size());

                List<Crypto> existingCryptos = getCachedCryptosFromDatabase();

                for (Crypto newCrypto : result) {
                    Crypto existingCrypto = findCryptoById(existingCryptos, newCrypto.getId());

                    if (existingCrypto != null) {
                        updateCryptoInDatabase(existingCrypto, newCrypto);
                    } else {
                        saveCryptoToDatabase(newCrypto);
                    }
                }

                logger.info("CoinGecko API Response: {}", Arrays.toString(responseEntity.getBody()));
            } else {
                logger.error("Error response from CoinGecko API. Status code: {}", responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            logger.error("Error while making a request to CoinGecko API", e);
        }

        return result;
    }

    public Crypto findById(String cryptoId) {
        if (cachedCryptos == null) {
            cachedCryptos = getCachedCryptosFromDatabase();
        }

        return cachedCryptos.stream()
                .filter(crypto -> crypto.getId().equals(cryptoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Crypto ID " + cryptoId + " not found"));
    }

    private Crypto findCryptoById(List<Crypto> cryptos, String cryptoId) {
        return cryptos.stream()
                .filter(crypto -> crypto.getId().equals(cryptoId))
                .findFirst()
                .orElse(null);
    }


    public Crypto findCryptoByIdInList(List<Crypto> cryptoList, String cryptoId) {
        return cryptoList.stream()
                .filter(crypto -> crypto.getId().equals(cryptoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Crypto ID " + cryptoId + " not found"));
    }

}
