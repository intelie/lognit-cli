package net.intelie.lognit.cli.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class SearchStatsTest {
    @Test
    public void canGetProperties() throws Exception {
        List<FreqPoint<Long>> hours = Arrays.asList(new FreqPoint<Long>(1L, 2));
        List<FreqPoint<Long>> last = Arrays.asList(new FreqPoint<Long>(3L, 4));
        Map<String, List<FreqPoint<String>>> fields = map("abc", 1L);

        SearchStats stats = new SearchStats(hours, last, fields);
        assertThat((Object) stats.hours()).isEqualTo(hours);
        assertThat((Object) stats.last()).isEqualTo(last);
        assertThat((Object) stats.fields()).isEqualTo(fields);

    }

    @Test
    public void canGetEmptyProperties() throws Exception {
        SearchStats stats = new SearchStats();
        assertThat(stats.hours()).isEmpty();
        assertThat(stats.last()).isEmpty();
        assertThat(stats.fields()).isEmpty();

    }

    @Test
    public void whenAreEqual() throws Exception {
        SearchStats s1 = new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 2)),
                Arrays.asList(new FreqPoint<Long>(3L, 4)),
                map("abc", 1L));
        SearchStats s2 = new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 2)),
                Arrays.asList(new FreqPoint<Long>(3L, 4)),
                map("abc", 1L));

        assertThat(s1).isEqualTo(s1);
        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s1.hashCode());
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
    }

    @Test
    public void whenMerging() throws Exception {
        SearchStats A = new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 2)),
                Arrays.asList(new FreqPoint<Long>(3L, 4)),
                new HashMap<String, List<FreqPoint<String>>>() {{
                    put("host", Arrays.asList(new FreqPoint<String>("A", 5)));
                }});

        SearchStats B = new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 2)),
                Arrays.asList(new FreqPoint<Long>(4L, 5)),
                new HashMap<String, List<FreqPoint<String>>>() {{
                    put("host", Arrays.asList(new FreqPoint<String>("B", 8), new FreqPoint<String>("A", 2)));
                    put("what", Arrays.asList(new FreqPoint<String>("C", 2)));
                }});

        SearchStats merged = new SearchStats();
        merged.merge(A);
        merged.merge(null);
        merged.merge(B);

        assertThat(merged).isEqualTo(new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 4)),
                Arrays.asList(new FreqPoint<Long>(3L, 4), new FreqPoint<Long>(4L, 5)),
                new HashMap<String, List<FreqPoint<String>>>() {{
                    put("host", Arrays.asList(new FreqPoint<String>("B", 8), new FreqPoint<String>("A", 7)));
                    put("what", Arrays.asList(new FreqPoint<String>("C", 2)));

                }}));
    }

    @Test
    public void whenAreDifferent() throws Exception {
        SearchStats s1 = new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 2)),
                Arrays.asList(new FreqPoint<Long>(3L, 4)),
                map("abc", 1L));
        SearchStats s2 = new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 3)),
                Arrays.asList(new FreqPoint<Long>(3L, 4)),
                map("abc", 1L));
        SearchStats s3 = new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 2)),
                Arrays.asList(new FreqPoint<Long>(2L, 4)),
                map("abc", 1L));
        SearchStats s4 = new SearchStats(
                Arrays.asList(new FreqPoint<Long>(1L, 2)),
                Arrays.asList(new FreqPoint<Long>(3L, 4)),
                map("adbc", 1L));

        assertThat(s1).isNotEqualTo(new Object());
        assertThat(s1).isNotEqualTo(s2);
        assertThat(s1).isNotEqualTo(s3);
        assertThat(s1).isNotEqualTo(s4);
        assertThat(s1.hashCode()).isNotEqualTo(new Object().hashCode());
        assertThat(s1.hashCode()).isNotEqualTo(s2.hashCode());
        assertThat(s1.hashCode()).isNotEqualTo(s3.hashCode());
        assertThat(s1.hashCode()).isNotEqualTo(s4.hashCode());
    }

    private Map<String, List<FreqPoint<String>>> map(String host, long freq) {
        Map<String, List<FreqPoint<String>>> map = new HashMap<String, List<FreqPoint<String>>>();
        map.put("host", Arrays.asList(new FreqPoint<String>(host, freq)));
        return map;
    }
}
