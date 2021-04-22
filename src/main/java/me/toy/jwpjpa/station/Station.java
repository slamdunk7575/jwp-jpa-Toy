package me.toy.jwpjpa.station;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.jwpjpa.base.BaseEntity;
import me.toy.jwpjpa.linestation.LineStation;

import javax.persistence.*;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "station", indexes =  {
        @Index(name = "idx_station", columnList = "name", unique = true)
})
public class Station extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "station", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineStation> lineStations = new ArrayList<>();

    public Station(String name) {
        this.name = name;
    }

    public static Station of(String name) {
        return new Station(name);
    }

    public Station updateName(String name) {
        this.name = name;
        return this;
    }

    public void addLineStation(LineStation lineStation) {
        this.lineStations.add(lineStation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
