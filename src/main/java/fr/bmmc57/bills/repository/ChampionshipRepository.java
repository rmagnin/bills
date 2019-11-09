package fr.bmmc57.bills.repository;
import fr.bmmc57.bills.domain.Championship;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Championship entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChampionshipRepository extends JpaRepository<Championship, Long> {

}
