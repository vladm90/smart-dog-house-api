package com.smartdoghouse.service;

import com.smartdoghouse.dao.TemperatureDto;
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
            raspiPin = RaspiPin.GPIO_04;
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
            raspiPin = RaspiPin.GPIO_04;
        }
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(raspiPin, "Light", PinState.LOW);
        pin.low();
        gpio.shutdown();
        gpio.unprovisionPin(pin);
        log.info("Light was closed for {}", relayId);
    }

    public List<TemperatureDto> findAll() throws InterruptedException {

        List<Temperature> list = new ArrayList<>();
        List<TemperatureDto> list2 = new ArrayList<>();
        temperatureDao.findAll().iterator().forEachRemaining(list::add);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        list.forEach(f -> {
            list2.add( TemperatureDto.builder()
                    .date(simpleDateFormat.format(f.getDate()))
                    .insideHappy(f.getInsideHappy())
                    .insideSnoopy(f.getInsideSnoopy())
                    .outside(f.getOutside())
                    .build());
        });
        Collections.reverse(list2);
        return list2;
    }

    public TemperatureDto getStats() throws InterruptedException {
        TemperatureDto temperature = new TemperatureDto();
        GpioUtil.enableNonPrivilegedAccess();
        GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalOutput myButton = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        final GpioPinDigitalOutput myButton2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
        System.out.println("1"+myButton.getState());
        System.out.println("2"+myButton2.getState());

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
        gpio.shutdown();
        gpio.unprovisionPin(myButton);
        gpio.unprovisionPin(myButton2);
        return temperature;
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
