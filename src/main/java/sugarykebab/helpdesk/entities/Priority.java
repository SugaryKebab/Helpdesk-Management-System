package sugarykebab.helpdesk.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "priority")
public class Priority {
    @Id
    @Column(name = "id", nullable = false)
    private Byte id;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @ColumnDefault("100")
    @Column(name = "sort_order", nullable = false)
    private Short sortOrder;

}