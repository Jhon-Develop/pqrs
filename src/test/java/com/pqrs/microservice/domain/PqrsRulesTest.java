package com.pqrs.microservice.domain;

import com.pqrs.microservice.domain.enums.PqrsType;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PqrsRulesTest {

    @Test
    void shouldParseTypeIgnoringCaseAndSpaces() {
        assertEquals(PqrsType.QUEJA, PqrsType.fromValue("  queja "));
    }

    @Test
    void shouldRejectInvalidType() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> PqrsType.fromValue("FELICITACION")
        );

        assertTrue(ex.getMessage().contains("Tipo de PQRS inválido"));
    }

}