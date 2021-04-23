package me.toy.jwpjpa.favorite;

import me.toy.jwpjpa.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository <Favorite, Long> {
    List<Favorite> findByMember(Member member);
}
