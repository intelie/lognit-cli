package net.intelie.lognit.cli.input;

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
        assertThat(opts.isHelp()).isEqualTo(false);
        assertThat(opts.isUsage()).isEqualTo(true);
    }

    @Test
    public void canConstructWithNonDefaults() {
        UserOptions opts = new UserOptions("-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-i");
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
        assertThat(opts.isInfo()).isEqualTo(true);
        assertThat(opts.isHelp()).isEqualTo(true);
        assertThat(opts.isUsage()).isEqualTo(true);
    }

    @Test
    public void testUsageCombinations() {
        assertThat(new UserOptions("-s", "someserver").isUsage()).isEqualTo(false);
        assertThat(new UserOptions("-s", "someserver", "*").isUsage()).isEqualTo(false);
        assertThat(new UserOptions("-s", "someserver", "-?").isUsage()).isEqualTo(true);
        assertThat(new UserOptions("-i").isUsage()).isEqualTo(false);
        assertThat(new UserOptions("-u", "user", "-p", "pass").isUsage()).isEqualTo(true);
        assertThat(new UserOptions("").isUsage()).isEqualTo(true);
        assertThat(new UserOptions().isUsage()).isEqualTo(true);
    }

    @Test
    public void testInfoCombinations() {
        assertThat(new UserOptions("-i").isInfo()).isEqualTo(true);
        assertThat(new UserOptions("-s", "someserver").isInfo()).isEqualTo(true);
        assertThat(new UserOptions("-s", "someserver", "*").isInfo()).isEqualTo(false);
        assertThat(new UserOptions("-s", "someserver", "-?").isInfo()).isEqualTo(true);
        assertThat(new UserOptions("-u", "user", "-p", "pass").isInfo()).isEqualTo(false);
    }

    @Test
    public void testAskPassCombinations() {
        assertThat(new UserOptions("-u", "user").askPassword()).isEqualTo(true);
        assertThat(new UserOptions("-p", "pass").askPassword()).isEqualTo(true);
        assertThat(new UserOptions("-u", "user", "-p", "pass").askPassword()).isEqualTo(false);
    }


    @Test
    public void differentOrderShouldDoTheSame() {
        UserOptions opts1 = new UserOptions("-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-i");
        UserOptions opts2 = new UserOptions("-i", "-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?");
        assertThat(opts1).isEqualTo(opts2);
        assertThat(opts1.hashCode()).isEqualTo(opts2.hashCode());
    }

    @Test
    public void whenAreDifferent() {
        UserOptions opts1 = new UserOptions("-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-i");
        UserOptions opts2 = new UserOptions("-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?");
        UserOptions opts3 = new UserOptions("-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-i");
        UserOptions opts4 = new UserOptions("-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-?", "-i");
        UserOptions opts5 = new UserOptions("-s", "A", "-u", "B", "-p", "C", "D", "-n", "43", "-f", "-?", "-i");
        UserOptions opts6 = new UserOptions("-s", "A", "-u", "B", "-p", "C", "D", "-t", "42", "-f", "-?", "-i");
        UserOptions opts7 = new UserOptions("-s", "A", "-u", "B", "-p", "C", "-n", "43", "-t", "42", "-f", "-?", "-i");
        UserOptions opts8 = new UserOptions("-s", "A", "-u", "B", "D", "-n", "43", "-t", "42", "-f", "-?", "-i");
        UserOptions opts9 = new UserOptions("-s", "A", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-i");
        UserOptions opts10 = new UserOptions("-u", "B", "-p", "C", "D", "-n", "43", "-t", "42", "-f", "-?", "-i");

        assertThat(opts1).isNotEqualTo(opts2);
        assertThat(opts1).isNotEqualTo(opts3);
        assertThat(opts1).isNotEqualTo(opts4);
        assertThat(opts1).isNotEqualTo(opts5);
        assertThat(opts1).isNotEqualTo(opts6);
        assertThat(opts1).isNotEqualTo(opts7);
        assertThat(opts1).isNotEqualTo(opts8);
        assertThat(opts1).isNotEqualTo(opts9);
        assertThat(opts1).isNotEqualTo(opts10);

        assertThat(opts1.hashCode()).isNotEqualTo(opts2.hashCode());
        assertThat(opts1.hashCode()).isNotEqualTo(opts3.hashCode());
        assertThat(opts1.hashCode()).isNotEqualTo(opts4.hashCode());
        assertThat(opts1.hashCode()).isNotEqualTo(opts5.hashCode());
        assertThat(opts1.hashCode()).isNotEqualTo(opts6.hashCode());
        assertThat(opts1.hashCode()).isNotEqualTo(opts7.hashCode());
        assertThat(opts1.hashCode()).isNotEqualTo(opts8.hashCode());
        assertThat(opts1.hashCode()).isNotEqualTo(opts9.hashCode());
        assertThat(opts1.hashCode()).isNotEqualTo(opts10.hashCode());
    }
}
