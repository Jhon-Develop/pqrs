package com.pqrs.microservice.domain;

import com.pqrs.microservice.domain.enums.PqrsType;
import com.pqrs.microservice.domain.enums.ResponsibleRole;
import com.pqrs.microservice.domain.exception.BusinessRuleException;
import com.pqrs.microservice.infrastructure.persistence.entity.AnswerEntity;
import com.pqrs.microservice.infrastructure.persistence.entity.CitizenEntity;
import com.pqrs.microservice.infrastructure.persistence.entity.PqrsEntity;
import com.pqrs.microservice.infrastructure.persistence.entity.ResponsibleEntity;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

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

    @Test
    void shouldNotReassignClosedPqrs() {
        CitizenEntity citizen = new CitizenEntity(
                "101823948", "Carlos Mendoza", "carlos@gmail.com", "3001234567");
        ResponsibleEntity responsible = new ResponsibleEntity(
                "987654321", "Ana Gomez", "ana.gomez@empresa.com",
                "Soporte Tecnico", ResponsibleRole.ANALISTA);

        PqrsEntity pqrs = new PqrsEntity(
                PqrsType.PETICION, "Una petición válida", LocalDate.now(), citizen);

        pqrs.assignResponsible(responsible);
        pqrs.passToManagement();
        pqrs.recordAnswer(new AnswerEntity("Respuesta", LocalDate.now(), responsible));
        pqrs.closeCase();

        assertThrows(BusinessRuleException.class, () -> pqrs.assignResponsible(responsible));
    }


}