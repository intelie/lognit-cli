package net.intelie.lognit.cli;

import jline.ConsoleReader;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserInputTest {

    private ByteArrayOutputStream out;
    private ConsoleReader console;

    @Before
    public void setUp() throws Exception {
        out = spy(new ByteArrayOutputStream());
        console = spy(new ConsoleReader(null, new OutputStreamWriter(out)));
    }

    @Test
    public void willPrintToStdout() throws Exception {
        UserInput input = new UserInput(console);
        input.printf("abc%d\nqwe%s\n", 1, "r");

        assertThat(out.toString()).isEqualTo("abc1\nqwer\n");
    }

    @Test
    public void willReadFromStdin() throws Exception {
        console.setInput(mockIn("abc\nqwe\n"));
        UserInput input = new UserInput(console);
        assertThat(input.readLine("field1: ")).isEqualTo("abc");
        assertThat(input.readLine("field2: ")).isEqualTo("qwe");
        verify(console).readLine("field1: ");
        verify(console).readLine("field2: ");
        assertThat(out.toString()).isEqualTo("field1: abc\nfield2: qwe\n");

    }

    @Test
    public void willReadPasswordFromStdin() throws Exception {
        console.setInput(mockIn("abc\nqwe\n"));
        UserInput input = new UserInput(console);
        assertThat(input.readPassword("field1: ")).isEqualTo("abc");
        assertThat(input.readPassword("field2: ")).isEqualTo("qwe");
        verify(console).readLine("field1: ", '\0');
        verify(console).readLine("field2: ", '\0');
        assertThat(out.toString()).isEqualTo("field1: \nfield2: \n");
    }

    @Test
    public void willSurviveInputStreamFailures() throws Exception {
        InputStream in = mock(InputStream.class);
        OutputStreamWriter out = mock(OutputStreamWriter.class);
        when(in.read()).thenThrow(new IOException());

        UserInput input = new UserInput(new ConsoleReader(in, out));
        assertThat(input.readPassword("abc")).isEqualTo("");
        assertThat(input.readLine("qwe")).isEqualTo("");
    }

    @Test
    public void willSurviveOutputFailures() throws Exception {
        OutputStreamWriter out = mock(OutputStreamWriter.class);
        doThrow(new IOException()).when(out).write(any(char[].class));

        UserInput input = new UserInput(new ConsoleReader(null, out));
        input.printf("abc");
        
        verify(out).write(any(char[].class));
    }

    private InputStream mockIn(String text) {
        return spy(new ByteArrayInputStream(text.getBytes()));
    }
    
}
