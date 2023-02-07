package digital.moveto.botinok.model.entities;

import digital.moveto.botinok.model.dto.MadeContactDto;
import digital.moveto.botinok.model.Const;
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
@Table(name = "made_contact", indexes = {@Index(name = "made_contact_index_linkedin_account_id", columnList = "account_id")})
public class MadeContact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDateTime date;

    public MadeContactDto toDto(){
        return Const.modelMapper.map(this, MadeContactDto.class);
    }
}
