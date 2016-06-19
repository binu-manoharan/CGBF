package org.binu.framework;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

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
