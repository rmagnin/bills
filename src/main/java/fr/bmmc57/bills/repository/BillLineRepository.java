package fr.bmmc57.bills.repository;
import fr.bmmc57.bills.domain.BillLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BillLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillLineRepository extends JpaRepository<BillLine, Long> {

}
