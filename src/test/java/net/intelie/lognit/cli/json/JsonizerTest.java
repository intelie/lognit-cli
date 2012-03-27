package net.intelie.lognit.cli.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
    public void testFromStream() throws Exception {
        ByteArrayInputStream stream = new ByteArrayInputStream("{message:'abc'}\n{message:'qwe'}{message:'asd'}".getBytes());
        Iterator<Welcome> iterator = json.from(stream, Welcome.class);
        
        assertThat(iterator.next().getMessage()).isEqualTo("abc");
        assertThat(iterator.next().getMessage()).isEqualTo("qwe");
        assertThat(iterator.next().getMessage()).isEqualTo("asd");
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
