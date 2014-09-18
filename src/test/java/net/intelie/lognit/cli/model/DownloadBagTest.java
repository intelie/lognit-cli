package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.Arrays;

import static net.intelie.lognit.cli.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static org.fest.assertions.Assertions.assertThat;

public class DownloadBagTest {
    public static final String TEST_JSON = "{current_hit:42, " +
            "total_hits:100, " +
            "items:[{id:'abc1'}, {id:'abc2'}], " +
            "aggregated:[{id:'abc1'}, {id:'abc2'}, {id:'abc3'}]}";

    @Test
    public void whenDeserializing() {
        DownloadBag messages = new Gson().fromJson(TEST_JSON, DownloadBag.class);

        assertThat(messages.getTotalHits()).isEqualTo(100);
        assertThat(messages.getCurrentHit()).isEqualTo(42);
        assertThat(messages.getItems().size()).isEqualTo(2);
        assertThat(messages.getAggregated().size()).isEqualTo(3);
    }

    @Test
    public void whenSerializing() {
        DownloadBag messages = new DownloadBag(
                Arrays.asList(new Message("abc1"), new Message("abc2")),
                new Aggregated(ai("abc1"), ai("abc2"), ai("abc3")),
                42, 100);
        JsonElement actual = jsonElement(messages);

        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    private AggregatedItem ai(String id) {
        AggregatedItem item = new AggregatedItem();
        item.put("id", id);
        return item;
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonParse(TEST_JSON, DownloadBag.class);
        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }
}
