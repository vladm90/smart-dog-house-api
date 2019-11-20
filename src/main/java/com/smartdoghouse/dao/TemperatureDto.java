package com.smartdoghouse.dao;

import lombok.*;
import java.io.Serializable;
import java.util.Date;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureDto implements Serializable {

    private Long id;
    private String date;
    private Double outside;
    private Double insideHappy;
    private Double insideSnoopy;
    private Boolean openHappy;
    private Boolean openSnoopy;

}