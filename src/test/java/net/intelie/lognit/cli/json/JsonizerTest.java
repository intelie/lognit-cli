package net.intelie.lognit.cli.json;

import com.google.gson.Gson;
import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
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
}
