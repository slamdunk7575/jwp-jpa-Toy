package me.toy.jwpjpa.line;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.jwpjpa.base.BaseEntity;
import me.toy.jwpjpa.linestation.LineStation;
import me.toy.jwpjpa.station.Station;

import javax.persistence.*;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "line", indexes = {
        @Index(name = "idx_line", columnList = "name", unique = true)
})
public class Line extends BaseEntity {

    private static final String ADD_DUPLICATE_STATION_ERROR = "%s 라인에 %s 역이 이미 포함된 역입니다.";

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineStation> lineStations = new ArrayList<>();

    @Builder
    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line updateName(String name) {
        this.name = name;
        return this;
    }

    public void addLineStation(LineStation lineStation) {
        Station station = lineStation.getStation();
        if (isExistStation(station)) {
            throw new IllegalArgumentException(String.format(ADD_DUPLICATE_STATION_ERROR, this.name, station));
        }
        this.lineStations.add(lineStation);
    }

    private boolean isExistStation(Station station) {
        return this.lineStations.stream()
                .anyMatch(lineStation -> lineStation.getStation().equals(station));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
