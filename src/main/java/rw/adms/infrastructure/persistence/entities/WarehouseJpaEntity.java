package rw.adms.infrastructure.persistence.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "warehouses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_warehouse_name",
                        columnNames = "name"
                )
        }
)
public class WarehouseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "warehouse_items",
            joinColumns = @JoinColumn(name = "warehouse_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "uk_warehouse_item",
                            columnNames = {
                                    "warehouse_id",
                                    "item_id"
                            }
                    )
            }
    )
    private List<ItemJpaEntity> items = new ArrayList<>();

    protected WarehouseJpaEntity() {
    }

    public WarehouseJpaEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ItemJpaEntity> getItems() {
        return items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(List<ItemJpaEntity> items) {
        this.items = items;
    }
}