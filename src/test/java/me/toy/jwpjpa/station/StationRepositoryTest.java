package me.toy.jwpjpa.station;

import me.toy.jwpjpa.line.Line;
import me.toy.jwpjpa.linestation.LineStation;
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
        Line line_shin = Line.builder()
                .name("신분당선")
                .color("빨강색")
                .build();

        Line line_2 = Line.builder()
                .name("2호선")
                .color("초록색")
                .build();

        entityManager.persist(line_shin);
        entityManager.persist(line_2);

        stationRepository.save(Station.builder()
                .name("판교역")
                .lines(Arrays.asList(line_shin))
                .build());

        stationRepository.save(Station.builder()
                .name("강남역")
                .lines(Arrays.asList(line_shin, line_2))
                .build());
    }

    @Test
    @DisplayName("Station 추가")
    void insert_station_test() {
        // given
        Station station = Station.builder()
                .name("잠실역")
                .build();

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
        Station station = Station.builder()
                .name("잠실역")
                .build();
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
