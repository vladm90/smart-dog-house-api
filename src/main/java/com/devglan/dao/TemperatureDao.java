package com.devglan.dao;

import com.devglan.model.Temperature;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureDao extends CrudRepository<Temperature, Integer> {

  /*  User findByUsername(String username);*/
}
