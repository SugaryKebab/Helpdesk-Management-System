package sugarykebab.helpdesk.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "sla_policy")
public class SlaPolicy {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "org_id", nullable = false)
    private Organization org;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "target_first_response_minutes", nullable = false)
    private Integer targetFirstResponseMinutes;

    @Column(name = "target_resolution_minutes", nullable = false)
    private Integer targetResolutionMinutes;

    @ColumnDefault("1")
    @Column(name = "active", nullable = false)
    private Boolean active = false;

}