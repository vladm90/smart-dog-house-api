package com.devglan.service.impl;

import com.devglan.dao.TemperatureDao;

import com.devglan.model.Temperature;
import com.devglan.model.TemperatureDto;
import com.devglan.service.TemperatureService;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import com.pi4j.io.w1.W1Master;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service(value = "temperatureService")
public class TemperatureServiceImpl implements TemperatureService {
	
	@Autowired
	private TemperatureDao temperatureDao;

	public List<Temperature> findAll() throws InterruptedException {
		/*W1Master master = new W1Master();
		for(TemperatureSensor device : master.getDevices(TemperatureSensor.class)){
			System.out.println("Temperature: " + ((TemperatureSensor) device).getTemperature());
		}
		 System.out.println("<--Pi4J--> GPIO Control Example ... started.");
*/
		GpioUtil.enableNonPrivilegedAccess();
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an output pin and turn on
        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
		//raspberry ppin.setShutdownOptions(true, PinState.LOW);
		pin.high();
		Thread.sleep(1000);
		pin.low();
		/*for (W1Device device : w1Devices) {
			//this line is enought if you want to read the temperature
			System.out.println("Temperature: " + ((TemperatureSensor) device).getTemperature());
			//returns the temperature as double rounded to one decimal place after the point

			try {
				System.out.println("1-Wire ID: " + device.getId() +  " value: " + device.getValue());
				//returns the ID of the Sensor and the  full text of the virtual file
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/

		gpio.shutdown();
		gpio.unprovisionPin(pin);
		List<Temperature> list = new ArrayList<>();
		temperatureDao.findAll().iterator().forEachRemaining(list::add);
		return list;


		/*List<Temperature> historyChanges = temperatureDao.findAll();

		return historyChanges.stream().map(historyChange -> conversionService.convert(historyChange, HistoryResponseDto.class)).collect(Collectors.toList());*/
	}


	public void scheduleTaskSaveTemperatures() {
		long now = System.currentTimeMillis() / 1000;
		System.out.println("schedule tasks using cron jobs - " + now);
	}
/*
	@Override
	public void delete(int id) {
		userDao.deleteById(id);
	}

	@Override
	public User findOne(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public User findById(int id) {
		Optional<User> optionalUser = userDao.findById(id);
		return optionalUser.isPresent() ? optionalUser.get() : null;
	}

    @Override
    public TemperatureDto update(TemperatureDto userDto) {
        User user = findById(userDto.getId());
        if(user != null) {
            BeanUtils.copyProperties(userDto, user, "password", "username");
            userDao.save(user);
        }
        return userDto;
    }

    @Override
    public User save(TemperatureDto user) {
	    User newUser = new User();
	    newUser.setUsername(user.getUsername());
	    newUser.setFirstName(user.getFirstName());
	    newUser.setLastName(user.getLastName());
	    newUser.setPassword(user.getPassword());
		newUser.setAge(user.getAge());
		newUser.setSalary(user.getSalary());
        return userDao.save(newUser);
    }*/
}
