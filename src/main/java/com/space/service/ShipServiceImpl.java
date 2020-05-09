package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipDetailsRepo;
import com.space.repository.specs.ShipSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipDetailsRepo shipDetailsRepo;

    @Override
    public Ship getById(Long id) {
        return shipDetailsRepo.findById(id).orElse(null);
    }

    @Override
    public int getCount(ShipSpecification specification) {
        return (int) shipDetailsRepo.count(specification);
    }

    @Override
    public Page<Ship> getAll(Pageable pageable, ShipSpecification specification) {
        return shipDetailsRepo.findAll(specification, pageable);
    }

    @Override
    public void save(Ship ship) {
        shipDetailsRepo.save(ship);
    }

    @Override
    public void delete(Long id) {
        shipDetailsRepo.deleteById(id);
    }
}
