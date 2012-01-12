package net.intelie.lognit.cli.state;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HttpClientStorageTest {
    @Test
    public void whenStoringFromClientVerifyIfTheArrayWasSaved() throws Exception {
        HttpClient client = mock(HttpClient.class, RETURNS_DEEP_STUBS);
        when(client.getState().getCookies()).thenReturn(newCookies());

        File file = File.createTempFile("test", "tmp");

        HttpClientStorage storage = new HttpClientStorage(file);
        storage.storeFrom(client);

        assertThat(FileUtils.readFileToByteArray(file))
                .isEqualTo(SerializationUtils.serialize(newCookies()));
    }

    @Test
    public void whenStoringWithException() throws Exception {
        HttpClient client = mock(HttpClient.class, RETURNS_DEEP_STUBS);
        when(client.getState().getCookies()).thenReturn(newCookies());

        File file = spy(File.createTempFile("test", "tmp"));
        when(file.getParentFile()).thenThrow(new RuntimeException());

        HttpClientStorage storage = new HttpClientStorage(file);
        storage.storeFrom(client);

        assertThat(FileUtils.readFileToByteArray(file)).isEmpty();
    }

    @Test
    public void whenStoringNonExistingFileCheckIfItWasCreated() throws Exception {
        HttpClient client = mock(HttpClient.class, RETURNS_DEEP_STUBS);
        when(client.getState().getCookies()).thenReturn(newCookies());

        File file = new File(File.createTempFile("test", "tmp").getAbsolutePath() + ".dir", "test/dir");

        HttpClientStorage storage = new HttpClientStorage(file);
        storage.storeFrom(client);

        assertThat(FileUtils.readFileToByteArray(file))
                .isEqualTo(SerializationUtils.serialize(newCookies()));
    }

    @Test
    public void whenRecoveringCookiesFromFile() throws Exception {
        File file = File.createTempFile("test", "tmp");
        FileUtils.writeByteArrayToFile(file, SerializationUtils.serialize(newCookies()));
        
        HttpClient client = mock(HttpClient.class, RETURNS_DEEP_STUBS);

        HttpClientStorage storage = new HttpClientStorage(file);
        storage.recoverTo(client);

        verify(client.getState()).addCookies(newCookies());
    }

    @Test
    public void whenRecoveringCookiesWithNonExistingFile() throws Exception {
        File file = new File(File.createTempFile("test", "tmp").getAbsolutePath() + ".dir", "test/dir");

        HttpClient client = mock(HttpClient.class, RETURNS_DEEP_STUBS);

        HttpClientStorage storage = new HttpClientStorage(file);
        storage.recoverTo(client);

        verifyZeroInteractions(client.getState());
    }

    @Test
    public void whenRecoveringCookiesFromNonCookieSerializedData() throws Exception {
        File file = File.createTempFile("test", "tmp");
        FileUtils.writeByteArrayToFile(file, SerializationUtils.serialize("blablabla"));

        HttpClient client = mock(HttpClient.class, RETURNS_DEEP_STUBS);

        HttpClientStorage storage = new HttpClientStorage(file);
        storage.recoverTo(client);

        verifyZeroInteractions(client.getState());
    }

    private Cookie[] newCookies() {
        return new Cookie[]{new Cookie("A", "B", "C"), new Cookie("D", "E", "F")};
    }
}
