package com.inteligencia.ciclo_comercial.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }

}
