package net.intelie.lognit.cli;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;

import static org.fest.assertions.Assertions.assertThat;

public class UserOptionsTest {
    @Test
    public void canConstructWithDefaults() {
        UserOptions opts = new UserOptions();
        assertThat(opts.getServer()).isEqualTo(null);
        assertThat(opts.hasServer()).isEqualTo(false);
        assertThat(opts.getUser()).isEqualTo(null);
        assertThat(opts.getPassword()).isEqualTo(null);
        assertThat(opts.getQuery()).isEqualTo("");
        assertThat(opts.hasQuery()).isEqualTo(false);
        assertThat(opts.getLines()).isEqualTo(20);
        assertThat(opts.getTimeout()).isEqualTo(10);
        assertThat(opts.getTimeoutInMilliseconds()).isEqualTo(10000);
        assertThat(opts.isFollow()).isEqualTo(false);
        assertThat(opts.isAll()).isEqualTo(false);
        assertThat(opts.isPurge()).isEqualTo(false);
        assertThat(opts.isPause()).isEqualTo(false);
        assertThat(opts.isResume()).isEqualTo(false);
        assertThat(opts.isUnpurge()).isEqualTo(false);
        assertThat(opts.isCancelPurges()).isEqualTo(false);
        assertThat(opts.getFormat()).isEqualTo("colored");
        assertThat(opts.isComplete()).isEqualTo(false);
        assertThat(opts.isDownload()).isEqualTo(false);
        assertThat(opts.isUsage()).isEqualTo(false);
        assertThat(opts.isVerbose()).isEqualTo(false);
        assertThat(opts.isForceLogin()).isEqualTo(false);
    }

    @Test
    public void defaultForLinesIs20millionWhenPurge() {
        UserOptions opts = new UserOptions("--purge");
        assertThat(opts.getLines()).isEqualTo(20000000);
    }

    @Test
    public void canChangeLines() {
        UserOptions opts = new UserOptions("abc", "-n", "500");
        assertThat(opts.realtimeOnly()).isEqualTo(new UserOptions("abc", "-n", "0"));
    }

    @Test
    public void defaultForLinesIs100WhenDownload() {
        UserOptions opts = new UserOptions("--download");
        assertThat(opts.getLines()).isEqualTo(100);
    }


    @Test
    public void canConstructWithNonDefaults() {
        UserOptions opts = new UserOptions("--purge", "--unpurge", "--pause", "--resume", "-d", "--all", "--cancel-purges", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-i", "-o", "plain", "-c", "-v", "--force-login");
        assertThat(opts.getServer()).isEqualTo("A");
        assertThat(opts.hasServer()).isEqualTo(true);
        assertThat(opts.getUser()).isEqualTo("B");
        assertThat(opts.getPassword()).isEqualTo("C");
        assertThat(opts.getQuery()).isEqualTo("D");
        assertThat(opts.hasQuery()).isEqualTo(true);
        assertThat(opts.getLines()).isEqualTo(43);
        assertThat(opts.getTimeout()).isEqualTo(42);
        assertThat(opts.getTimeoutInMilliseconds()).isEqualTo(42000);
        assertThat(opts.isFollow()).isEqualTo(true);
        assertThat(opts.isAll()).isEqualTo(true);
        assertThat(opts.isPurge()).isEqualTo(true);
        assertThat(opts.isUnpurge()).isEqualTo(true);
        assertThat(opts.isPause()).isEqualTo(true);
        assertThat(opts.isResume()).isEqualTo(true);
        assertThat(opts.isCancelPurges()).isEqualTo(true);
        assertThat(opts.isInfo()).isEqualTo(true);
        assertThat(opts.getFormat()).isEqualTo("plain");
        assertThat(opts.isComplete()).isEqualTo(true);
        assertThat(opts.isDownload()).isEqualTo(true);
        assertThat(opts.isUsage()).isEqualTo(true);
        assertThat(opts.isVerbose()).isEqualTo(true);
        assertThat(opts.isForceLogin()).isEqualTo(true);
    }

    @Test
    public void testUsageCombinations() {
        assertThat(new UserOptions("-s", "someserver", "-?").isUsage()).isEqualTo(true);
    }

    @Test
    public void testInfoCombinations() {
        assertThat(new UserOptions("-i").isInfo()).isEqualTo(true);
    }

    @Test
    public void testAskPassCombinations() {
        assertThat(new UserOptions("-u", "user").askPassword()).isEqualTo(true);
        assertThat(new UserOptions("-p", "pass").askPassword()).isEqualTo(true);
        assertThat(new UserOptions("-u", "user", "-p", "pass").askPassword()).isEqualTo(false);
    }


    @Test
    public void differentOrderShouldDoTheSame() {
        UserOptions opts1 = new UserOptions("-s", "A", "--purge", "--force-login", "-d", "--all", "--cancel-purges", "--unpurge", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-i", "-o", "-c", "-v");
        UserOptions opts2 = new UserOptions("--unpurge", "--force-login", "--cancel-purges", "--all", "-i", "-d", "-s", "A", "--purge", "-v", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-o", "-c");
        assertThat(opts1).isEqualTo(opts2);
        assertThat(opts1.hashCode()).isEqualTo(opts2.hashCode());
    }

    @Test
    public void whenAreDifferent() {
        String[] original = {"--all", "--pause", "--resume", "--cancel-purges", "--unpurge", "--purge",
                "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-o", "plain", "-?",
                "-i", "-c", "-v", "-d", "--force-login"};
        UserOptions opts1 = new UserOptions(original);

        for (int i = 0; i < original.length; i++) {
            ArrayList<String> list = Lists.newArrayList(original);
            list.remove(i);
            UserOptions opts2 = new UserOptions(Iterables.toArray(list, String.class));
            assertThat(opts2).isNotEqualTo(opts1);
            assertThat(opts2.hashCode()).isNotEqualTo(opts1.hashCode());
        }
    }

}
