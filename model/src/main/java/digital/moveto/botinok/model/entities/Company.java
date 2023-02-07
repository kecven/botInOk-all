package digital.moveto.botinok.model.entities;

import digital.moveto.botinok.model.dto.CompanyDto;
import digital.moveto.botinok.model.Const;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SuperBuilder
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    public CompanyDto toDto(){
        return Const.modelMapper.map(this,CompanyDto.class);
    }
}
