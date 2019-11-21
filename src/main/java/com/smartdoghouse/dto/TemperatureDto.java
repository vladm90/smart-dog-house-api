package com.smartdoghouse.dto;

import lombok.*;
import java.io.Serializable;


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
    private String openHappy;
    private String openSnoopy;

}
