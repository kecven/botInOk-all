package digital.moveto.botinok.model.entities;


import digital.moveto.botinok.model.dto.AccountDto;
import digital.moveto.botinok.model.entities.enums.Location;
import digital.moveto.botinok.model.Const;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "folder")
    private String folder;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "active_search")
    private Boolean activeSearch;

    @Column(name = "position")
    private String position;

    @Enumerated(EnumType.STRING)
    @Column(name = "location", length = 30)
    private Location location;

    @Column(name = "end_date_license")
    private LocalDate endDateLicense;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Contact> contacts;

    @Column(name = "work_in_shabat")
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

    public AccountDto toDto(){
        return Const.modelMapper.map(this, AccountDto.class);
    }
}
