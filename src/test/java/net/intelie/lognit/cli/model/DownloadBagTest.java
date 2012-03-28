package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.Arrays;

import static net.intelie.lognit.cli.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static org.fest.assertions.Assertions.assertThat;

public class DownloadBagTest {
    public static final String TEST_JSON = "{remaining_docs:42, " +
            "items:[{id:'abc1'}, {id:'abc2'}]}";

    @Test
    public void whenDeserializing() {
        DownloadBag messages = new Gson().fromJson(TEST_JSON, DownloadBag.class);

        assertThat(messages.getRemainingDocs()).isEqualTo(42);
        assertThat(messages.getItems().size()).isEqualTo(2);
    }

    @Test
    public void whenSerializing() {
        DownloadBag messages = new DownloadBag(Arrays.asList(new Message("abc1"), new Message("abc2")), 42);
        JsonElement actual = jsonElement(messages);

        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonParse(TEST_JSON, DownloadBag.class);
        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }
}
