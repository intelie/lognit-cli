package net.intelie.lognit.cli.state;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ClockTest {
    @Test
    public void clockIsntSoOffYet() {
        Clock clock = new Clock();
        long value = clock.currentMillis();
        long difference = Math.abs(System.currentTimeMillis() - value);
        assertThat(difference).isLessThan(100);
    }
}
