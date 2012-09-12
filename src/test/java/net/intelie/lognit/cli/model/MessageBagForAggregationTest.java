package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import static net.intelie.lognit.cli.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static org.fest.assertions.Assertions.assertThat;

public class MessageBagForAggregationTest {

    public static final String TEST_JSON = "{" +
            "success:true, " +
            "realtime:true, " +
            "aggregated:[" +
            "{timestamp:123, count:1, first_abc:'abc'}," +
            "{timestamp:123, count:42000000000001, first_abc:'qwe'}], " +
            "node: abc," +
            "time: 42} ";

    @Test
    public void whenDeserializing() {
        MessageBag messages = new Gson().fromJson(TEST_JSON, MessageBag.class);

        assertThat(messages.isSuccess()).isTrue();
        assertThat(messages.isRealtime()).isTrue();
        assertThat(messages.isHistoric()).isFalse();
        assertThat(messages.getAggregated()).containsExactly(
                map("timestamp", 123.0, "count", 1.0, "first_abc", "abc"),
                map("timestamp", 123.0, "count", 42000000000001.0, "first_abc", "qwe"));
        assertThat(messages.getMessage()).isNull();
        assertThat(messages.getTotalNodes()).isNull();
        assertThat(messages.getTotalItems()).isNull();
        assertThat(messages.getItems()).isNull();
        assertThat(messages.getNode()).isEqualTo("abc");
        assertThat(messages.getTime()).isEqualTo(42L);
    }

    @Test
    public void whenSerializing() {
        Aggregated agg = new Aggregated();
        agg.add(map("timestamp", 123.0, "count", 1.0, "first_abc", "abc"));
        agg.add(map("timestamp", 123.0, "count", 42000000000001.0, "first_abc", "qwe"));
        MessageBag messages = new MessageBag(null, null, agg,
                "abc", 42L, null, true, true, null, null);
        JsonElement actual = jsonElement(messages);

        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonParse(TEST_JSON, MessageBag.class);
        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }


    private AggregatedItem map(Object... values) {
        AggregatedItem map = new AggregatedItem();
        for (int i = 0; i + 1 < values.length; i += 2) {
            map.put((String) values[i], values[i + 1]);
        }
        return map;
    }
}
