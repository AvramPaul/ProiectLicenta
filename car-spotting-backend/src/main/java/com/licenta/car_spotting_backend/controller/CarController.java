package com.licenta.car_spotting_backend.controller;

import com.licenta.car_spotting_backend.model.Car;
import com.licenta.car_spotting_backend.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cars")
@CrossOrigin(origins = "*")
public class CarController {

    @Autowired
    private CarRepository carRepository;
    private Long id;

    @GetMapping
    public List<Car> getAllCars(){
        return carRepository.findAll();
    }

    @GetMapping("/{id}")
    public Car getCarbyID(@PathVariable Long id ) { return carRepository.getById(id);}
}
