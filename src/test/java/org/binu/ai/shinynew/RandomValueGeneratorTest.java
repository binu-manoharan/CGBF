package org.binu.ai.shinynew;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Random number generator
 */
public class RandomValueGeneratorTest {

    private static final int MAX_VALUE = 5;
    private static final int MIN_VALUE1 = 1;
    private static final int MIN_VALUE0 = 0;

    @Test
    public void should_generate_random_value_between_1_and_5() throws Exception {
        final RandomValueGenerator randomValueGenerator = new RandomValueGenerator();
        final int randomValue = randomValueGenerator.getRandomValueBetween(MIN_VALUE1, MAX_VALUE);
        System.out.println("Random value = " + randomValue);
        assertThat("Random value is between 1 and 5", randomValue <= MAX_VALUE, is(true));
        assertThat("Random value is between 1 and 5", randomValue >= MIN_VALUE1, is(true));
    }

    @Test
    public void should_generate_random_value_between_0_and_5() throws Exception {
        final RandomValueGenerator randomValueGenerator = new RandomValueGenerator();
        final int randomValue = randomValueGenerator.getRandomValueBetween(MIN_VALUE0, MAX_VALUE);
        System.out.println("Random value = " + randomValue);
        assertThat("Random value is between 1 and 5", randomValue <= MAX_VALUE, is(true));
        assertThat("Random value is between 1 and 5", randomValue >= MIN_VALUE0, is(true));

    }
}
