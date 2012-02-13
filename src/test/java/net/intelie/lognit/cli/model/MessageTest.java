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

    public static final String TEST_JSON = "{message:'abc'," +
            "host:'A', " +
            "date:'B', " +
            "time:'C', " +
            "facility:'D', " +
            "severity:'E', " +
            "app:'F', " +
            "id:'123'}";

    @Test
    public void whenDeserializing() {
        Message message = new Gson().fromJson(
                TEST_JSON, Message.class);

        assertThat(message.getId()).isEqualTo("123");
        assertThat(message.getHost()).isEqualTo("A");
        assertThat(message.getDate()).isEqualTo("B");
        assertThat(message.getTime()).isEqualTo("C");
        assertThat(message.getFacility()).isEqualTo("D");
        assertThat(message.getSeverity()).isEqualTo("E");
        assertThat(message.getApp()).isEqualTo("F");
        assertThat(message.getMessage()).isEqualTo("abc");
    }

    @Test
    public void whenSerializing() {
        JsonElement actual = jsonElement(new Message("123", "A", "B", "C", "D", "E", "F", "abc"));

        assertThat(actual).isEqualTo(jsonExpected(TEST_JSON));
    }

    @Test
    public void whenComparing() {
        Message m1 = new Message("A", null, null, null, null, null, null, null);
        Message m2 = new Message("a", null, null, null, null, null, null, null);
        Message m3 = new Message("B", null, null, null, null, null, null, null);
        Message m4 = new Message(null, null, null, null, null, null, null, null);

        assertThat(m1.compareTo(m2)).isZero();

        assertThat(m1.compareTo(m3)).isPositive();
        assertThat(m3.compareTo(m1)).isNegative();

        assertThat(m1.compareTo(m4)).isNegative();
        assertThat(m4.compareTo(m1)).isPositive();
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonPrepare(TEST_JSON, Message.class);
        assertThat(actual).isEqualTo(jsonExpected(TEST_JSON));
    }

    @Test
    public void whenFormattingDateTime() {
        Message message = new Message(null, null, "20120213", "182653", null, null, null, null);

        assertThat(message.formattedDateTime()).isEqualTo("Feb 13 18:26:53");
    }

    @Test
    public void whenFormattingDateTimeWrong() {
        Message message1 = new Message(null, null, "2012021", "182653", null, null, null, null);
        Message message2 = new Message(null, null, "20120213", "82653", null, null, null, null);
        Message message3 = new Message(null, null, "20121313", "182653", null, null, null, null);

        assertThat(message1.formattedDateTime()).isEqualTo("2012021182653");
        assertThat(message2.formattedDateTime()).isEqualTo("2012021382653");
        assertThat(message3.formattedDateTime()).isEqualTo("?13? 13 18:26:53");
    }


}
