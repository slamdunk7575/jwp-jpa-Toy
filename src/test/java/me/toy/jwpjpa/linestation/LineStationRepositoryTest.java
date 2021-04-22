package me.toy.jwpjpa.linestation;

import me.toy.jwpjpa.line.Line;
import me.toy.jwpjpa.station.Station;
import me.toy.jwpjpa.station.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LineStationRepositoryTest {

    @Autowired
    private LineStationRepository lineStationRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        Line lineShin = Line.builder()
                .name("신분당선")
                .color("빨강색")
                .build();

        Line lineTwo = Line.builder()
                .name("2호선")
                .color("초록색")
                .build();

        entityManager.persist(lineShin);
        entityManager.persist(lineTwo);

        Station jamSil = Station.of("잠실역");
        Station gangNam = Station.of("강남역");
        Station panGyo = Station.of("판교역");

        LineStation jamSilLineStation = LineStation.builder()
                .line(lineTwo)
                .station(jamSil)
                .preStationInfo(PreStationInfo.of(gangNam, 15))
                .build();
        jamSil.addLineStation(jamSilLineStation);

        LineStation gangNamLineStation1 = LineStation.builder()
                .line(lineTwo)
                .station(gangNam)
                .preStationInfo(PreStationInfo.of(null, 0))
                .build();
        gangNam.addLineStation(gangNamLineStation1);

        LineStation gangNameLineStation2 = LineStation.builder()
                .line(lineShin)
                .station(gangNam)
                .preStationInfo(PreStationInfo.of(null, 0))
                .build();
        gangNam.addLineStation(gangNameLineStation2);

        LineStation panGyoLineStation = LineStation.builder()
                .line(lineShin)
                .station(panGyo)
                .preStationInfo(PreStationInfo.of(gangNam, 10))
                .build();
        panGyo.addLineStation(panGyoLineStation);

        stationRepository.save(jamSil);
        stationRepository.save(gangNam);
        stationRepository.save(panGyo);
    }

    @Test
    @DisplayName("현재 역의 이전 역 이름과 거리")
    void previous_station_distance_test() {
        LineStation 이호선_잠실역 = lineStationRepository.findByLineNameAndStationName("2호선", "잠실역");
        LineStation 신분당선_판교역 = lineStationRepository.findByLineNameAndStationName("신분당선", "판교역");

        assertThat(이호선_잠실역.preStation().getName()).isEqualTo("강남역");
        assertThat(이호선_잠실역.distance()).isEqualTo(15);

        assertThat(신분당선_판교역.preStation().getName()).isEqualTo("강남역");
        assertThat(신분당선_판교역.distance()).isEqualTo(10);
    }
}
