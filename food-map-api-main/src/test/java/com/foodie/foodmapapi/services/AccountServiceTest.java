package com.foodie.foodmapapi.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @BeforeAll
    static void setup() {

    }

    @BeforeEach
    void init() {

    }

    @Test
    @DisplayName("Get Account - Success")
    void testGetAccount_Success() {
        assertEquals("Temp Test", "Temp Test");
    }
}
