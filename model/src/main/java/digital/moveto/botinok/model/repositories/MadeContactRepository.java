package digital.moveto.botinok.model.repositories;

import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.MadeContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MadeContactRepository extends JpaRepository<MadeContact, Long> {
    List<MadeContact> findAllByAccount(Account account);
}
