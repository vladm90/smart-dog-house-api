package com.devglan.model;

import lombok.*;
import java.io.Serializable;
import java.util.Date;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureDto implements Serializable {

    private Long id;
    private Date date;
    private Double outside;
    private Double insideHappy;
    private Double insideSnoopy;
    private Boolean openHappy;
    private Boolean openSnoopy;

}
