package com.smartdoghouse.controller;


import com.smartdoghouse.dto.DateDto;
import com.smartdoghouse.dto.TemperatureDto;
import com.smartdoghouse.model.ApiResponse;
import com.smartdoghouse.model.Temperature;
import com.smartdoghouse.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @PostMapping(value = "/temperatures", consumes = APPLICATION_JSON_VALUE)
    public ApiResponse<List<Temperature>> getAllByFilters(@RequestBody @Valid DateDto dateDto) throws InterruptedException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Temperatures list fetched successfully.", temperatureService.getAllByFilters(dateDto));

    }

    @GetMapping("/stats")
    public ApiResponse<TemperatureDto> getStats() throws InterruptedException {
        return new ApiResponse<>(HttpStatus.OK.value(), "Temperatures list fetched successfully.", temperatureService.getStats());

    }



}
