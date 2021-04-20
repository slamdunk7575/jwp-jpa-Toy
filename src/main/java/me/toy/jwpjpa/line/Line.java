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
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "line", indexes = {
        @Index(name = "idx_line", columnList = "name", unique = true)
})
public class Line extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineStation> lineStations = new ArrayList<>();

    @Builder
    public Line(String name, String color, List<Station> stations) {
        this.name = name;
        this.color = color;
        this.lineStations = Optional.ofNullable(stations).orElse(Collections.emptyList()).stream()
        .map(station -> LineStation.of(this, station))
        .collect(Collectors.toList());
    }

    public Line updateName(String name) {
        this.name = name;
        return this;
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
