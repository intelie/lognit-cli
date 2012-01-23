package net.intelie.lognit.cli.input;

import jline.ConsoleReader;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserConsoleTest {

    private ByteArrayOutputStream err;
    private ByteArrayOutputStream out;
    private ConsoleReader console;

    @Before
    public void setUp() throws Exception {
        err = spy(new ByteArrayOutputStream());
        out = spy(new ByteArrayOutputStream());
        console = spy(new ConsoleReader(null, new OutputStreamWriter(err)));
    }

    @Test
    public void willPrintMessageToStdout() throws Exception {
        UserConsole input = new UserConsole(console, new PrintWriter(out));
        input.printOut("abc%d", 1);

        assertThat(out.toString()).isEqualTo(safe("abc1\n"));
    }


    @Test
    public void willPrintToStderr() throws Exception {
        UserConsole input = new UserConsole(console, null);
        input.println("abc%d", 1);

        assertThat(err.toString()).isEqualTo(safe("abc1\n"));
    }

    @Test
    public void willReadFromStdin() throws Exception {
        console.setInput(mockIn("abc\nqwe\n"));
        UserConsole input = new UserConsole(console, null);
        assertThat(input.readLine("field1: ")).isEqualTo("abc");
        assertThat(input.readLine("field2: ")).isEqualTo("qwe");
        verify(console).readLine("field1: ");
        verify(console).readLine("field2: ");
        assertThat(err.toString()).isEqualTo(safe("field1: abc\nfield2: qwe\n"));

    }

    @Test
    public void willReadPasswordFromStdin() throws Exception {
        console.setInput(mockIn("abc\nqwe\n"));
        UserConsole input = new UserConsole(console, null);
        assertThat(input.readPassword("field1: ")).isEqualTo("abc");
        assertThat(input.readPassword("field2: ")).isEqualTo("qwe");
        verify(console).readLine("field1: ", '\0');
        verify(console).readLine("field2: ", '\0');
        assertThat(err.toString()).isEqualTo(safe("field1: \nfield2: \n"));
    }

    @Test
    public void willSurviveInputStreamFailures() throws Exception {
        InputStream in = mock(InputStream.class);
        OutputStreamWriter out = mock(OutputStreamWriter.class);
        when(in.read()).thenThrow(new IOException());

        UserConsole input = new UserConsole(new ConsoleReader(in, out), null);
        assertThat(input.readPassword("abc")).isEqualTo("");
        assertThat(input.readLine("qwe")).isEqualTo("");
    }

    @Test
    public void willSurviveOutputFailures() throws Exception {
        OutputStreamWriter out = mock(OutputStreamWriter.class);
        doThrow(new IOException()).when(out).write(any(char[].class));

        UserConsole input = new UserConsole(new ConsoleReader(null, out), null);
        input.println("abc");
        
        verify(out).write(any(char[].class));
    }

    private InputStream mockIn(String text) {
        return spy(new ByteArrayInputStream(text.getBytes()));
    }
    
    private String safe(String str) {
        return str.replace("\n", System.getProperty("line.separator"));
    }
    
}
