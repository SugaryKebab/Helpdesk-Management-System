package sugarykebab.helpdesk.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class TicketTagId implements Serializable {
    private static final long serialVersionUID = -4495159887638749470L;
    @Column(name = "ticket_id", nullable = false, length = 36)
    private String ticketId;

    @Column(name = "tag_id", nullable = false, length = 36)
    private String tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TicketTagId entity = (TicketTagId) o;
        return Objects.equals(this.tagId, entity.tagId) &&
                Objects.equals(this.ticketId, entity.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, ticketId);
    }

}