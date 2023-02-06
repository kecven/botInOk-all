package digital.moveto.botinok.model.repositories;

import digital.moveto.botinok.model.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findByLinkedinUrl(String linkedinUrl);
}
