package com.smartdoghouse.service;

import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;

import java.io.File;

import static java.util.Objects.isNull;
import static org.bytedeco.opencv.global.opencv_core.cvFlip;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;
import com.smartdoghouse.dto.DateDto;
import com.smartdoghouse.dto.TemperatureDto;
import com.smartdoghouse.repository.TemperatureDao;

import com.smartdoghouse.model.Temperature;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import com.pi4j.io.w1.W1Master;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
            raspiPin = RaspiPin.GPIO_21;
        } else if (relayId == 2) {
            raspiPin = RaspiPin.GPIO_22;
        }

        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(raspiPin, "Light", PinState.LOW);
        pin.low();
        gpio.shutdown();
        gpio.unprovisionPin(pin);
        log.info("Light was started for {}", relayId);
    }

    public void closeRelay(Long relayId) {
        GpioUtil.enableNonPrivilegedAccess();
        GpioController gpio = GpioFactory.getInstance();
        Pin raspiPin = null;
        if(relayId == 1) {
            raspiPin = RaspiPin.GPIO_21;
        } else if (relayId == 2) {
            raspiPin = RaspiPin.GPIO_22;
        }
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(raspiPin, "Light", PinState.HIGH);
        pin.high();
        gpio.shutdown();
        gpio.unprovisionPin(pin);
        log.info("Light was closed for {}", relayId);
    }

    public List<TemperatureDto> findAll() throws InterruptedException {

        List<Temperature> listTemp = new ArrayList<>();
        List<TemperatureDto> listTempDto = new ArrayList<>();
        temperatureDao.findTop50ByOrderByDateDesc().iterator().forEachRemaining(listTemp::add);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        listTemp.forEach(f -> {
            listTempDto.add( TemperatureDto.builder()
                    .date(simpleDateFormat.format(f.getDate()))
                    .insideHappy(f.getInsideHappy())
                    .insideSnoopy(f.getInsideSnoopy())
                    .outside(f.getOutside())
                    .openHappy(f.getOpenHappy()? "OPEN" : "CLOSED")
                    .openSnoopy(f.getOpenSnoopy()? "OPEN" : "CLOSED")
                    .build());
        });
        //Collections.reverse(listTempDto);
        return listTempDto;
    }

    public List<TemperatureDto> getAllByFilters(DateDto dateDto) throws InterruptedException {
        List<Temperature> listTemp = new ArrayList<>();
        List<TemperatureDto> listTempDto = new ArrayList<>();
        temperatureDao.findAllByFilters(dateDto.getStartDate(), dateDto.getEndDate()).iterator().forEachRemaining(listTemp::add);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        listTemp.forEach(f -> {
            listTempDto.add( TemperatureDto.builder()
                    .date(simpleDateFormat.format(f.getDate()))
                    .insideHappy(f.getInsideHappy())
                    .insideSnoopy(f.getInsideSnoopy())
                    .outside(f.getOutside())
                    .openHappy(f.getOpenHappy()? "OPEN" : "CLOSED")
                    .openSnoopy(f.getOpenSnoopy()? "OPEN" : "CLOSED")
                    .build());
        });
        Collections.reverse(listTempDto);
        return listTempDto;
    }

    public TemperatureDto getStats(){
        TemperatureDto temperature = new TemperatureDto();
        GpioUtil.enableNonPrivilegedAccess();
        GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalOutput lightHappy = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21);
        final GpioPinDigitalOutput lightSnoopy = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22);

        temperature.setOpenHappy(lightHappy.getState().isLow() ? "OPEN" : "CLOSED");
        temperature.setOpenSnoopy(lightSnoopy.getState().isLow() ? "OPEN" : "CLOSED");

        W1Master master = new W1Master();
        for (TemperatureSensor device : master.getDevices(TemperatureSensor.class)) {
            if (device.getName().equals("28-0300a279cbc1")) { //3m
                temperature.setInsideSnoopy(device.getTemperature());
            }
            if (device.getName().equals("28-0300a279e21e")) { //2m
                temperature.setInsideHappy(device.getTemperature());
            }
            if (device.getName().equals("28-0416a15904ff")) { //1m
                temperature.setOutside(device.getTemperature());
            }

        }
        if(isNull(temperature.getInsideSnoopy())){
            temperature.setInsideSnoopy(0D);
        }
        gpio.shutdown();
        gpio.unprovisionPin(lightHappy);
        gpio.unprovisionPin(lightSnoopy);
        return temperature;
    }

    public void getImages() {
        //get image happy
        FrameGrabber grabber = new OpenCVFrameGrabber(0); // 1 for next camera
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        IplImage img;
        try {
            grabber.start();
            Frame frame = grabber.grab();
            img = converter.convert(frame);

            //the grabbed frame will be flipped, re-flip to make it right
            cvFlip(img, img, -1);// l-r = 90_degrees_steps_anti_clockwise

            cvSaveImage("/home/pi/work/smart-dog-house-frontend/src/resources/imageHappy.jpg", img);
            grabber.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void scheduleTaskSaveTemperatures() {
        TemperatureDto temperature = getStats();
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        if(hour < 9 || hour > 16){
            if (temperature.getInsideHappy() > 7 && temperature.getInsideHappy() < 12){
                openRelay(1L);
                temperature.setOpenHappy("OPEN");
            } else {
                closeRelay(1L);
                temperature.setOpenHappy("CLOSED");
            }

            if (temperature.getInsideSnoopy() > 7 && temperature.getInsideSnoopy() < 12){
                openRelay(2L);
                temperature.setOpenSnoopy("OPEN");
            } else {
                closeRelay(2L);
                temperature.setOpenSnoopy("CLOSED");
            }
        } else {
            closeRelay(1L);
            temperature.setOpenHappy("CLOSED");
            closeRelay(2L);
            temperature.setOpenSnoopy("CLOSED");
        }

        temperatureDao.save(Temperature.builder()
                .openHappy(temperature.getOpenHappy().equals("OPEN"))
                .openSnoopy(temperature.getOpenSnoopy().equals("OPEN"))
                .insideHappy(temperature.getInsideHappy())
                .insideSnoopy(temperature.getInsideSnoopy())
                .outside(temperature.getOutside())
                .build());
        log.info("######Cron Finished ######");
    }

}
