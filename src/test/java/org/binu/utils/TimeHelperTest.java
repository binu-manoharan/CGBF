package org.binu.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link TimeHelper}
 */
public class TimeHelperTest {
    @Test
    public void should_be_atleast_50ms_since_start_time() throws Exception {
        final TimeHelper timeHelper = new TimeHelper();
        Thread.sleep(50L);
        final long timeSinceStart = timeHelper.getTimeSinceStartInMills();
        assertThat("It has been atleast 50ms since start time", timeSinceStart >= 50, is(true));
    }
}
