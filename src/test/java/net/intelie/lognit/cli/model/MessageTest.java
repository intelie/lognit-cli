package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.intelie.lognit.cli.JsonHelpers.*;
import static org.fest.assertions.Assertions.assertThat;

public class MessageTest {

    public static final String TEST_JSON = "{message:'abc'," +
            "host:'A', " +
            "date:'B', " +
            "time:'C', " +
            "facility:'D', " +
            "severity:'E', " +
            "app:'F', " +
            "id:'123', " +
            "metadata:{'aaa': ['bbb', 'ccc']}}";

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
        Map<String, List<String>> metadata = message.getMetadata();
        assertThat(metadata.size()).isEqualTo(1);
        assertThat(metadata.get("aaa")).containsExactly("bbb", "ccc");
    }

    @Test
    public void whenSerializing() {
        Map<String, List<String>> metadata = new HashMap<String, List<String>>() {{
            put("aaa", Arrays.asList("bbb", "ccc"));
        }};

        JsonElement actual = jsonElement(new Message("123", "A", "B", "C", "D", "E", "F", "abc", metadata));

        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    @Test
    public void whenComparing() {
        Message m1 = new Message("A");
        Message m2 = new Message("a");
        Message m3 = new Message("B");
        Message m4 = new Message(null);

        assertThat(m1.compareTo(m2)).isZero();

        assertThat(m1.compareTo(m3)).isPositive();
        assertThat(m3.compareTo(m1)).isNegative();

        assertThat(m1.compareTo(m4)).isNegative();
        assertThat(m4.compareTo(m1)).isPositive();
    }

    @Test
    public void whenAreEqual() {
        Message m1 = new Message("A");
        Message m2 = new Message("A");

        assertThat(m1).isEqualTo(m1);
        assertThat(m1).isEqualTo(m2);
        assertThat(m1.hashCode()).isEqualTo(m1.hashCode());
        assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
    }

    @Test
    public void whenAreDifferent() {
        Message m1 = new Message("A");
        Message m2 = new Message("B");

        assertThat(m1).isNotEqualTo(m2);
        assertThat(m1).isNotEqualTo(new Object());
        assertThat(m1.hashCode()).isNotEqualTo(m2.hashCode());
        assertThat(m1.hashCode()).isNotEqualTo(new Object().hashCode());
    }


    @Test
    public void fullTest() {
        JsonElement actual = jsonParse(TEST_JSON, Message.class);
        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    @Test
    public void whenFormattingDateTime() {
        Message message = new Message(null, null, "20120213", "182653", null, null, null, null, null);

        assertThat(message.formattedDateTime()).isEqualTo("2012-02-13 18:26:53");
    }

    @Test
    public void whenFormattingDateTimeWrong() {
        Message message1 = new Message(null, null, "2012021", "182653", null, null, null, null, null);
        Message message2 = new Message(null, null, "20120213", "82653", null, null, null, null, null);
        Message message3 = new Message(null, null, "20121313", "182653", null, null, null, null, null);
        Message message4 = new Message(null, null, null, null, null, null, null, null, null);

        assertThat(message1.formattedDateTime()).isEqualTo("2012021182653");
        assertThat(message2.formattedDateTime()).isEqualTo("2012021382653");
        assertThat(message3.formattedDateTime()).isEqualTo("2012-13-13 18:26:53");
        assertThat(message4.formattedDateTime()).isEqualTo("nullnull");
    }


}
