package rw.adms.domain.items.interfaces;

import rw.adms.domain.items.Item;
import rw.adms.domain.items.vo.ItemId;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findById(ItemId id);

    List<Item> findAll();

    boolean existsById(ItemId id);

    void deleteById(ItemId id);
}