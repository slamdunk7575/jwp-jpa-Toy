package me.toy.jwpjpa.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository.save(Line.of("2호선", "초록색"));
        lineRepository.save(Line.of("4호선", "파랑색"));
    }

    @Test
    @DisplayName("Line 추가")
    void insert_line_test() {
        // given
        Line line = Line.of("8호선", "분홍색");

        // when
        Line persistedLine = lineRepository.save(line);

        // then
        assertThat(persistedLine.getId()).isNotNull();
        assertThat(persistedLine.getCreatedDate()).isNotNull();
        assertThat(persistedLine.getModifiedDate()).isNotNull();
    }

    @Test
    @DisplayName("Line 조회")
    void select_line_test() {
        // given
        String lineName = "2호선";

        // when
        Line persistedLine = lineRepository.findByName(lineName);

        // then
        assertAll(
                () -> assertThat(persistedLine.getId()).isNotNull(),
                () -> assertThat(persistedLine.getName()).isEqualTo("2호선"),
                () -> assertThat(persistedLine.getColor()).isEqualTo("초록색")
        );
    }

    @Test
    @DisplayName("Line 수정")
    void update_line_test() {
        // given
        String changeName = "1호선";

        // when
        Line line = lineRepository.findByName("4호선");
        line.updateName(changeName);

        Line updatedLine = lineRepository.findByName(changeName);

        // then
        assertAll(
                () -> assertThat(updatedLine.getName()).isEqualTo("1호선"),
                () -> assertThat(updatedLine.getColor()).isEqualTo("파랑색")
        );
    }

    @Test
    @DisplayName("Line 삭제")
    void delete_line_test() {
        // given
        Line line = lineRepository.findByName("2호선");

        // when
        lineRepository.delete(line);

        // then
        Line deleteLine = lineRepository.findByName("2호선");
        assertThat(deleteLine).isNull();
    }

    @Test
    @DisplayName("Line 전체 조회")
    void select_all_line_test() {
        // given
        Line line = Line.of("8호선", "분홍색");
        lineRepository.save(line);

        // when
        List<Line> lines = lineRepository.findAll();
        List<String> lineNames = lines.stream()
                .map(Line::getName)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(lineNames.size()).isEqualTo(3),
                () -> assertThat(lineNames).contains("8호선", "4호선", "2호선")
        );
    }

}
