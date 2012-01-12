package net.intelie.lognit.cli.state;

import net.intelie.lognit.cli.http.Jsonizer;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestState;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RestStateStorageTest {

    private Jsonizer jsonizer;
    private RestClient client;

    @Before
    public void setUp() throws Exception {
        jsonizer = mock(Jsonizer.class);
        client = mock(RestClient.class, RETURNS_DEEP_STUBS);
    }

    @Test
    public void whenStoringFromClientVerifyIfTheArrayWasSaved() throws Exception {
        when(jsonizer.to(client.getState())).thenReturn("42");

        File file = File.createTempFile("test", "tmp");

        RestStateStorage storage = new RestStateStorage(file, jsonizer);
        storage.storeFrom(client);

        assertThat(FileUtils.readFileToString(file)).isEqualTo("42");
    }

    @Test
    public void whenStoringWithException() throws Exception {
        when(jsonizer.to(client.getState())).thenReturn("42");

        File file = spy(File.createTempFile("test", "tmp"));
        when(file.getParentFile()).thenThrow(new RuntimeException());

        RestStateStorage storage = new RestStateStorage(file, jsonizer);
        storage.storeFrom(client);

        assertThat(FileUtils.readFileToString(file)).isEmpty();
    }

    @Test
    public void whenStoringNonExistingFileCheckIfItWasCreated() throws Exception {
        when(jsonizer.to(client.getState())).thenReturn("42");

        File file = new File(File.createTempFile("test", "tmp").getAbsolutePath() + ".dir", "test/dir");

        RestStateStorage storage = new RestStateStorage(file, jsonizer);
        storage.storeFrom(client);

        assertThat(FileUtils.readFileToString(file)).isEqualTo("42");
    }

    @Test
    public void whenRecoveringCookiesFromFile() throws Exception {
        RestState state = mock(RestState.class);
        File file = File.createTempFile("test", "tmp");
        FileUtils.writeStringToFile(file, "42");

        when(jsonizer.from("42", RestState.class)).thenReturn(state);

        RestStateStorage storage = new RestStateStorage(file, jsonizer);
        storage.recoverTo(client);

        verify(client).setState(state);
    }

    @Test
    public void whenRecoveringCookiesWithNonExistingFile() throws Exception {
        File file = new File(File.createTempFile("test", "tmp").getAbsolutePath() + ".dir", "test/dir");

        RestStateStorage storage = new RestStateStorage(file, jsonizer);
        storage.recoverTo(client);
        verifyNoMoreInteractions(client);
    }

    @Test
    public void whenRecoveringCookiesFromNonCookieSerializedData() throws Exception {
        RestState state = mock(RestState.class);
        File file = File.createTempFile("test", "tmp");
        FileUtils.writeStringToFile(file, "42");

        when(jsonizer.from("42", RestState.class)).thenThrow(new RuntimeException());

        RestStateStorage storage = new RestStateStorage(file, jsonizer);
        storage.recoverTo(client);

        verifyNoMoreInteractions(client);
    }

}
