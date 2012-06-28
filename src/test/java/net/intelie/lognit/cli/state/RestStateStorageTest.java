package net.intelie.lognit.cli.state;

import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestState;
import net.intelie.lognit.cli.json.Jsonizer;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RestStateStorageTest {

    private RestClient client;

    @Before
    public void setUp() throws Exception {
        client = mock(RestClient.class, RETURNS_DEEP_STUBS);
    }


    @Test
    public void canStore() throws Exception {
        File file = File.createTempFile("test", "tmp");

        try {
            when(client.getState()).thenReturn(new RestState(new Cookie[] { new Cookie("A", "B", "C", "D", new Date(12345), true)}, "svr"));

            RestStateStorage storage = new RestStateStorage(file);
            storage.storeFrom(client);

            assertThat(FileUtils.readFileToString(file)).isEqualTo("#svr\na\tTRUE\tD\tTRUE\t12\tB\tC\n");
        } finally {
            file.delete();
        }
    }

    @Test
    public void canRead() throws Exception {
        File file = File.createTempFile("test", "tmp");

        try {
            FileUtils.writeStringToFile(file, "#svr\n" +
                    "a\tTRUE\tD\tTRUE\t12\tB\tC\n");

            RestStateStorage storage = new RestStateStorage(file);
            storage.recoverTo(client);

            ArgumentCaptor<RestState> captor = ArgumentCaptor.forClass(RestState.class);

            verify(client).setState(captor.capture());
            RestState value = captor.getValue();
            assertThat(value.getServer()).isEqualTo("svr");
            assertThat(value.getCookies()).containsOnly(new Cookie("A", "B", "C", "D", new Date(12345), true));

        } finally {
            file.delete();
        }
    }
}
