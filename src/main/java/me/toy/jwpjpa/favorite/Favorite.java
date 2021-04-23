package me.toy.jwpjpa.favorite;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.jwpjpa.base.BaseEntity;
import me.toy.jwpjpa.member.Member;
import me.toy.jwpjpa.station.Station;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Favorite extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_station_id")
    private Station departure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_station_id")
    private Station arrival;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Favorite(Station departure, Station arrival, Member member) {
        this.departure = departure;
        this.arrival = arrival;
        this.member = member;
        member.addFavorite(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(departure, favorite.departure) && Objects.equals(arrival, favorite.arrival) && Objects.equals(member, favorite.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departure, arrival, member);
    }
}
