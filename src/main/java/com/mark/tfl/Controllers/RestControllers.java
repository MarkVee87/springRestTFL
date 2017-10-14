package com.mark.tfl.Controllers;

import com.mark.tfl.Models.LineStatus;
import com.mark.tfl.Services.SchedulingService;
import com.mark.tfl.Services.TFLStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@RestController
public class RestControllers {

    private static final Logger log = LoggerFactory.getLogger(SchedulingService.class);

    @Autowired
    private static TFLStatusService tflStatusService;

    static TFLStatusService getTflStatusService() {
        return tflStatusService;
    }

    public RestControllers() {
        tflStatusService = new TFLStatusService();
    }

    public static void lastScheduledRuntime(String time) {
        log.info("Dashboard - " + time);
        tflStatusService.scheduleAPICall();
    }

    @RequestMapping("/allstatuses")
    public List<LineStatus> allLinesStatuses() throws IOException {
        return tflStatusService.getLineStatuses();
    }

    @RequestMapping("/issues")
    public List<LineStatus> linesWithIssues() {
        return tflStatusService.getLineIssues();
    }

    @RequestMapping("/checkline")
    public LineStatus checkLineStatus(@RequestParam("line") String line) {
        if (isEmpty(line)) {
            return new LineStatus("Error", "No line name entered");
        }
        return tflStatusService.checkLineStatus(line);
    }
}