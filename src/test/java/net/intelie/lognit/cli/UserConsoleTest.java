package net.intelie.lognit.cli;

import jline.ConsoleOperations;
import jline.ConsoleReader;
import jline.CursorBuffer;
import net.intelie.lognit.cli.UserConsole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
    public void willPrintStillToStderr() throws Exception {
        CursorBuffer buffer = mock(CursorBuffer.class);
        when(console.getCursorBuffer()).thenReturn(buffer);

        UserConsole input = new UserConsole(console, null);
        input.printStill("abc%d", 1);
        
        verify(buffer).clearBuffer();
        verify(console).setDefaultPrompt(null);
        verify(console).killLine();
        assertThat(err.toString()).isEqualTo(safe("abc1"));
    }

    @Test
    public void willFixUnfinishedLines() throws Exception {
        UserConsole input = new UserConsole(console, null);

        input.fixCursor();
        assertThat(err.toString()).isEqualTo(safe(""));

        input.printStill("abc");

        assertThat(err.toString()).isEqualTo(safe("abc"));
        input.fixCursor();
        assertThat(err.toString()).isEqualTo(safe("abc\n"));

        input.println("abc");
        assertThat(err.toString()).isEqualTo(safe("abc\nabc\n"));

        input.fixCursor();
        assertThat(err.toString()).isEqualTo(safe("abc\nabc\n"));
    }

    @Test
    public void willRegisterFixAtRuntime() throws Exception {
        Runtime runtime = mock(Runtime.class);
        UserConsole input = new UserConsole(console, null);
        input.registerFix(runtime);

        input.printStill("abc");

        ArgumentCaptor<Thread> captor = ArgumentCaptor.forClass(Thread.class);
        verify(runtime).addShutdownHook(captor.capture());
        captor.getValue().run();
        
        assertThat(err.toString()).isEqualTo(safe("abc\n"));
    }

    @Test
    public void willReadCharFromStdin() throws Exception {
        console.setInput(mockIn("abdq"));
        UserConsole input = new UserConsole(console, null);
        assertThat(input.waitChar('q', 'd')).isEqualTo('d');
        assertThat(input.waitChar('q')).isEqualTo('q');
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
        assertThat(input.waitChar('a', 'b')).isEqualTo('\0');
    }

    @Test
    public void willSurviveOutputFailures() throws Exception {
        OutputStreamWriter out = mock(OutputStreamWriter.class);
        doThrow(new IOException()).when(out).write(any(char[].class));

        UserConsole input = new UserConsole(new ConsoleReader(null, out), null);
        input.println("abc");
        input.printStill("abc");
    }

    private InputStream mockIn(String text) {
        return spy(new ByteArrayInputStream(text.getBytes()));
    }
    
    private String safe(String str) {
        return str.replace("\n", System.getProperty("line.separator"));
    }
    
}
