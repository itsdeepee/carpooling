package org.example.Model.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.Model.RideRequestStatus;
import org.example.Util.ConstraintNames;

@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="ride_request",
uniqueConstraints = {
        @UniqueConstraint(
                name = ConstraintNames.UNIQUE_RIDE_USER_CONSTRAINT,
                columnNames = {"ride_id","user_id"}
        )})
public class RideRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ride_id",insertable=false, updatable=false)
    @JsonBackReference
    private RideEntity ride;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity passenger;
    private RideRequestStatus status;
}
