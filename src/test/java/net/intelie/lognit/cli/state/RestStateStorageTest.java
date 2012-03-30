package net.intelie.lognit.cli.state;

import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestState;
import net.intelie.lognit.cli.json.Jsonizer;
import org.apache.commons.io.FileUtils;
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

        try {
            RestStateStorage storage = new RestStateStorage(file, jsonizer);
            storage.storeFrom(client);

            assertThat(FileUtils.readFileToString(file)).isEqualTo("42");
        } finally {
            file.delete();
        }
    }

    @Test
    public void whenStoringWithException() throws Exception {
        when(jsonizer.to(client.getState())).thenReturn("42");

        File file = spy(File.createTempFile("test", "tmp"));
        try {
            when(file.getParentFile()).thenThrow(new RuntimeException());

            RestStateStorage storage = new RestStateStorage(file, jsonizer);
            storage.storeFrom(client);

            assertThat(FileUtils.readFileToString(file)).isEmpty();
        } finally {
            file.delete();
        }
    }

    @Test
    public void whenStoringNonExistingFileCheckIfItWasCreated() throws Exception {
        when(jsonizer.to(client.getState())).thenReturn("42");

        File tempFile = File.createTempFile("test", "tmp");
        String tempDir = tempFile.getAbsolutePath() + ".dir";
        try {
            File file = new File(tempDir, "test/dir");

            RestStateStorage storage = new RestStateStorage(file, jsonizer);
            storage.storeFrom(client);

            assertThat(FileUtils.readFileToString(file)).isEqualTo("42");
        } finally {
            FileUtils.deleteDirectory(new File(tempDir));
            tempFile.delete();
        }
    }

    @Test
    public void whenRecoveringCookiesFromFile() throws Exception {
        RestState state = mock(RestState.class);
        File file = File.createTempFile("test", "tmp");
        try {
            FileUtils.writeStringToFile(file, "42");

            when(jsonizer.from("42", RestState.class)).thenReturn(state);

            RestStateStorage storage = new RestStateStorage(file, jsonizer);
            storage.recoverTo(client);

            verify(client).setState(state);
        } finally {
            file.delete();
        }
    }

    @Test
    public void whenRecoveringCookiesWithNonExistingFile() throws Exception {
        File tempFile = File.createTempFile("test", "tmp");
        String tempDir = tempFile.getAbsolutePath() + ".dir";
        try {
            File file = new File(tempDir, "test/dir");

            RestStateStorage storage = new RestStateStorage(file, jsonizer);
            storage.recoverTo(client);
            verifyNoMoreInteractions(client);
        } finally {
            tempFile.delete();
            FileUtils.deleteDirectory(new File(tempDir));
        }
    }

    @Test
    public void whenRecoveringCookiesFromNonCookieSerializedData() throws Exception {
        RestState state = mock(RestState.class);
        File file = File.createTempFile("test", "tmp");
        try {
            FileUtils.writeStringToFile(file, "42");

            when(jsonizer.from("42", RestState.class)).thenThrow(new RuntimeException());

            RestStateStorage storage = new RestStateStorage(file, jsonizer);
            storage.recoverTo(client);

            verifyNoMoreInteractions(client);
        } finally {
            file.delete();
        }
    }

}
