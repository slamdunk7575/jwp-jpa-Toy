package me.toy.jwpjpa.station;

import me.toy.jwpjpa.line.Line;
import me.toy.jwpjpa.linestation.LineStation;
import me.toy.jwpjpa.linestation.PreStationInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private EntityManager entityManager;

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

        Station gangNam = Station.of("강남역");
        Station panGyo = Station.of("판교역");

        LineStation gangNamLineStation1 = LineStation.builder()
                .line(lineTwo)
                .station(gangNam)
                .preStationInfo(PreStationInfo.of(panGyo, 10))
                .build();
        gangNam.addLineStation(gangNamLineStation1);

        LineStation gangNamLineStation2 = LineStation.builder()
                .line(lineShin)
                .station(gangNam)
                .preStationInfo(PreStationInfo.of(panGyo, 10))
                .build();
        gangNam.addLineStation(gangNamLineStation2);

        LineStation panGyoLineStation = LineStation.builder()
                .line(lineShin)
                .station(panGyo)
                .preStationInfo(PreStationInfo.of(null, 0))
                .build();
        panGyo.addLineStation(panGyoLineStation);

        stationRepository.save(gangNam);
        stationRepository.save(panGyo);
    }

    @Test
    @DisplayName("Station 추가")
    void insert_station_test() {
        // given
        Station station = Station.of("잠실역");

        // when
        Station persistedStation = stationRepository.save(station);

        // then
        assertThat(persistedStation.getId()).isNotNull();
        assertThat(persistedStation.getCreatedDate()).isNotNull();
        assertThat(persistedStation.getModifiedDate()).isNotNull();
    }

    @Test
    @DisplayName("Station 조회")
    void select_station_test() {
        // given
        String station = "강남역";

        // when
        Station persistStation = stationRepository.findByName(station);

        // then
        assertThat(persistStation.getName()).isEqualTo("강남역");
    }

    @Test
    @DisplayName("Station 수정")
    void update_station_test() {
        // given
        String changeStation = "이대역";

        // when
        Station station = stationRepository.findByName("강남역");
        station.updateName(changeStation);

        Station updatedStation = stationRepository.findByName(changeStation);

        // then
        assertThat(updatedStation.getName()).isEqualTo(changeStation);
    }

    @Test
    @DisplayName("Station 삭제")
    void delete_station_test() {
        // given
        Station station = stationRepository.findByName("강남역");

        // when
        stationRepository.delete(station);

        // then
        Station deletedStation = stationRepository.findByName("강남역");
        assertThat(deletedStation).isNull();
    }

    @Test
    @DisplayName("Station 전체 조회")
    void select_all_station_test() {
        // given
        Station station = Station.of("잠실역");
        stationRepository.save(station);

        // when
        List<Station> stations = stationRepository.findAll();
        List<String> stationNames = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(stationNames.size()).isEqualTo(3),
                () -> assertThat(stationNames).contains("판교역", "잠실역", "강남역")
        );
    }

    @Test
    @DisplayName("지하철역 조회 시 어느 노선에 속하는지 테스트")
    void get_station_included_line_test() {
        // given
        Station station = stationRepository.findByName("강남역");
        List<LineStation> lineStations = station.getLineStations();

        // when
        List<Line> lines = Optional.ofNullable(lineStations)
                .orElse(Collections.emptyList()).stream()
                .map(LineStation::getLine)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(lines).hasSize(2),
                () -> assertThat(lines).contains(
                            Line.builder()
                                    .name("2호선")
                                    .color("초록색")
                                    .build(),
                            Line.builder()
                                    .name("신분당선")
                                    .color("빨강색")
                                    .build())
        );
    }
}
