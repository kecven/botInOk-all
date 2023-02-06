package digital.moveto.botinok.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SuperBuilder
@Table(name = "contact", indexes = {@Index(name = "contact_index_linkedin_url", columnList = "linkedin_url")})
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "location")
    private String location;

    @Column(name = "position")
    private String position;

    @Column(name = "linkedin_url", nullable = false, unique = true)
    private String linkedinUrl;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDate createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Column(name = "parse_date")
    private LocalDate parseDate;

    @Column(name = "html", length = 10_000_000)
    private String html;

    @Override
    public String toString(){
        return "Contact(id=" +id +", name=" + firstName + " " + lastName + ", email=" + email + ", phone=" + phone + ", location=" + location + ", position=" + position + ", linkedinUrl=" + linkedinUrl + ")";
    }
}
