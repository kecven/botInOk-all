package digital.moveto.botinok.model.dto;


import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.enums.Location;
import digital.moveto.botinok.model.Const;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class AccountDto implements Serializable {
    private UUID id;

    private String folder;

    private String firstName;

    private String lastName;

    private String login;

    private String password;

    private Boolean active;

    private Boolean activeSearch;

    private String position;

    private Location location;

    private LocalDate endDateLicense;

    private List<ContactDto> contactDtos;

    private Boolean workInShabat;

    public String getFullName() {
        if (Strings.isNotBlank(firstName) && Strings.isNotBlank(lastName)) {
            return firstName + " " + lastName;
        }

        if (Strings.isNotBlank(firstName)) {
            return lastName;
        }

        if (Strings.isNotBlank(lastName)) {
            return firstName;
        }

        return "Default";
    }

    public Account toEntity(){
        return Const.modelMapper.map(this, Account.class);
    }
}
