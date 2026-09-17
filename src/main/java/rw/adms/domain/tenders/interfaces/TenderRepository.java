package rw.adms.domain.tenders.interfaces;

import rw.adms.domain.tenders.Tender;
import rw.adms.domain.tenders.vo.TenderId;

import java.util.List;
import java.util.Optional;

public interface TenderRepository {

    void save(Tender tender);

    Optional<Tender> findById(TenderId id);

    List<Tender> findAll();

    boolean existsById(TenderId id);

    void deleteById(TenderId id);
}