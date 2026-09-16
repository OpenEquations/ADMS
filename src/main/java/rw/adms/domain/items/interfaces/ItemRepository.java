package rw.adms.domain.items.interfaces;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.vo.ItemId;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    void save(Item item);

    Optional<Item> findById(ItemId id);

    List<Item> findAll();

    void deleteById(ItemId id);
}