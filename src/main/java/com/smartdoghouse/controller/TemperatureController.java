package com.smartdoghouse.controller;


import com.smartdoghouse.dao.TemperatureDto;
import com.smartdoghouse.model.ApiResponse;
import com.smartdoghouse.model.Temperature;
import com.smartdoghouse.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TemperatureController {

    @Autowired
    private TemperatureService temperatureService;

    @GetMapping("/temperatures")
    public ApiResponse<List<Temperature>> getAllTemperatures() throws InterruptedException {
         return new ApiResponse<>(HttpStatus.OK.value(), "Temperatures list fetched successfully.", temperatureService.findAll());

    }

    @GetMapping("/stats")
    public ApiResponse<TemperatureDto> getStats() throws InterruptedException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Temperatures list fetched successfully.", temperatureService.getStats());

    }



}
