package com.smartdoghouse.repository;

import com.smartdoghouse.dto.DateDto;
import com.smartdoghouse.model.Temperature;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TemperatureDao extends CrudRepository<Temperature, Integer> {

    /*  User findByUsername(String username);*/

    @Query("Select t from Temperature t where t.date > ?1 and t.date <= ?2")
    List<Temperature> findAllByFilters(Date startDate, Date endDate);

    //@Query("Select t from Temperature t where rownum <= 50 order by t.date desc")
    List<Temperature> findTop50ByOrderByDateDesc();
}
