package me.toy.jwpjpa.linestation;

import lombok.*;
import me.toy.jwpjpa.base.BaseEntity;
import me.toy.jwpjpa.line.Line;
import me.toy.jwpjpa.station.Station;

import javax.persistence.*;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "line_station")
@Entity
public class LineStation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

    @Embedded
    private PreStationInfo preStationInfo;

    @Builder
    public LineStation(Line line, Station station, PreStationInfo preStationInfo) {
        this.line = line;
        this.station = station;
        this.preStationInfo = preStationInfo;
    }

    public Station preStation() {
        return preStationInfo.getPreStation();
    }

    public int distance() {
        return preStationInfo.getDistance();
    }
}
