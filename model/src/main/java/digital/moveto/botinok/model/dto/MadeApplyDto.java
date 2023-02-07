package digital.moveto.botinok.model.dto;


import digital.moveto.botinok.model.entities.MadeApply;
import digital.moveto.botinok.model.Const;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class MadeApplyDto implements Serializable {

    private UUID id;

    private AccountDto accountDto;

    private CompanyDto companyDto;

    private String position;

    private String link;

    private LocalDateTime date;


    public MadeApply toEntity(){
        return Const.modelMapper.map(this, MadeApply.class);
    }
}
