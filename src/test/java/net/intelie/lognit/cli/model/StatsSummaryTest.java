package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static net.intelie.lognit.cli.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static org.fest.assertions.Assertions.assertThat;

public class StatsSummaryTest {
    public static final String TEST_JSON = "{ " +
            " total_messages: 150," +
            " total_bytes: 450," +
            " total_docs: 42," +
            " nodes: ['AA', 'BB']," +
            " queries: ['AAA','BBB','CCC','DDD']," +
            " missing: 2, " +
            " message_rate: [3, 5, 6, 9, 1], " +
            " byte_rate: [1, 1, 2, 2], " +
            " per_nodes: [" +
            "    { " +
            "      node: 'AA'," +
            "      queries: ['AAA', 'BBB', 'CCC']," +
            "      message_rate: [1, 2, 3, 4]," +
            "      byte_rate: [1, 1, 1, 1]," +
            "      total_messages: 100, " +
            "      total_bytes: 300, " +
            "      total_docs: 28 " +
            "    }," +
            "    { " +
            "      node: 'BB'," +
            "      message_rate: [2, 3, 3, 5, 1]," +
            "      byte_rate: [0, 0, 1, 1]," +
            "      queries: ['DDD', 'BBB', 'CCC']," +
            "      total_messages: 50, " +
            "      total_bytes: 150, " +
            "      total_docs: 14 " +
            "    }]" +
            " }";

    @Test
    public void whenDeserializing() {
        StatsSummary message = new Gson().fromJson(
                TEST_JSON, StatsSummary.class);

        assertThat(message.getNodes()).containsOnly("AA", "BB");
        assertThat(message.getQueries()).containsOnly("AAA", "BBB", "CCC", "DDD");
        assertThat(message.getTotalMessages()).isEqualTo(150);
        assertThat(message.getTotalBytes()).isEqualTo(450);
        assertThat(message.getTotalDocs()).isEqualTo(42);
        assertThat(message.getMissing()).isEqualTo(2);
        assertThat(message.getMessageRate()).isEqualTo(Arrays.asList(3L, 5L, 6L, 9L, 1L));

        Iterator<Stats> it = message.getPerNodes().iterator();

        Stats st1 = it.next(), st2 = it.next();
        assertThat(st1.getNode()).isEqualTo("AA");
        assertThat(st1.getQueries()).containsOnly("AAA", "BBB", "CCC");
        assertThat(st1.getTotalMessages()).isEqualTo(100);
        assertThat(st1.getTotalBytes()).isEqualTo(300);
        assertThat(st1.getTotalDocs()).isEqualTo(28);
        assertThat(st1.getDocsRate()).isEqualTo(Arrays.asList(1L, 2L, 3L, 4L));

        assertThat(st2.getNode()).isEqualTo("BB");
        assertThat(st2.getQueries()).containsOnly("DDD", "BBB", "CCC");
        assertThat(st2.getTotalMessages()).isEqualTo(50);
        assertThat(st2.getTotalBytes()).isEqualTo(150);
        assertThat(st2.getTotalDocs()).isEqualTo(14);
        assertThat(st2.getDocsRate()).isEqualTo(Arrays.asList(2L, 3L, 3L, 5L, 1L));

    }

    @Test
    public void whenSerializing() {
        JsonElement actual = jsonElement(new StatsSummary(150, 450, 42,
                Arrays.asList("AA", "BB"),
                Arrays.asList("AAA", "BBB", "CCC", "DDD"),
                Arrays.asList(
                    new Stats("AA", 100, 300, 28, Arrays.asList("AAA", "BBB", "CCC"), Arrays.asList(1L, 2L, 3L, 4L), Arrays.asList(1L, 1L, 1L, 1L)),
                    new Stats("BB", 50, 150, 14, Arrays.asList("DDD", "BBB", "CCC"), Arrays.asList(2L, 3L, 3L, 5L, 1L), Arrays.asList(0L, 0L, 1L, 1L))),
                Arrays.asList(3L, 5L, 6L, 9L, 1L), Arrays.asList(1L, 1L, 2L, 2L), 2));

        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonParse(TEST_JSON, StatsSummary.class);
        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }
}
