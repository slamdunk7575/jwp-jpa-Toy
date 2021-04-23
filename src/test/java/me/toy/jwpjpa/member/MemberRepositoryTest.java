package me.toy.jwpjpa.member;

import me.toy.jwpjpa.favorite.Favorite;
import me.toy.jwpjpa.favorite.FavoriteRepository;
import me.toy.jwpjpa.station.Station;
import me.toy.jwpjpa.station.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        Member member1 = Member.builder()
                .age(20)
                .email("pobi@test.com")
                .password("12345")
                .build();

        /*Member member2 = Member.builder()
                .age(30)
                .email("slamdunk7575@test.com")
                .password("678910")
                .build();*/

        memberRepository.save(member1);
        // memberRepository.save(member1);

        Station gangNam = stationRepository.save(Station.of("강남역"));
        Station jamSil = stationRepository.save(Station.of("잠실역"));
        Station panGyo = stationRepository.save(Station.of("판교역"));

        favoriteRepository.save(Favorite.builder()
                .departure(gangNam)
                .arrival(jamSil)
                .member(member1)
                .build());

        favoriteRepository.save(Favorite.builder()
                .departure(jamSil)
                .arrival(panGyo)
                .member(member1)
                .build());
    }

    @Test
    @DisplayName("Member 추가")
    void insert_member_test() {
        // given
        Member member = Member.builder()
                .age(10)
                .email("ykj@test.com")
                .password("1234")
                .build();

        // when
        Member persistedMember = memberRepository.save(member);

        // then
        assertThat(persistedMember.getId()).isNotNull();
        assertThat(persistedMember.getCreatedDate()).isNotNull();
        assertThat(persistedMember.getModifiedDate()).isNotNull();
    }

    @Test
    @DisplayName("Member 조회")
    void select_member_test() {
        // given
        Member member = Member.builder()
                .age(10)
                .email("ykj@test.com")
                .password("1234")
                .build();

        // when
        Member persistedMember = memberRepository.save(member);

        // then
        assertAll(
                () -> assertThat(persistedMember.getAge()).isEqualTo(10),
                () -> assertThat(persistedMember.getEmail()).isEqualTo("ykj@test.com"),
                () -> assertThat(persistedMember.getPassword()).isEqualTo("1234")
        );
    }

    @Test
    @DisplayName("Member 수정")
    void update_member_test() {
        // given
        String changeEmail = "change@test.com";

        // when
        Member member = memberRepository.findByEmail("pobi@test.com");
        member.updateEmail(changeEmail);

        Member updateMember = memberRepository.findByEmail(changeEmail);

        // then
        assertThat(updateMember.getEmail()).isEqualTo(changeEmail);
    }

    @Test
    @DisplayName("Member 삭제")
    void delete_member_test() {
        // given
        Member member = memberRepository.findByEmail("pobi@test.com");

        // when
        memberRepository.delete(member);

        // then
        Member deletedMember = memberRepository.findByEmail("pobi@test.com");
        assertThat(deletedMember).isNull();
    }

    @Test
    @DisplayName("Member 전체 조회")
    void select_all_member_test() {
        // given
        Member member = Member.builder()
                .age(19)
                .email("ykj@test.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        // when
        List<Member> members = memberRepository.findAll();
        List<String> memberEmails = members.stream()
                .map(Member::getEmail)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(memberEmails.size()).isEqualTo(2),
                () -> assertThat(memberEmails).contains("pobi@test.com", "ykj@test.com")
        );
    }

    @Test
    @DisplayName("사용자는 여러 즐겨찾기를 가질 수 있다")
    void member_have_favorites_test() {
        // given
        Member member = memberRepository.findByEmail("pobi@test.com");

        // when
        List<Favorite> favorites = member.getFavorites();

        // then
        assertAll(
                () -> assertThat(favorites).hasSize(2),
                () -> assertThat(favorites).contains(
                        Favorite.builder()
                                .departure(Station.of("강남역"))
                                .arrival(Station.of("잠실역"))
                                .member(member)
                                .build(),
                        Favorite.builder()
                                .departure(Station.of("잠실역"))
                                .arrival(Station.of("판교역"))
                                .member(member)
                                .build())

        );

    }
}
