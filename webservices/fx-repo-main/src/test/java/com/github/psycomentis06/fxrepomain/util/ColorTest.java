package com.github.psycomentis06.fxrepomain.util;

import com.github.psycomentis06.fxrepomain.util.Color;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class ColorTest {
    @Test
    public void isRgba() {
        assertThat(Color.isRgba("rgba(255, 0, 0, 1.0)"))
                .as("assertion 1")
                .isTrue();
        assertThat(Color.isRgba("rgba(255, 0, 0, 0.0)"))
                .as("assertion 2")
                .isTrue();
        assertThat(Color.isRgba("rgba(255,200,0,0.5)"))
                .as("assertion 3")
                .isTrue();
       assertThat(Color.isRgba("rgb(255,0,0)"))
               .as("assertion 4")
               .isFalse();
       assertThat(Color.isRgba("hsl(0, 100%, 50%)"))
               .as("assertion 5")
               .isFalse();
    }
}
