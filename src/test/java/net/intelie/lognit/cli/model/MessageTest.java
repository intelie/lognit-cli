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
        Message message = new Gson().fromJson("{message:'abc', severity:'qwe', id:'123'}", Message.class);

        assertThat(message.getMessage()).isEqualTo("abc");
        assertThat(message.getId()).isEqualTo("123");
        assertThat(message.getSeverity()).isEqualTo("qwe");
    }

    @Test
    public void whenSerializing() {
        JsonElement actual = jsonElement(new Message("123", "qwe", "abc"));

        assertThat(actual).isEqualTo(jsonExpected("{message:'abc', id:'123', severity:'qwe'}"));
    }

    @Test
    public void whenComparing() {
        Message m1 = new Message("A", null, null);
        Message m2 = new Message("a", null, null);
        Message m3 = new Message("B", null, null);
        Message m4 = new Message(null, null, null);

        assertThat(m1.compareTo(m2)).isZero();

        assertThat(m1.compareTo(m3)).isPositive();
        assertThat(m3.compareTo(m1)).isNegative();

        assertThat(m1.compareTo(m4)).isNegative();
        assertThat(m4.compareTo(m1)).isPositive();
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonPrepare("{message:'42', id:'123', will_ignore:true}", Message.class);
        assertThat(actual).isEqualTo(jsonExpected("{message:'42', id:'123'}"));
    }

}
