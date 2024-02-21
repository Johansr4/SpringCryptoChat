package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.model.Crypto;
import com.example.registrationlogindemo.model.Portafolio;
import com.example.registrationlogindemo.service.CoinGeckoService;
import com.example.registrationlogindemo.service.PortafolioService;
import com.example.registrationlogindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
public class PortafolioController {

    @Autowired
    private CoinGeckoService coinGeckoService;

    @Autowired
    private PortafolioService portafolioService;

    @Autowired
    private UserService userService;

    @GetMapping("/portafolio")
    public String showAddCryptoForm(Model model, Authentication authentication) {
        //
        List<Crypto> cryptos = coinGeckoService.getCachedCryptosFromDatabase();
       /* model.addAttribute("cryptos", cryptos);
        model.addAttribute("portafolio", new Portafolio());
        model.addAttribute("quantity", 0);*/
        User user = userService.findByEmail(authentication.getName());
        model.addAttribute("cryptos_usuarios", portafolioService.findByUser(user));
        return "portafolio";
    }



    @GetMapping("/portafolio/add")
    public String showAddCryptoForm(Model model) {
        Portafolio portafolio = new Portafolio();
        model.addAttribute("portafolio", portafolio);

        List<Crypto> allCryptos = coinGeckoService.getCachedCryptosFromDatabase();
        model.addAttribute("cryptos", allCryptos);

        return "formulario";
    }

    @PostMapping("/portafolio/delete/{portafolioId}")
    public String deletePortafolio(@PathVariable Long portafolioId) {
        portafolioService.deletePortafolio(portafolioId);
        return "redirect:/portafolio";
    }



    @GetMapping("/portafolio/update/{portafolioId}")
    public String showUpdateForm(@PathVariable Long portafolioId, Model model) {
        Portafolio portafolio = portafolioService.findById(portafolioId);
        model.addAttribute("portafolio", portafolio);
        model.addAttribute("cryptos", coinGeckoService.getCachedCryptosFromDatabase());
        return "updateForm";
    }

    @PostMapping("/portafolio/update/{portafolioId}")
    public String updatePortafolio(
            @PathVariable Long portafolioId,
            @RequestParam("newQuantity") double newQuantity) {
        portafolioService.updatePortafolio(portafolioId, newQuantity);
        return "redirect:/portafolio";
    }

    @PostMapping("/portafolio/add/submit")
    public String addCryptoToPortafolio(
            @ModelAttribute Portafolio portafolio,
            @RequestParam("cryptoId") String cryptoId,
            @RequestParam("quantity") double quantity,
            Authentication authentication
    ) {
        User user = userService.findByEmail(authentication.getName());

        // Obtiene la Crypto desde la base de datos usando el ID proporcionado
        Crypto selectedCrypto = coinGeckoService.findById(cryptoId);

        // Crea una nueva instancia de Portafolio y asocia el usuario y la crypto
        Portafolio auxPortafolio = new Portafolio();
        auxPortafolio.setUser(user);
        auxPortafolio.setCrypto(selectedCrypto);

        // Asigna la cantidad directamente en el objeto Portafolio
        auxPortafolio.setQuantity(quantity);

        // Guarda el portafolio
        portafolioService.save(auxPortafolio);

        return "redirect:/portafolio";
    }


}