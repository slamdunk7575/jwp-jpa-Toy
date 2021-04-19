package me.toy.jwpjpa.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        favoriteRepository.save(new Favorite());
        favoriteRepository.save(new Favorite());
    }

    @Test
    @DisplayName("Favorite 조회")
    void select_favorite_test() {
        // given
        Favorite favorite = favoriteRepository.findById(1L).get();

        // when & then
        assertAll(
                () -> assertThat(favorite.getId()),
                () -> assertThat(favorite).isNotNull()
        );
    }

    @Test
    @DisplayName("Favorite 추가")
    void insert_favorite_test() {
        // given
        Favorite favorite = new Favorite();

        // when
        Favorite persistedFavorite = favoriteRepository.save(favorite);
        List<Favorite> favorites = favoriteRepository.findAll();

        // then
        assertThat(persistedFavorite.getId()).isNotNull();
        assertThat(persistedFavorite.getCreatedDate()).isNotNull();
        assertThat(persistedFavorite.getModifiedDate()).isNotNull();
        assertThat(favorites.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Favorite 삭제")
    void delete_favorite_test() {
        // given
        Favorite favorite = favoriteRepository.findById(1L)
                .orElseThrow(EntityNotFoundException::new);

        // when
        favoriteRepository.delete(favorite);
        List<Favorite> favorites = favoriteRepository.findAll();

        // then
        assertThat(favorites).hasSize(1);
    }

    @Test
    @DisplayName("Favorite 전체 조회")
    void select_all_favorite_test() {
        // given
        Favorite favorite = new Favorite();
        favoriteRepository.save(favorite);

        // when
        List<Favorite> favorites = favoriteRepository.findAll();

        // then
        assertThat(favorites.size()).isEqualTo(3);
    }

}
