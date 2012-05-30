package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.intelie.lognit.cli.AggregatedItemHelper;
import org.junit.Test;

import static net.intelie.lognit.cli.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static org.fest.assertions.Assertions.assertThat;

public class AggregatedTest {
    public static final String TEST_JSON = "[" +
            "{timestamp:123, count:1, first_abc:'abc'}," +
            "{timestamp:123, count:42000000000001, first_abc:'qwe'}]";

    @Test
    public void whenDeserializing() {
        Aggregated agg = new Gson().fromJson(TEST_JSON, Aggregated.class);

        assertThat(agg).containsExactly(
                AggregatedItemHelper.map("timestamp", 123.0, "count", 1.0, "first_abc", "abc"),
                AggregatedItemHelper.map("timestamp", 123.0, "count", 42000000000001.0, "first_abc", "qwe"));
    }

    @Test
    public void whenSerializing() {
        Aggregated agg = new Aggregated();
        agg.add(AggregatedItemHelper.map("timestamp", 123.0, "count", 1.0, "first_abc", "abc"));
        agg.add(AggregatedItemHelper.map("timestamp", 123.0, "count", 42000000000001.0, "first_abc", "qwe"));

        JsonElement actual = jsonElement(agg);

        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonParse(TEST_JSON, Aggregated.class);
        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

}
