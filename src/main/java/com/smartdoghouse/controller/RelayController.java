package com.smartdoghouse.controller;

import com.smartdoghouse.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/relay")
public class RelayController {

    @Autowired
    private TemperatureService temperatureService;


    @GetMapping("{relayId}/on")
    public void setOn(@PathVariable Long relayId) {
         temperatureService.openRelay(relayId);
    }


    @GetMapping("{relayId}/off")
    public void setOff(@PathVariable Long relayId) {
        temperatureService.closeRelay(relayId);
    }





}

