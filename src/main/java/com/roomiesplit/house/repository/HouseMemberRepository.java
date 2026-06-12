package com.roomiesplit.house.repository;

import com.roomiesplit.house.entity.House;
import com.roomiesplit.house.entity.HouseMember;
import com.roomiesplit.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface HouseMemberRepository
        extends JpaRepository<HouseMember, Long> {

    List<HouseMember> findByHouse(
            House house
    );

    Optional<HouseMember> findByUser(
            User user
    );

    boolean existsByHouseAndUser(
            House house,
            User user
    );

    boolean existsByUser(User user);

    @Transactional
    void deleteByUser(User user);
}