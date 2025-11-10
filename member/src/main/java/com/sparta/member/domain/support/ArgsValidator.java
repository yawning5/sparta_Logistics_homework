package com.sparta.member.domain.support;

public final class ArgsValidator {

    private ArgsValidator() {}

    public static void requireAllNonNull(Object... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("arguments must be in name-value pairs");
        }

        for (int i = 0; i < args.length; i += 2) {
            String name = String.valueOf(args[i]);
            Object value = args[i + 1];
            if (value == null) {
                throw new IllegalArgumentException(name + " must not be null");
            }
        }
    }
}
