package com.mark.tfl.unittests;

import com.mark.tfl.Models.LineStatus;
import com.mark.tfl.Services.TFLStatusService;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mark.tfl.Services.TimeService.getCurrentTime;
import static java.util.Collections.emptyList;

public class ServiceTests {

    @Test
    public void refreshLineStatusesTest(){
        TFLStatusService tflStatusService = new TFLStatusService();
        List<LineStatus> oldStatuses = tflStatusService.getLineStatuses();

        tflStatusService.scheduleAPICall();
        List<LineStatus> newStatuses = tflStatusService.getLineStatuses();

        Assert.assertEquals(emptyList(), oldStatuses);
        Assert.assertNotEquals(emptyList(), newStatuses);
    }

    @Test
    public void checkTimeTest(){
        String actual = getCurrentTime();

        LocalTime time = LocalTime.now();
        String expected = time.format(DateTimeFormatter.ofPattern("HH:mm a"));

        Assert.assertEquals(expected, actual);
    }
}
