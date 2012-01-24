package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.Arrays;

import static net.intelie.lognit.cli.model.JsonHelpers.*;
import static org.fest.assertions.Assertions.assertThat;

public class MessageBagTest {

    public static final String TEST_JSON = "{success:true, " +
            "realtime:true, " +
            "message:'some message', " +
            "total_items:200, " +
            "total_nodes:42, " +
            "items:[{message:'abc1'}, {message:'abc2'}]}";

    @Test
    public void whenDeserializing() {
        MessageBag messages = new Gson().fromJson(TEST_JSON, MessageBag.class);

        assertThat(messages.isSuccess()).isTrue();
        assertThat(messages.isRealtime()).isTrue();
        assertThat(messages.isHistoric()).isFalse();
        assertThat(messages.getMessage()).isEqualTo("some message");
        assertThat(messages.getTotalNodes()).isEqualTo(42);
        assertThat(messages.getTotalItems()).isEqualTo(200);
        assertThat(messages.getItems().size()).isEqualTo(2);
    }

    @Test
    public void whenSerializing() {
        MessageBag messages = new MessageBag(Arrays.asList(new Message(null, null, "abc1"), new Message(null, null, "abc2")),
                "some message", true, true, 42, 200);
        JsonElement actual = jsonElement(messages);

        assertThat(actual).isEqualTo(jsonExpected(TEST_JSON));
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonPrepare(TEST_JSON, MessageBag.class);
        assertThat(actual).isEqualTo(jsonExpected(TEST_JSON));
    }

}
