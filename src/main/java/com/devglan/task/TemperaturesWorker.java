package com.devglan.task;

import java.io.IOException;

import com.devglan.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
@EnableScheduling
public class TemperaturesWorker{

    @Autowired
    TemperatureService temperatureService;

    @Scheduled(cron = "0 */10 * * * *")
    public void saveTemperatures() {
        System.out.println("######Cron Start ######");
        temperatureService.scheduleTaskSaveTemperatures();
    }
}