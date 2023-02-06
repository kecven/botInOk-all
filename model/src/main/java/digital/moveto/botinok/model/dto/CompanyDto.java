package digital.moveto.botinok.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class CompanyDto implements Serializable {

    private UUID id;

    private String name;

    private String link;
}
