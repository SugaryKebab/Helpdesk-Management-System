package sugarykebab.helpdesk.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "app_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "org_id")
    private Organization org;

    @Column(name = "email")
    private String email;

    @ColumnDefault("1")
    @Column(name = "is_active")
    private Boolean isActive;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "password", length = 256)
    private String password;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @ColumnDefault("'USER'")
    @Lob
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role role; // ADMIN, AGENT, USER

    public String getFullName() {
        return firstName + " " + lastName;
    }
}