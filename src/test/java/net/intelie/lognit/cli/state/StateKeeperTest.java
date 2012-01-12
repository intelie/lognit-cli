package net.intelie.lognit.cli.state;

import net.intelie.lognit.cli.http.RestClient;
import org.apache.commons.httpclient.HttpClient;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class StateKeeperTest {

    private RestStateStorage storage;
    private RestClient client;
    private StateKeeper keeper;

    @Before
    public void setUp() throws Exception {
        storage = mock(RestStateStorage.class);
        client = mock(RestClient.class);
        keeper = new StateKeeper(client, storage);
        verifyZeroInteractions(storage,  client);
    }

    @Test
    public void testBegin() throws Exception {
        keeper.begin();
        verify(storage).recoverTo(client);
    }

    @Test
    public void testEnd() throws Exception {
        keeper.end();
        verify(storage).storeFrom(client);
    }
}
