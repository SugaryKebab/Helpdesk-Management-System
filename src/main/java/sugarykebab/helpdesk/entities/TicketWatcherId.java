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
public class TicketWatcherId implements Serializable {
    private static final long serialVersionUID = -3659906644576284856L;
    @Column(name = "ticket_id", nullable = false, length = 36)
    private String ticketId;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TicketWatcherId entity = (TicketWatcherId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.ticketId, entity.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, ticketId);
    }

}