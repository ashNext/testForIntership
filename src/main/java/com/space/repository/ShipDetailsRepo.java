package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 Repo interface for {@link Ship} class.
 */

public interface ShipDetailsRepo extends PagingAndSortingRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {

}
