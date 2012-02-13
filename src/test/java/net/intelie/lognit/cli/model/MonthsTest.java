package net.intelie.lognit.cli.model;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class MonthsTest {
    @Test
    public void withValidMonths() {
        assertThat(Months.forNumber("01")).isEqualTo("Jan");
        assertThat(Months.forNumber("02")).isEqualTo("Feb");
        assertThat(Months.forNumber("03")).isEqualTo("Mar");
        assertThat(Months.forNumber("04")).isEqualTo("Apr");
        assertThat(Months.forNumber("05")).isEqualTo("May");
        assertThat(Months.forNumber("06")).isEqualTo("Jun");
        assertThat(Months.forNumber("07")).isEqualTo("Jul");
        assertThat(Months.forNumber("08")).isEqualTo("Aug");
        assertThat(Months.forNumber("09")).isEqualTo("Sep");
        assertThat(Months.forNumber("10")).isEqualTo("Oct");
        assertThat(Months.forNumber("11")).isEqualTo("Nov");
        assertThat(Months.forNumber("12")).isEqualTo("Dec");

    }

    @Test
    public void withInvalidMonths() {
        assertThat(Months.forNumber("0")).isEqualTo("?0?");
        assertThat(Months.forNumber("13")).isEqualTo("?13?");
        assertThat(Months.forNumber("abc")).isEqualTo("?abc?");

    }
}
