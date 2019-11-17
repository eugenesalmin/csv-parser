package com.test.csvparser.controller;

import com.test.csvparser.model.CsvStat;
import com.test.csvparser.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    CsvService csvService;

    @GetMapping(value = "/")
    public String analyzeCsv(@RequestParam("folder") String folder, Model model) {
        List<CsvStat> stats = csvService.analyzeCSVs(folder);

        model.addAttribute("folder", folder);

        if (stats == null) {
            return "error";
        }

        model.addAttribute("stats", stats);
        return "list";
    }
}
