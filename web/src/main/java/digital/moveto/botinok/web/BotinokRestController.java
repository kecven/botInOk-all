package digital.moveto.botinok.web;

import digital.moveto.botinok.WebConst;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static digital.moveto.botinok.WebConst.API_ADDRESS;

@RestController
@RequestMapping(API_ADDRESS + "botinok")
@RequiredArgsConstructor
public class BotinokRestController {

    private final Logger log = LoggerFactory.getLogger(BotinokRestController.class);


    @GetMapping("/version")
    public String version() {
        return WebConst.VERSION;
    }

}
