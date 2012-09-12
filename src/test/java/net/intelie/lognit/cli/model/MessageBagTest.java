package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static net.intelie.lognit.cli.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static org.fest.assertions.Assertions.assertThat;

public class MessageBagTest {

    public static final String TEST_JSON = "{success:true, " +
            "realtime:true, " +
            "message:'some message', " +
            "total_items:200, " +
            "total_nodes:42, " +
            "node: abc," +
            "time: 42, " +
            "stats:{hours:[{key:1,freq:2}], last:[{key:3,freq:4}], fields:{host:[{key:'A', freq:5}]}}," +
            "items:[{id:'abc1'}, {id:'abc2'}]}";

    public static final SearchStats DEFAULT_STATS = new SearchStats(
            Arrays.asList(new FreqPoint<Long>(1L, 2)),
            Arrays.asList(new FreqPoint<Long>(3L, 4)),
            new HashMap<String, List<FreqPoint<String>>>() {{
                put("host", Arrays.asList(new FreqPoint<String>("A", 5)));
            }});

    @Test
    public void whenDeserializing() {
        MessageBag messages = new Gson().fromJson(TEST_JSON, MessageBag.class);

        assertThat(messages.isSuccess()).isTrue();
        assertThat(messages.isRealtime()).isTrue();
        assertThat(messages.isHistoric()).isFalse();
        assertThat(messages.getMessage()).isEqualTo("some message");
        assertThat(messages.getAggregated()).isNull();
        assertThat(messages.getTotalNodes()).isEqualTo(42);
        assertThat(messages.getTotalItems()).isEqualTo(200L);
        assertThat(messages.getItems().size()).isEqualTo(2);
        assertThat(messages.getNode()).isEqualTo("abc");
        assertThat(messages.getTime()).isEqualTo(42L);
        assertThat(messages.getStats()).isEqualTo(DEFAULT_STATS);
    }

    @Test
    public void whenSerializing() {
        MessageBag messages = new MessageBag(Arrays.asList(new Message("abc1"), new Message("abc2")), DEFAULT_STATS, null,
                "abc", 42L, "some message", true, true, 42, 200L);
        JsonElement actual = jsonElement(messages);

        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonParse(TEST_JSON, MessageBag.class);
        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

}
