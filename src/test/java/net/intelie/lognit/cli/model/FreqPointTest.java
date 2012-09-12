package net.intelie.lognit.cli.model;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FreqPointTest {
    @Test
    public void canGetProperties() throws Exception {
        FreqPoint point = new FreqPoint<Long>(123L, 456);
        assertThat(point.key()).isEqualTo(123L);
        assertThat(point.freq()).isEqualTo(456);
    }

    @Test
    public void whenAreEqual() throws Exception {
        FreqPoint p1 = new FreqPoint<Long>(123L, 456);
        FreqPoint p2 = new FreqPoint<Long>(123L, 456);

        assertThat(p1).isEqualTo(p1);
        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p1.hashCode());
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    public void whenAreDifferent() throws Exception {
        FreqPoint p1 = new FreqPoint<Long>(123L, 456);
        FreqPoint p2 = new FreqPoint<Long>(123L, 457);
        FreqPoint p3 = new FreqPoint<Long>(124L, 456);

        assertThat(p1).isNotEqualTo(new Object());
        assertThat(p1).isNotEqualTo(p2);
        assertThat(p1).isNotEqualTo(p3);
        assertThat(p1.hashCode()).isNotEqualTo(new Object().hashCode());
        assertThat(p1.hashCode()).isNotEqualTo(p2.hashCode());
        assertThat(p1.hashCode()).isNotEqualTo(p3.hashCode());
    }

    @Test
    public void canToString() throws Exception {
        FreqPoint p1 = new FreqPoint<Long>(123L, 456);
        assertThat(p1.toString()).isEqualTo("123: 456");
    }


}
