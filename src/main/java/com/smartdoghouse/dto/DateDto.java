package com.smartdoghouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateDto implements Serializable {
    private Date startDate;
    private Date endDate;
}
