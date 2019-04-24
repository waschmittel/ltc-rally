package de.flubba.rally.service;

import de.flubba.rally.entity.Lap;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class RaceResultsServiceTest {

    private static Lap lap(Long duration) {
        Lap lap = new Lap();
        lap.setDuration(duration);
        return lap;
    }

    @Test
    public void getAverage() {
        List<Lap> laps = Lists.newArrayList(
                lap(11L),
                lap(10L),
                lap(11L)
        );
        Assert.assertEquals(new BigDecimal("10.667"), RaceResultsService.getAverage(laps));
    }
}
