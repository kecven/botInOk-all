package digital.moveto.botinok.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class MadeContactDto implements Serializable {

    private UUID id;

    private AccountDto accountDto;

    private String name;

    private LocalDateTime date;
}
