package com.space.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "ship")
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String planet;
    @Enumerated(EnumType.STRING)
    private ShipType shipType;
    private Date prodDate;
    private Boolean isUsed;
    private Double speed;
    private Integer crewSize;
    private Double rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

//    public Long getProdDate() {
//        return prodDate.getTime();
//    }
//
//    public void setProdDate(Long prodDate) {
//        this.prodDate = new Date(prodDate);
//    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    private void setRating(Double rating) {
        this.rating = rating;
    }

    public void calcRating()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getProdDate());
        int year = calendar.get(Calendar.YEAR);

        double value=((80*getSpeed()*(getUsed() ? 0.5 : 1))/(3019-year+1));

        setRating(new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

//    @Override
//    public String toString() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(getProdDate());
//        int prodYear = calendar.get(Calendar.YEAR);
//        return "Ship{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", planet='" + planet + '\'' +
//                ", shipType=" + shipType +
//                ", prodDate=" + getProdDate() +
//                ", isUsed=" + isUsed +
//                ", speed=" + speed +
//                ", crewSize=" + crewSize +
//                ", rating=" + rating +
//                '}';
//    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Ship ship = (Ship) o;
//        return id.equals(ship.id) &&
//                name.equals(ship.name) &&
//                planet.equals(ship.planet) &&
//                shipType == ship.shipType &&
//                prodDate.equals(ship.prodDate) &&
//                isUsed.equals(ship.isUsed) &&
//                speed.equals(ship.speed) &&
//                crewSize.equals(ship.crewSize) &&
//                rating.equals(ship.rating);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, planet, shipType, prodDate, isUsed, speed, crewSize, rating);
//    }
}
