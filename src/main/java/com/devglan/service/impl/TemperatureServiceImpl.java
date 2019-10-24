package com.devglan.service.impl;

import com.devglan.dao.TemperatureDao;

import com.devglan.model.Temperature;
import com.devglan.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "temperatureService")
public class TemperatureServiceImpl implements TemperatureService {
	
	@Autowired
	private TemperatureDao temperatureDao;

	public List<Temperature> findAll() {
		List<Temperature> list = new ArrayList<>();
		temperatureDao.findAll().iterator().forEachRemaining(list::add);
		return list;
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
