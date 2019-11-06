package com.smartdoghouse.task;


import com.smartdoghouse.service.TemperatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@EnableScheduling
public class TemperaturesWorker{

    @Autowired
    TemperatureService temperatureService;

    @Scheduled(cron = "0 0/30 * * * *")
    public void saveTemperatures() {
        log.info("######Cron Start ######");
        temperatureService.scheduleTaskSaveTemperatures();
    }
}