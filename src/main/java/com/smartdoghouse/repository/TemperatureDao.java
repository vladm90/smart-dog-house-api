package com.smartdoghouse.repository;

import com.smartdoghouse.model.Temperature;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureDao extends CrudRepository<Temperature, Integer> {

  /*  User findByUsername(String username);*/
}
