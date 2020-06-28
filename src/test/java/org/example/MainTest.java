package org.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MainTest {

    @Test
    void name() {
        assertThat("1").isEqualTo("1");
    }
}