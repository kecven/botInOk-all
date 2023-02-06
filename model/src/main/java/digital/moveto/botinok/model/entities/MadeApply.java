package digital.moveto.botinok.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SuperBuilder
@Table(name = "made_apply", indexes = {@Index(name = "made_apply_index_linkedin_account_id", columnList = "account_id")})
public class MadeApply {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "position")
    private String position;

    @Column(name = "link")
    private String link;

    @Column(name = "date")
    private LocalDateTime date;
}
