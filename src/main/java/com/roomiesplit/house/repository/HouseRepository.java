package com.roomiesplit.house.repository;

import com.roomiesplit.house.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HouseRepository
        extends JpaRepository<House, Long> {

    Optional<House> findByInviteCode(
            String inviteCode
    );
}