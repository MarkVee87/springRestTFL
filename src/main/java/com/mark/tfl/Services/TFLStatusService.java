package com.mark.tfl.Services;

import com.mark.tfl.Models.LineStatus;
import com.mark.tfl.Models.TFLResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TFLStatusService {

    private static final Logger log = LoggerFactory.getLogger(SchedulingService.class);
    private ObjectMapper objectMapper;
    private List<LineStatus> allLineStatuses;
    private List<LineStatus> linesWithIssues;
    private List<TFLResponse> tflRawLineStatuses;

    public TFLStatusService() {
        objectMapper = new ObjectMapper();
        linesWithIssues = new ArrayList<>();
        tflRawLineStatuses = new ArrayList<>();
        allLineStatuses = new ArrayList<>();
    }

    public void setAllLineStatuses(List<LineStatus> allLineStatuses) {
        this.allLineStatuses = allLineStatuses;
    }

    public void setLinesWithIssues(List<LineStatus> linesWithIssues) {
        this.linesWithIssues = linesWithIssues;
    }

    public void scheduleAPICall() {
        log.info("Updating local data on tube statuses...");
        runAllStatusChecks();
        log.info("Update complete");
    }

    public List<LineStatus> getLineStatuses() {
        if (allLineStatuses == null) {
            scheduleAPICall();
        }
        return allLineStatuses;
    }

    public List<LineStatus> getLineIssues() {
        if (linesWithIssues == null) {
            scheduleAPICall();
        }
        return linesWithIssues;
    }

    public LineStatus checkLineStatus(String line) {
        runAllStatusChecks();
        for (LineStatus lineStatus : allLineStatuses) {
            if (lineStatus.getLineName().equalsIgnoreCase(line)) {
                return lineStatus;
            }
        }
        return new LineStatus("Error", "Line \"" + line + "\" is not recognised");
    }

    private void runAllStatusChecks() {
        if (tflRawLineStatuses.isEmpty() || allLineStatuses.isEmpty() || linesWithIssues.isEmpty()) {
            getTFLResponse();
            getAllLineStatuses();
            getLinesWithIssues();
        }
    }

    private void getTFLResponse() {
        try {
            URL url = new URL("https://api.tfl.gov.uk/line/mode/tube/status");
            tflRawLineStatuses = objectMapper.readValue(url, new TypeReference<List<TFLResponse>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAllLineStatuses() {
        List<LineStatus> newLineStatuses = new ArrayList<>();
        for (TFLResponse tflResponse : tflRawLineStatuses) {
            LineStatus lineStatus = new LineStatus(tflResponse.getName(), tflResponse.getStatus());
            newLineStatuses.add(lineStatus);
        }
        setAllLineStatuses(newLineStatuses);
    }

    private void getLinesWithIssues() {
        List<LineStatus> newlinesWithIssues = new ArrayList<>();
        for (LineStatus lineStatus : allLineStatuses) {
            String status = lineStatus.getLineStatus();
            if (!Objects.equals(status, "Good Service") && !Objects.equals(status, "Minor Delays")) {
                newlinesWithIssues.add(lineStatus);
            }
        }
        setLinesWithIssues(newlinesWithIssues);
    }
}