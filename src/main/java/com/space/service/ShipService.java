package com.space.service;

import com.space.model.Ship;
import com.space.repository.specs.ShipSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipService {
    Ship getById(Long id);
    Page<Ship> getAll(Pageable pageable, ShipSpecification specification);
    int getCount(ShipSpecification specification);
    void save(Ship ship);
    void delete(Long id);
}
