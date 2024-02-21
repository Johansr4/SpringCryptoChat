package com.example.registrationlogindemo;

import com.example.registrationlogindemo.model.Crypto;
import com.example.registrationlogindemo.service.CoinGeckoService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ApiCaller {

    public static void main(String[] args) {
        CoinGeckoService coinGeckoService = new CoinGeckoService();

        // Hacer la llamada a la API
        List<Crypto> cryptoList = coinGeckoService.requestCoinGeckoApi();

        if (!cryptoList.isEmpty()) {
            // Imprimir la lista de Crypto
            System.out.println("Datos de la API:");
            for (Crypto crypto : cryptoList) {
                System.out.println(crypto);
            }
        } else {
            // Imprimir un mensaje si la lista está vacía
            System.out.println("No se encontraron datos de la API.");
        }
    }
}
