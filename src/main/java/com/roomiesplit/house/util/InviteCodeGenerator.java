package com.roomiesplit.house.util;

import java.security.SecureRandom;

public class InviteCodeGenerator {

    private static final String CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final SecureRandom RANDOM =
            new SecureRandom();

    public static String generate() {

        StringBuilder code =
                new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(
                    CHARS.charAt(
                            RANDOM.nextInt(CHARS.length())
                    )
            );
        }

        return code.toString();
    }
}