package com.shubham.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping
    public String welcome(){
        return "<!DOCTYPE html><html><head><title>My Static Page</title><style>body{margin:0;font-family:sans-serif;background:linear-gradient(to right,#4facfe,#00f2fe);display:flex;flex-direction:column;justify-content:center;align-items:center;height:100vh;color:white;text-shadow:1px 1px 2px black}h1{font-size:3em;margin:0}p{font-size:1.2em;margin-top:10px}</style></head><body><h1>Welcome to My Page</h1><p>Beautiful, realistic, and responsive in just one line!</p></body></html>";
    }

    @GetMapping("/csrf")
    public CsrfToken getToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
