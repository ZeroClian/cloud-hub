package cn.zeroclian.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Justin
 */
@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${author}")
    public String author;

    @GetMapping("/author")
    public String getAuthor() {
        return author;
    }
}
