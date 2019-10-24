package com.devglan.service;

import com.devglan.model.Temperature;
import com.devglan.model.TemperatureDto;

import java.util.List;

public interface TemperatureService {
    List<Temperature> findAll();
    void scheduleTaskSaveTemperatures();
   /* User save(TemperatureDto user);

    void delete(int id);

    User findOne(String username);

    User findById(int id);

    TemperatureDto update(TemperatureDto userDto);*/
}
