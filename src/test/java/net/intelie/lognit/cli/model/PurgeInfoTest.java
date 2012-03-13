package net.intelie.lognit.cli.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import static net.intelie.lognit.cli.JsonHelpers.jsonElement;
import static net.intelie.lognit.cli.JsonHelpers.jsonParse;
import static org.fest.assertions.Assertions.assertThat;

public class PurgeInfoTest {
    public static final String TEST_JSON = "{" +
            "status:'CANCELLED', " +
            "message:'abc', " +
            "failed:1, " +
            "purged:2, " +
            "expected:3 }";

    @Test
    public void whenDeserializing() {
        PurgeInfo messages = new Gson().fromJson(TEST_JSON, PurgeInfo.class);

        assertThat(messages.getStatus()).isEqualTo(PurgeInfo.Status.CANCELLED);
        assertThat(messages.getMessage()).isEqualTo("abc");
        assertThat(messages.getFailed()).isEqualTo(1);
        assertThat(messages.getPurged()).isEqualTo(2);
        assertThat(messages.getExpected()).isEqualTo(3);
    }

    @Test
    public void whenSerializing() {
        PurgeInfo messages = new PurgeInfo(PurgeInfo.Status.CANCELLED, "abc", 1, 2, 3);
        JsonElement actual = jsonElement(messages);

        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    @Test
    public void fullTest() {
        JsonElement actual = jsonParse(TEST_JSON, PurgeInfo.class);
        assertThat(actual).isEqualTo(jsonParse(TEST_JSON));
    }

    @Test
    public void statusFinishedTest() {
        assertThat(PurgeInfo.Status.CANCELLED.isFinished()).isEqualTo(true);
        assertThat(PurgeInfo.Status.COMPLETED.isFinished()).isEqualTo(true);
        assertThat(PurgeInfo.Status.FAILURE.isFinished()).isEqualTo(true);
        assertThat(PurgeInfo.Status.WAITING.isFinished()).isEqualTo(false);
        assertThat(PurgeInfo.Status.RUNNING.isFinished()).isEqualTo(false);

    }


}
