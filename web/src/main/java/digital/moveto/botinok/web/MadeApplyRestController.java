package digital.moveto.botinok.web;

import digital.moveto.botinok.model.dto.MadeApplyDto;
import digital.moveto.botinok.model.repositories.MadeApplyRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static digital.moveto.botinok.WebConst.API_ADDRESS;

@RestController
@RequestMapping(API_ADDRESS + "madeApply")
@RequiredArgsConstructor
public class MadeApplyRestController {

    private final Logger log = LoggerFactory.getLogger(MadeApplyRestController.class);

    private final MadeApplyRepository madeApplyRepository;

    @PostMapping("/save")
    MadeApplyDto save(@RequestBody MadeApplyDto madeApplyDto) {
        return madeApplyRepository.save(madeApplyDto.toEntity()).toDto();
    }

}
