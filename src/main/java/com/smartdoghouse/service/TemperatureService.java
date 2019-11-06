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

    public void openRelay() {
        System.out.println("<--Pi4J--> GPIO Control Example ... started.ON");
        GpioUtil.enableNonPrivilegedAccess();
        // create gpio controller
        GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an output pin and turn on
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);
        pin.high();
        gpio.shutdown();
        gpio.unprovisionPin(pin);
        System.out.println("<--Pi4J--> GPIO Control Example ... started.ON END");
    }

    public void closeRelay() {
        System.out.println("<--Pi4J--> GPIO Control Example ... started.OFF");
        GpioUtil.enableNonPrivilegedAccess();
        // create gpio controller
        GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an output pin and turn on
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED9", PinState.LOW);
        pin.low();
        gpio.shutdown();
        gpio.unprovisionPin(pin);
        System.out.println("<--Pi4J--> GPIO OFF END");
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
