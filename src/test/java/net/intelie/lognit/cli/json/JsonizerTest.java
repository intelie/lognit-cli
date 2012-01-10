package net.intelie.lognit.cli.json;

import com.google.gson.Gson;
import net.intelie.lognit.cli.model.Welcome;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class JsonizerTest {
    @Test
    public void testTo() throws Exception {
        Welcome welcome = new Welcome("abc");
        Jsonizer json = new Jsonizer(new Gson());
        assertThat(json.to(welcome)).isEqualTo(new Gson().toJson(welcome));
    }

    @Test
    public void testFrom() throws Exception {
        String welcome = "{message:'abc'}";
        Jsonizer json = new Jsonizer(new Gson());
        assertThat(json.from(welcome, Welcome.class).getMessage()).isEqualTo("abc");

    }
}
