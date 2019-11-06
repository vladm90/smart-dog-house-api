package com.smartdoghouse.controller;

import com.smartdoghouse.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/relay")
public class RelayController {

    @Autowired
    private TemperatureService temperatureService;


    @GetMapping("/on")
    public void setOn() {
         temperatureService.openRelay();
    }


    @GetMapping("/off")
    public void setOff() {
        temperatureService.closeRelay();
    }





}

