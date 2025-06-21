package com.licenta.car_spotting_backend.repository;

import com.licenta.car_spotting_backend.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository  extends JpaRepository<Car, Long> {
}
