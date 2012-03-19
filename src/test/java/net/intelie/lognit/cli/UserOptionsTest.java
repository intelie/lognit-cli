package net.intelie.lognit.cli;

import org.junit.Test;

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
        assertThat(opts.isUnpurge()).isEqualTo(false);
        assertThat(opts.isCancelPurges()).isEqualTo(false);
        assertThat(opts.getFormat()).isEqualTo("colored");
        assertThat(opts.isComplete()).isEqualTo(false);
        assertThat(opts.isUsage()).isEqualTo(false);
        assertThat(opts.isVerbose()).isEqualTo(false);
    }

    @Test
    public void defaultForLinesIs20millionWhenPurge() {
        UserOptions opts = new UserOptions("--purge");
        assertThat(opts.getLines()).isEqualTo(20000000);
    }


    @Test
    public void canConstructWithNonDefaults() {
        UserOptions opts = new UserOptions("--purge", "--unpurge", "--all", "--cancel-purges", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-i", "-b", "plain", "-c", "-v");
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
        assertThat(opts.isCancelPurges()).isEqualTo(true);
        assertThat(opts.isInfo()).isEqualTo(true);
        assertThat(opts.getFormat()).isEqualTo("plain");
        assertThat(opts.isComplete()).isEqualTo(true);
        assertThat(opts.isUsage()).isEqualTo(true);
        assertThat(opts.isVerbose()).isEqualTo(true);
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
        UserOptions opts1 = new UserOptions("-s", "--purge", "--all", "--cancel-purges", "--unpurge", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-i", "-b", "-c", "-v");
        UserOptions opts2 = new UserOptions("--unpurge", "--cancel-purges", "--all", "-i", "-s", "--purge", "A", "-v", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-b", "-c");
        assertThat(opts1).isEqualTo(opts2);
        assertThat(opts1.hashCode()).isEqualTo(opts2.hashCode());
    }

    @Test
    public void whenAreDifferent() {
        UserOptions opts1 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts2 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-c", "-v");
        UserOptions opts3 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-i", "-c", "-v");
        UserOptions opts4 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts5 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-f", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts6 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts7 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts8 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts9 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts10 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts11 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-i", "-c", "-v");
        UserOptions opts12 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-v");
        UserOptions opts13 = new UserOptions("--all", "--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c");
        UserOptions opts14 = new UserOptions("--all", "--cancel-purges", "--unpurge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts15 = new UserOptions("--all", "--cancel-purges", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts16 = new UserOptions("--all", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c", "-v");
        UserOptions opts17 = new UserOptions("--cancel-purges", "--unpurge", "--purge", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-b", "plain", "-?", "-i", "-c", "-v");

        assertNotEqual(opts1,
                opts2, opts3, opts4, opts5, opts6, opts7, opts8,
                opts9, opts10, opts11, opts12, opts13, opts14,
                opts15, opts16, opts17);

    }

    private void assertNotEqual(UserOptions opt1, Object... otherOpts) {
        for (Object opt2 : otherOpts) {
            assertThat(opt1.hashCode()).isNotEqualTo(opt2.hashCode());
        }
    }
}
