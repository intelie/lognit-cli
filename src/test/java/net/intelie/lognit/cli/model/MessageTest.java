package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import static net.intelie.lognit.cli.model.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.model.JsonHelpers.jsonExpected;
import static net.intelie.lognit.cli.model.JsonHelpers.jsonPrepare;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class MessageTest {
    @Test
    public void whenDeserializing() {
        Message message = new Gson().fromJson("{message:'abc'}", Message.class);

        assertThat(message.getMessage()).isEqualTo("abc");
    }

    @Test
    public void whenSerializing() {
        JsonElement actual = jsonElement(new Message("abc"));

        assertThat(actual).isEqualTo(jsonExpected("{message:'abc'}"));
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonPrepare("{message:'42', will_ignore:true}", Message.class);
        assertThat(actual).isEqualTo(jsonExpected("{message:'42'}"));
    }

}
