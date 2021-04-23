package me.toy.jwpjpa.favorite;

import me.toy.jwpjpa.member.Member;
import me.toy.jwpjpa.member.MemberRepository;
import me.toy.jwpjpa.station.Station;
import me.toy.jwpjpa.station.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .age(20)
                .email("pobi@test.com")
                .password("12345")
                .build();

        // memberRepository.save(member);

        Station gangNam = stationRepository.save(Station.of("강남역"));
        Station jamSil = stationRepository.save(Station.of("잠실역"));
        Station panGyo = stationRepository.save(Station.of("판교역"));

        favoriteRepository.save(Favorite.builder()
                .departure(gangNam)
                .arrival(jamSil)
                .member(member)
                .build());

        favoriteRepository.save(Favorite.builder()
                .departure(jamSil)
                .arrival(panGyo)
                .member(member)
                .build());
    }

    @Test
    @DisplayName("사용자로 Favorite 목록 조회")
    void select_favorite_by_member_test() {
        // given
        Member member = memberRepository.findByEmail("pobi@test.com");

        // when
        List<Favorite> favorites = favoriteRepository.findByMember(member);

        // then
        assertAll(
                () -> assertThat(favorites).isNotNull(),
                () -> assertThat(favorites).hasSize(2)
        );
    }

    @Test
    @DisplayName("Favorite 추가")
    void insert_favorite_test() {
        // given
        Member member = memberRepository.findByEmail("pobi@test.com");
        Station gangNam = stationRepository.findByName("강남역");
        Station panGyo = stationRepository.findByName("판교역");

        Favorite favorite = Favorite.builder()
                .departure(gangNam)
                .arrival(panGyo)
                .member(member)
                .build();

        // when
        Favorite persistFavorite = favoriteRepository.save(favorite);
        List<Favorite> favorites = favoriteRepository.findAll();

        // then
        assertThat(persistFavorite.getId()).isNotNull();
        assertThat(persistFavorite.getCreatedDate()).isNotNull();
        assertThat(persistFavorite.getModifiedDate()).isNotNull();
        assertThat(favorites).hasSize(3);
    }

    @Test
    @DisplayName("Favorite 전체 조회")
    void select_all_favorites_test() {
        // given
        int expected = 2;

        // when
        List<Favorite> favorites = favoriteRepository.findAll();

        // then
        assertThat(favorites).hasSize(2);
    }

    @Test
    @DisplayName("사용자가 삭제되면 지정된 Favorite 삭제")
    void delete_favorite_by_member_test() {
        // given
        Member member = memberRepository.findByEmail("pobi@test.com");

        // when
        memberRepository.delete(member);

        // then
        assertThat(favoriteRepository.findByMember(member)).isEmpty();
    }
}
