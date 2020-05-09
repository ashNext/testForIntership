package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.specs.SearchCriteria;
import com.space.repository.specs.SearchOperation;
import com.space.repository.specs.ShipSpecification;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("rest/ships")
public class ShipController {

    @Autowired
    private ShipService shipService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> getShip(@PathVariable("id") Long id) {
        if (id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shipService.getById(id);

        if (ship == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Ship>> getAllShip(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "planet", required = false) String planet,
            @RequestParam(name = "shipType", required = false) ShipType shipType,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed", required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "3") int pageSize,
            @RequestParam(name = "order", defaultValue = "ID") ShipOrder order
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        ShipSpecification shipSpecification = getShipSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        List<Ship> ships = shipService.getAll(pageable, shipSpecification).getContent();

        if (ships.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(ships, HttpStatus.OK);
    }

    @RequestMapping(value = "count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Integer> getCount(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "planet", required = false) String planet,
            @RequestParam(name = "shipType", required = false) ShipType shipType,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed", required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating
    ) {
        ShipSpecification shipSpecification = getShipSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        return new ResponseEntity<>(shipService.getCount(shipSpecification), HttpStatus.OK);
    }

    private ShipSpecification getShipSpecification(String name,
                                                   String planet,
                                                   ShipType shipType,
                                                   Long after, Long before,
                                                   Boolean isUsed,
                                                   Double minSpeed, Double maxSpeed,
                                                   Integer minCrewSize, Integer maxCrewSize,
                                                   Double minRating, Double maxRating) {
        ShipSpecification shipSpecification = new ShipSpecification();

        if (name != null)
            shipSpecification.add(new SearchCriteria("name", name, SearchOperation.MATCH));
        if (planet != null)
            shipSpecification.add(new SearchCriteria("planet", planet, SearchOperation.MATCH));
        if (shipType != null)
            shipSpecification.add(new SearchCriteria("shipType", shipType, SearchOperation.EQUAL));
        if (after != null)
            shipSpecification.add(new SearchCriteria("prodDate", after, SearchOperation.DATE_GREATER_THAN_EQUAL));
        if (before != null)
            shipSpecification.add(new SearchCriteria("prodDate", before, SearchOperation.DATE_LESS_THAN_EQUAL));
        if (isUsed != null)
            shipSpecification.add(new SearchCriteria("isUsed", isUsed, SearchOperation.EQUAL));
        if (minSpeed != null)
            shipSpecification.add(new SearchCriteria("speed", minSpeed, SearchOperation.GREATER_THAN_EQUAL));
        if (maxSpeed != null)
            shipSpecification.add(new SearchCriteria("speed", maxSpeed, SearchOperation.LESS_THAN_EQUAL));
        if (minCrewSize != null)
            shipSpecification.add(new SearchCriteria("crewSize", minCrewSize, SearchOperation.GREATER_THAN_EQUAL));
        if (maxCrewSize != null)
            shipSpecification.add(new SearchCriteria("crewSize", maxCrewSize, SearchOperation.LESS_THAN_EQUAL));
        if (minRating != null)
            shipSpecification.add(new SearchCriteria("rating", minRating, SearchOperation.GREATER_THAN_EQUAL));
        if (maxRating != null)
            shipSpecification.add(new SearchCriteria("rating", maxRating, SearchOperation.LESS_THAN_EQUAL));

        return shipSpecification;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> saveShip(@RequestBody Ship ship) {
        HttpHeaders headers = new HttpHeaders();

        if (ship == null
                || ship.getName() == null || ship.getPlanet() == null || ship.getShipType() == null
                || ship.getProdDate() == null || ship.getSpeed() == null || ship.getCrewSize() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        int y = calendar.get(Calendar.YEAR);
        calendar.set(y, Calendar.JANUARY, 1);
        ship.setProdDate(calendar.getTime());


        if (ship.getName().isEmpty() || ship.getPlanet().isEmpty()
                || ship.getPlanet().length() > 50 || ship.getName().length() > 50
                || y < 2800 || y > 3019
                || ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99
                || ship.getCrewSize() < 1 || ship.getCrewSize() > 9999)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (ship.getUsed() == null)
            ship.setUsed(false);
        ship.calcRating();

        shipService.save(ship);
        return new ResponseEntity<>(ship, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> updateShip(
            @PathVariable("id") Long id,
            @RequestBody Ship requestShip
    ) {
        HttpHeaders headers = new HttpHeaders();
        if (id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shipService.getById(id);
        if (ship == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (requestShip.getName() != null)
            if (!requestShip.getName().isEmpty() && requestShip.getName().length() <= 50)
                ship.setName(requestShip.getName());
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (requestShip.getPlanet() != null)
            if (!requestShip.getPlanet().isEmpty() && requestShip.getPlanet().length() <= 50)
                ship.setPlanet(requestShip.getPlanet());
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (requestShip.getShipType() != null)
            ship.setShipType(requestShip.getShipType());

        if (requestShip.getProdDate() != null) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(requestShip.getProdDate());
            int y = calendar.get(Calendar.YEAR);
            calendar.set(y, Calendar.JANUARY, 1);
            requestShip.setProdDate(calendar.getTime());

            if (y >= 2800 && y <= 3019)
                ship.setProdDate(requestShip.getProdDate());
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (requestShip.getUsed() != null)
            ship.setUsed(requestShip.getUsed());

        if (requestShip.getSpeed() != null)
            if (requestShip.getSpeed() >= 0.01 && requestShip.getSpeed() <= 0.99)
                ship.setSpeed(requestShip.getSpeed());
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (requestShip.getCrewSize() != null)
            if (requestShip.getCrewSize() >= 1 && requestShip.getCrewSize() <= 9999)
                ship.setCrewSize(requestShip.getCrewSize());
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (requestShip.getSpeed() != null || requestShip.getUsed() != null || requestShip.getProdDate() != null)
            ship.calcRating();


        shipService.save(ship);
        return new ResponseEntity<>(ship, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") Long id) {
        if (id < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship ship = shipService.getById(id);
        if (ship == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        shipService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
