package digital.moveto.botinok.model.dto;

import digital.moveto.botinok.model.entities.Contact;
import digital.moveto.botinok.model.Const;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ContactDto implements Serializable {

    private UUID id;

    private AccountDto accountDto;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String location;

    private String position;

    private String linkedinUrl;

    private LocalDate createdDate;

    private LocalDate updatedDate;

    private LocalDate parseDate;

    private String html;

    @Override
    public String toString(){
        return "ContactDto(id=" +id +", name=" + firstName + " " + lastName + ", email=" + email + ", phone=" + phone + ", location=" + location + ", position=" + position + ", linkedinUrl=" + linkedinUrl + ")";
    }

    public Contact toEntity(){
        return Const.modelMapper.map(this, Contact.class);
    }
}
