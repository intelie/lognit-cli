package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.Arrays;

import static net.intelie.lognit.cli.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static org.fest.assertions.Assertions.assertThat;

public class TermsTest {
    public static final String TEST_JSON = "{terms:['AAA', 'BBB', 'CCC']}";

    @Test
    public void whenDeserializing() {
        Terms terms = new Gson().fromJson(TEST_JSON, Terms.class);

        assertThat(terms.getTerms()).isEqualTo(Arrays.asList("AAA", "BBB", "CCC"));
    }

    @Test
    public void whenSerializing() {
        Terms terms = new Terms(Arrays.asList("AAA", "BBB", "CCC"));
        JsonElement actual = jsonElement(terms);

        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonParse(TEST_JSON, Terms.class);
        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }
}
