package me.toy.jwpjpa.member;

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

    @BeforeEach
    void setUp() {
        memberRepository.save(Member.of(20, "pobi@test.com", "12345"));
        memberRepository.save(Member.of(30, "slamdunk@test.com", "678910"));
    }

    @Test
    @DisplayName("Member 추가")
    void insert_member_test() {
        // given
        Member member = Member.of(10, "ykj@test.com", "757575");

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
        Member member = Member.of(10, "ykj@test.com", "757575");

        // when
        Member persistedMember = memberRepository.save(member);

        // then
        assertAll(
                () -> assertThat(persistedMember.getAge()).isEqualTo(10),
                () -> assertThat(persistedMember.getEmail()).isEqualTo("ykj@test.com"),
                () -> assertThat(persistedMember.getPassword()).isEqualTo("757575")
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
        Member member = memberRepository.findByEmail("slamdunk@test.com");

        // when
        memberRepository.delete(member);

        // then
        Member deletedMember = memberRepository.findByEmail("slamdunk@test.com");
        assertThat(deletedMember).isNull();
    }

    @Test
    @DisplayName("Member 전체 조회")
    void select_all_member_test() {
        // given
        Member member = Member.of(19, "ykj@test.com", "757575");
        memberRepository.save(member);

        // when
        List<Member> members = memberRepository.findAll();
        List<String> memberEmails = members.stream()
                .map(Member::getEmail)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(memberEmails.size()).isEqualTo(3),
                () -> assertThat(memberEmails).contains("pobi@test.com", "slamdunk@test.com", "ykj@test.com")
        );
    }
}
