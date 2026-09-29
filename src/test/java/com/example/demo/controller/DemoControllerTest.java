package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Skeleton template for a controller test using MockMvc.
 *
 * You can use annotations from JUnit 5 such as @ParameterizedTest, @ValueSauce,
 * @CsvSource and @MethodSource for your test data.
 *
 * Example usage of mockMvc for a GET request
 * mockMvc.perform(get("/path-to-your-endpoint").param("your-query-param", param-value))
 *                 .andExpect(status().whateverStatusCodeYouExpect())
 *                 .andExpect(content().string("string-you-expect-in-response")).
 *                 .andExpect(jsonPath("$.jsonField").value("json-value-you-expect"));
 */
@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({
            "eloquent, loquen",
            "country, ountr",
            "person, erso",
            "xyz, y",
            "12345, 234",
            "@hello!, hello",
            "1@2, @"
    })
    void shouldRemoveFirstAndLastCharacter(
            String original,
            String expected) throws Exception {

        mockMvc.perform(get("/remove")
                        .param("original", original))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @ParameterizedTest
    @CsvSource({
            "ab",
            "12",
            "@#"
    })
    void shouldReturnEmptyStringForTwoCharacterString(
            String original) throws Exception {

        mockMvc.perform(get("/remove")
                        .param("original", original))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @ParameterizedTest
    @CsvSource({
            "a",
            "1",
            "@"
    })
    void shouldReturnBadRequestForOneCharacterString(
            String original) throws Exception {

        mockMvc.perform(get("/remove")
                        .param("original", original))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForEmptyString() throws Exception {

        mockMvc.perform(get("/remove")
                        .param("original", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleNumbersAndSpecialCharacters() throws Exception {

        /*
         * According to the stated requirement, only the first and last
         * characters are removed.
         *
         * 123%qwerty+ -> 23%qwerty
         */
        mockMvc.perform(get("/remove")
                        .param("original", "123%qwerty+"))
                .andExpect(status().isOk())
                .andExpect(content().string("23%qwerty"));
    }
}
