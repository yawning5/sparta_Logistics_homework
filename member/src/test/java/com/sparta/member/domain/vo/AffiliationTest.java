package com.keepgoing.member.domain.vo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AffiliationTest {

    @Test
    @DisplayName("RED - 인자에 null 이 들어온 경우")
    void of() {
        Type t = Type.COMPANY;
        UUID id = UUID.randomUUID();
        String name = null;

        assertThrows(IllegalArgumentException.class, () ->
            new Affiliation(t, id, name)
        );
    }

    @Test
    @DisplayName("GREEN")
    void of2() {
        Type t = Type.COMPANY;
        UUID id = UUID.randomUUID();
        String name = "회사이름";

        Affiliation a = new Affiliation(t, id, name);

        assertAll(
            () -> assertTrue(a.isType(Type.COMPANY)),
            () -> assertEquals(id, a.id()),
            () -> assertEquals(name, a.name())
        );
    }
}