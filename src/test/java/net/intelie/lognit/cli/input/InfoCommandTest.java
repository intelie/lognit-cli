package net.intelie.lognit.cli.input;

import net.intelie.lognit.cli.Lognit;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InfoCommandTest {
    @Test
    public void nameIsInfo() {
        assertThat(new InfoCommand(null, null).name()).isEqualTo("info");
    }

    @Test
    public void willCallLognitInfo() throws Exception {
        Lognit lognit = mock(Lognit.class);
        UserInput input = mock(UserInput.class);

        when(lognit.info()).thenReturn(new Welcome("abc"));

        InfoCommand info =  new InfoCommand(input, lognit);
        info.execute();

        verify(input).printf("%s\n", "abc");
    }

}
