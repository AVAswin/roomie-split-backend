package com.roomiesplit.house.service;

import com.roomiesplit.house.dto.*;
import com.roomiesplit.house.entity.House;
import com.roomiesplit.house.entity.HouseMember;
import com.roomiesplit.house.repository.HouseMemberRepository;
import com.roomiesplit.house.repository.HouseRepository;
import com.roomiesplit.house.util.InviteCodeGenerator;
import com.roomiesplit.user.entity.User;
import com.roomiesplit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final HouseMemberRepository houseMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateHouseResponse createHouse(
            String email,
            CreateHouseRequest request
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        if (houseMemberRepository.existsByUser(user)) {

            throw new RuntimeException(
                    "User already belongs to a house"
            );
        }

        House house = House.builder()
                .name(request.name())
                .inviteCode(
                        InviteCodeGenerator.generate()
                )
                .owner(user)
                .createdAt(LocalDateTime.now())
                .build();

        house = houseRepository.save(house);

        HouseMember member =
                HouseMember.builder()
                        .house(house)
                        .user(user)
                        .joinedAt(LocalDateTime.now())
                        .build();

        houseMemberRepository.save(member);

        return new CreateHouseResponse(
                house.getId(),
                house.getName(),
                house.getInviteCode()
        );
    }

    @Transactional
    public void joinHouse(
            String email,
            JoinHouseRequest request
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        if (houseMemberRepository.existsByUser(user)) {

            throw new RuntimeException(
                    "User already belongs to a house"
            );
        }

        House house = houseRepository
                .findByInviteCode(
                        request.inviteCode()
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Invalid invite code"
                        )
                );

        HouseMember member =
                HouseMember.builder()
                        .house(house)
                        .user(user)
                        .joinedAt(LocalDateTime.now())
                        .build();

        houseMemberRepository.save(member);
    }

    public HouseDetailsResponse getMyHouse(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        HouseMember membership =
                houseMemberRepository
                        .findByUser(user)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User does not belong to a house"
                                )
                        );

        House house = membership.getHouse();

        List<MemberResponse> members =
                houseMemberRepository
                        .findByHouse(house)
                        .stream()
                        .map(member ->
                                new MemberResponse(
                                        member.getUser().getId(),
                                        member.getUser().getName(),
                                        member.getUser().getEmail()
                                )
                        )
                        .toList();

        return new HouseDetailsResponse(
                house.getId(),
                house.getName(),
                house.getInviteCode(),
                house.getOwner().getName(),
                members
        );
    }

    @Transactional
    public void leaveHouse(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        HouseMember membership =
                houseMemberRepository
                        .findByUser(user)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User is not in any house"
                                )
                        );

        House house = membership.getHouse();

        if (house.getOwner().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Owner cannot leave house"
            );
        }

        houseMemberRepository.delete(membership);
    }

    public List<MemberResponse> getMembers(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        HouseMember membership =
                houseMemberRepository
                        .findByUser(user)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User does not belong to a house"
                                )
                        );

        House house = membership.getHouse();

        return houseMemberRepository
                .findByHouse(house)
                .stream()
                .map(member ->
                        new MemberResponse(
                                member.getUser().getId(),
                                member.getUser().getName(),
                                member.getUser().getEmail()
                        )
                )
                .toList();
    }
}