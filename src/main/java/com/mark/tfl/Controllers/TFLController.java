package com.mark.tfl.Controllers;

import com.mark.tfl.Models.LineStatus;
import com.mark.tfl.Services.SchedulingService;
import com.mark.tfl.Services.TFLStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.util.StringUtils.isEmpty;

@Controller
public class TFLController {

    private static final Logger log = LoggerFactory.getLogger(SchedulingService.class);

    @Autowired
    private static TFLStatusService tflStatusService;

    public TFLController() {
        tflStatusService = new TFLStatusService();
    }

    public static void lastScheduledRuntime(String time) {
        log.info("Dashboard - " + time);
        tflStatusService.scheduleAPICall();
    }

    @RequestMapping("/")
    public String homeController(Model model){
        model.addAttribute("title", "Home");
        model.addAttribute("tablecontent", tflStatusService.getLineStatuses());
        model.addAttribute("dropdowncontent", tflStatusService.getLineStatuses());
        return "index";
    }

    @RequestMapping("/issues")
    public String linesWithIssues(Model model) {
        model.addAttribute("title", "Issues");
        model.addAttribute("tablecontent", tflStatusService.getLineIssues());
        model.addAttribute("dropdowncontent", tflStatusService.getLineStatuses());
        return "index";
    }

    @RequestMapping("/checkline")
    public String checkLineStatus(@RequestParam("line") String line, Model model) {
        if (isEmpty(line)) {
            model.addAttribute(new LineStatus("Error", "No line name entered"));
        }
        model.addAttribute("content", tflStatusService.checkLineStatus(line));
        return "jsonoutput";
    }
}