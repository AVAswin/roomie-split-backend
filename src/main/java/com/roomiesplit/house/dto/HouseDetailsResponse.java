package com.roomiesplit.house.dto;

import java.util.List;

public record HouseDetailsResponse(
        Long houseId,
        String houseName,
        String inviteCode,
        String owner,
        List<MemberResponse> members
) {
}