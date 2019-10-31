package com.devglan.controller;


import com.devglan.model.ApiResponse;
import com.devglan.model.Temperature;
import com.devglan.service.TemperatureService;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.gpio.*;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;




@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/relay")
public class RelayController {

    @Autowired
    private TemperatureService temperatureService;



    @GetMapping("/on")
    public void setOn() throws InterruptedException {
         temperatureService.openRelay();

    }


    @GetMapping("/off")
    public void setOff() throws InterruptedException {
        temperatureService.closeRelay();

    }





}

