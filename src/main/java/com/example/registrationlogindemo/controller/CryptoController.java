package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.model.Crypto;
import com.example.registrationlogindemo.service.CoinGeckoService;
import com.example.registrationlogindemo.service.CryptoService;
import com.example.registrationlogindemo.service.PortafolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class CryptoController {

    @Autowired
    private CoinGeckoService coinGeckoService;
    private PortafolioService portafolioService;

    @Autowired
    private CryptoService cryptoService;



    @GetMapping("/cryptos/peticionGecko")
    public String getCoinGeckoData(Model model) {
        List<Crypto> cryptoList = (List<Crypto>) coinGeckoService.getCachedCryptosFromDatabase();
        model.addAttribute("cryptoList", cryptoList);
        System.out.println("Crypto List Size: " + cryptoList.size());
        return "cryptos";
    }

    @GetMapping("/cryptos/peticionBBDD")
    public String showCryptosCoinGeckoData(Model model) {
        List<Crypto> cryptoList = cryptoService.getAllCryptos();
        model.addAttribute("cryptoList", cryptoList);
        System.out.println("Crypto List Size: " + cryptoList.size());
        return "cryptos";
    }

    @PostMapping("/add/submit")
    public String addCryptoToPortafolio(@RequestParam("cryptoId") String cryptoId,
                                        @RequestParam("quantity") double quantity,
                                        @RequestParam("portafolioId") Long portafolioId) {
        portafolioService.addCryptoToPortafolio(portafolioId, cryptoId, quantity);
        return "redirect:/portafolio";  // Redirigir a la página del portafolio después de agregar la moneda
    }




}
