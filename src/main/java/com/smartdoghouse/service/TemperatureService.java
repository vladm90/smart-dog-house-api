package com.smartdoghouse.service;

import com.smartdoghouse.repository.TemperatureDao;

import com.smartdoghouse.model.Temperature;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import com.pi4j.io.w1.W1Master;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Transactional
@Service(value = "temperatureService")
public class TemperatureService {

    @Autowired
    private TemperatureDao temperatureDao;

    public void openRelay(Long relayId) {
        GpioUtil.enableNonPrivilegedAccess();
        GpioController gpio = GpioFactory.getInstance();
        Pin raspiPin = null;
        if(relayId == 1) {
            raspiPin = RaspiPin.GPIO_01;
        } else if (relayId == 2) {
            raspiPin = RaspiPin.GPIO_01;
        }

        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(raspiPin, "Light", PinState.HIGH);
        pin.high();
        gpio.shutdown();
        gpio.unprovisionPin(pin);
        log.info("Light was started for {}", relayId);
    }

    public void closeRelay(Long relayId) {
        GpioUtil.enableNonPrivilegedAccess();
        GpioController gpio = GpioFactory.getInstance();
        Pin raspiPin = null;
        if(relayId == 1) {
            raspiPin = RaspiPin.GPIO_01;
        } else if (relayId == 2) {
            raspiPin = RaspiPin.GPIO_01;
        }
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(raspiPin, "Light", PinState.LOW);
        pin.low();
        gpio.shutdown();
        gpio.unprovisionPin(pin);
        log.info("Light was started for {}", relayId);
    }

    public List<Temperature> findAll() throws InterruptedException {

        List<Temperature> list = new ArrayList<>();
        temperatureDao.findAll().iterator().forEachRemaining(list::add);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        //list.forEach(f -> f.setDate(simpleDateFormat.format(f.getDate())));
        Collections.reverse(list);
        return list;
    }


    public void scheduleTaskSaveTemperatures() {
        Temperature temperature = new Temperature();
        W1Master master = new W1Master();
        for (TemperatureSensor device : master.getDevices(TemperatureSensor.class)) {
            if (device.getName().equals("28-0416a17be5ff")) { //3m
                temperature.setInsideSnoopy(device.getTemperature());
            }
            if (device.getName().equals("28-0516a1a5b9ff")) { //2m
                temperature.setInsideHappy(device.getTemperature());
            }
            if (device.getName().equals("28-0416a15904ff")) { //1m
                temperature.setOutside(device.getTemperature());
            }

        }
        temperature.setOpenHappy(false);
        temperature.setOpenSnoopy(false);
        temperatureDao.save(temperature);
        log.info("######Cron Finished ######");
    }

}
