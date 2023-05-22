package digital.moveto.botinok.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "setting")
public class Setting {


    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;
}
