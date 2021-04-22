package me.toy.jwpjpa.linestation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.jwpjpa.station.Station;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PreStationInfo {

    @Column
    private int distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pre_station_id")
    private Station preStation;

    private PreStationInfo(Station preStation, int distance) {
        this.preStation = preStation;
        this.distance = distance;
    }

    public static PreStationInfo of(Station preStation, int distance) {
        return new PreStationInfo(preStation, distance);
    }
}
