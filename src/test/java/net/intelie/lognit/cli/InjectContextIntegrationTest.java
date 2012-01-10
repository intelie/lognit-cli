package net.intelie.lognit.cli;

import com.google.inject.Guice;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class InjectContextIntegrationTest {
    @Test
    public void canCreateContext() {
        ArgsParser parser = Guice.createInjector(new MainModule())
                .getInstance(ArgsParser.class);
        assertThat(parser).isInstanceOf(ArgsParser.class);
    }
}
