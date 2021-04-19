package me.toy.jwpjpa.station;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.toy.jwpjpa.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Station extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    public Station(String name) {
        this.name = name;
    }

    public Station updateName(String name) {
        this.name = name;
        return this;
    }
}
