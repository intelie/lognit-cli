package net.intelie.lognit.cli.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.intelie.lognit.cli.model.AggregatedItem;
import net.intelie.lognit.cli.model.DownloadBag;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;

import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static org.fest.assertions.Assertions.assertThat;

public class JsonizerTest {

    private Jsonizer json;

    @Before
    public void setUp() throws Exception {
        json = new Jsonizer();
    }

    @Test
    public void testTo() throws Exception {
        Welcome welcome = new Welcome("abc");
        assertThat(json.to(welcome)).isEqualTo(new Gson().toJson(welcome));
    }

    @Test
    public void testToFlat() throws Exception {
        HashMap<String, List<String>> metadata = new HashMap<String, List<String>>();
        metadata.put("abc", Arrays.asList("123", "234"));

        Message message = new Message("a", null, null, null, null, null, null, null, metadata);
        assertThat(jsonParse(json.toFlat(message))).isEqualTo(jsonParse("{id:'a', abc:'123'}"));
    }

    @Test
    public void testFrom() throws Exception {
        String welcome = "{message:'abc'}";
        assertThat(json.from(welcome, Welcome.class).getMessage()).isEqualTo("abc");

    }

    @Test
    public void testFromMapTypeUsingNumbers() throws Exception {
        AggregatedItem map = json.from("{abc:123}", AggregatedItem.class);
        assertThat(map.get("abc")).isEqualTo(new BigDecimal(123));

    }

    @Test
    public void testFromMapTypeUsingDoubles() throws Exception {
        AggregatedItem map = json.from("{abc:123.456}", AggregatedItem.class);
        assertThat(map.get("abc")).isEqualTo(new BigDecimal("123.456"));

    }

    @Test
    public void testFromStream() throws Exception {
        ByteArrayInputStream stream = new ByteArrayInputStream("{message:'abc'}\n{message:'qwe'}{message:'asd'}".getBytes());
        Iterator<Welcome> iterator = json.from(stream, Welcome.class);

        assertThat(iterator.next().getMessage()).isEqualTo("abc");
        assertThat(iterator.next().getMessage()).isEqualTo("qwe");
        assertThat(iterator.next().getMessage()).isEqualTo("asd");
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void testFromEmptyStream() throws Exception {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        Iterator<Welcome> iterator = json.from(stream, Welcome.class);

        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void testFromComplexStream() throws Exception {
        ByteArrayInputStream stream = new ByteArrayInputStream("{\"items\":[{\"id\":\"13304222ec41137d877b91be4db00000/192.0.0.1\",\"message\":\"A #pri\\u003d0\",\"facility\":\"KERN\",\"host\":\"192.0.0.1\",\"severity\":\"EMERGENCY\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191553\",\"metadata\":{\"pri\":[\"0\"]}},{\"id\":\"13304222ec42137d877bd5bb3b050000/localhost\",\"message\":\"B #pri\\u003d1\",\"facility\":\"KERN\",\"host\":\"localhost\",\"severity\":\"ALERT\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191554\",\"metadata\":{\"pri\":[\"1\"]}},{\"id\":\"13304222ec43137d877bf2b7e7560000/test3\",\"message\":\"C #pri\\u003d2\",\"facility\":\"KERN\",\"host\":\"test3\",\"severity\":\"CRITICAL\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191555\",\"metadata\":{\"pri\":[\"2\"]}}],\"remaining_docs\":10}{\"items\":[{\"id\":\"13304222ec44137d877c17b601d40000/test2\",\"message\":\"D #pri\\u003d3\",\"facility\":\"KERN\",\"host\":\"test2\",\"severity\":\"ERROR\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191556\",\"metadata\":{\"pri\":[\"3\"]}},{\"id\":\"13304222ec45137d877c7733828c0000/test3\",\"message\":\"E #pri\\u003d4\",\"facility\":\"KERN\",\"host\":\"test3\",\"severity\":\"WARNING\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191557\",\"metadata\":{\"pri\":[\"4\"]}},{\"id\":\"13304222ec45137d877c913c4baa0000/test4\",\"message\":\"f #pri\\u003d5\",\"facility\":\"KERN\",\"host\":\"test4\",\"severity\":\"NOTICE\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191557\",\"metadata\":{\"pri\":[\"5\"]}}],\"remaining_docs\":7}{\"items\":[{\"id\":\"13304222ec46137d877ca0be16ea0000/test5\",\"message\":\"g #pri\\u003d6\",\"facility\":\"KERN\",\"host\":\"test5\",\"severity\":\"INFO\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191558\",\"metadata\":{\"pri\":[\"6\"]}},{\"id\":\"13304222ec46137d877cb9b7102d0000/test6\",\"message\":\"h #pri\\u003d7\",\"facility\":\"KERN\",\"host\":\"test6\",\"severity\":\"DEBUG\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191558\",\"metadata\":{\"pri\":[\"7\"]}},{\"id\":\"13304222ec47137d877cd939398b0000/test7\",\"message\":\"i #pri\\u003d8\",\"facility\":\"USER\",\"host\":\"test7\",\"severity\":\"EMERGENCY\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191559\",\"metadata\":{\"pri\":[\"8\"]}}],\"remaining_docs\":4}{\"items\":[{\"id\":\"13304222ec47137d877cf0b3025a0000/test8\",\"message\":\"j #pri\\u003d9\",\"facility\":\"USER\",\"host\":\"test8\",\"severity\":\"ALERT\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191559\",\"metadata\":{\"pri\":[\"9\"]}},{\"id\":\"13304222ec70137d877d14b7aa010000/192.0.0.1\",\"message\":\"k #pri\\u003d10\",\"facility\":\"USER\",\"host\":\"192.0.0.1\",\"severity\":\"CRITICAL\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191600\",\"metadata\":{\"pri\":[\"10\"]}},{\"id\":\"13304222ec70137d877d2e37e8940000/localhost\",\"message\":\"l #pri\\u003d11\",\"facility\":\"USER\",\"host\":\"localhost\",\"severity\":\"ERROR\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191600\",\"metadata\":{\"pri\":[\"11\"]}}],\"remaining_docs\":1}{\"items\":[{\"id\":\"13304222ec70137d877d47b13a890000/test3\",\"message\":\"m #pri\\u003d12\",\"facility\":\"USER\",\"host\":\"test3\",\"severity\":\"WARNING\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191600\",\"metadata\":{\"pri\":[\"12\"]}},{\"id\":\"13304222ec71137d877d6234e2000000/test2\",\"message\":\"n #pri\\u003d13\",\"facility\":\"USER\",\"host\":\"test2\",\"severity\":\"NOTICE\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191601\",\"metadata\":{\"pri\":[\"13\"]}},{\"id\":\"13304222ec71137d877d7db0877f0000/test3\",\"message\":\"o #pri\\u003d14\",\"facility\":\"USER\",\"host\":\"test3\",\"severity\":\"INFO\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191601\",\"metadata\":{\"pri\":[\"14\"]}}],\"remaining_docs\":0}{\"items\":[{\"id\":\"13304222ec72137d877d953b2d0f0000/test4\",\"message\":\"p #pri\\u003d15\",\"facility\":\"USER\",\"host\":\"test4\",\"severity\":\"DEBUG\",\"app\":\"load-ap2p\",\"date\":\"20120610\",\"time\":\"191602\",\"metadata\":{\"pri\":[\"15\"]}}],\"remaining_docs\":0}".getBytes());
        Iterator<DownloadBag> iterator = json.from(stream, DownloadBag.class);

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().getItems().size()).isEqualTo(3);

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().getItems().size()).isEqualTo(3);

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().getItems().size()).isEqualTo(3);

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().getItems().size()).isEqualTo(3);

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().getItems().size()).isEqualTo(3);

        assertThat(iterator.hasNext()).isTrue();
        assertThat(iterator.next().getItems().size()).isEqualTo(1);

        assertThat(iterator.hasNext()).isFalse();
    }

    @Test(expected = JsonSyntaxException.class)
    public void testFromStreamMalformed() throws Exception {
        ByteArrayInputStream stream = new ByteArrayInputStream("{message:'abc'}\n{messa".getBytes());
        Iterator<Welcome> iterator = json.from(stream, Welcome.class);

        assertThat(iterator.next().getMessage()).isEqualTo("abc");
        iterator.next();
    }
}
