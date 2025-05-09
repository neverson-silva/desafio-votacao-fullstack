package com.neverson.votacao.utils;
import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public class UUIDUtils {

    public static UUID generate() {
        return UuidCreator.getTimeOrdered();
    }
}
