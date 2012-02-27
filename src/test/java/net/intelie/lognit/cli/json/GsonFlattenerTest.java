package net.intelie.lognit.cli.json;

import com.google.gson.JsonElement;
import org.junit.Before;
import org.junit.Test;

import static net.intelie.lognit.cli.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GsonFlattenerTest {

    private GsonFlattener flattener;

    @Before
    public void setUp() throws Exception {
        flattener = new GsonFlattener();
    }

    @Test
    public void whenFlatteningAlreadyFlattenedJson() {
        JsonElement element = jsonParse("{abc:'qwe'}");
        assertThat(flattener.flatten(element)).isEqualTo(jsonParse("{abc:'qwe'}"));
    }

    @Test
    public void whenFlatteningNull() {
        JsonElement element = jsonParse("{abc:null}");
        assertThat(flattener.flatten(element)).isEqualTo(jsonParse("{abc:null}"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenFlatteningUnknown() {
        JsonElement element = mock(JsonElement.class);
        flattener.flatten(element);
    }

    @Test
    public void whenFlatteningNonFlattenedObject() {
        JsonElement element = jsonParse("{abc:{abd: 'qwe'}}");
        assertThat(flattener.flatten(element)).isEqualTo(jsonParse("{abd:'qwe'}"));
    }

    @Test
    public void whenFlatteningObjectWithInnerArray() {
        JsonElement element = jsonParse("{abc:[123, 'qwe']}");
        assertThat(flattener.flatten(element)).isEqualTo(jsonParse("{abc:123}"));
    }


    @Test
    public void whenFlatteningOuterArray() {
        JsonElement element = jsonParse("[123, 'qwe']");
        assertThat(flattener.flatten(element)).isEqualTo(jsonParse("{default:123}"));
    }

}
