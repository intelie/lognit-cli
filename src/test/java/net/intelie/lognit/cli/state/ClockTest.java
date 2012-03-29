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

    @Test(timeout = 100)
    public void willSleepAtLeast10milliseconds()  throws Exception {
        final Clock clock = new Clock();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                clock.sleep(50);
            }
        });

        long start = System.currentTimeMillis();
        t.start();
        t.join();
        long difference = Math.abs(System.currentTimeMillis() - start);
        assertThat(difference).isGreaterThanOrEqualTo(50);
    }

    @Test(timeout = 100)
    public void willReturnEarlierIfThreadDies() throws Exception {
        final Clock clock = new Clock();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                clock.sleep(50);
            }
        });
        
        long start = System.currentTimeMillis();
        t.start();
        t.interrupt();
        t.join();
        long difference = Math.abs(System.currentTimeMillis() - start);
        assertThat(difference).isLessThanOrEqualTo(50);
    }
}
