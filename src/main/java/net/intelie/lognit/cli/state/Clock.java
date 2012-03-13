package net.intelie.lognit.cli.state;

public class Clock {
    public long currentMillis() {
        return System.currentTimeMillis();
    }

    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
        }
    }
}
