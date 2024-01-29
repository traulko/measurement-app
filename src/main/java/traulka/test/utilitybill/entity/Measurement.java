package traulka.test.utilitybill.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import traulka.test.utilitybill.entity.type.MeasurementType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MEASUREMENT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(setterPrefix = "set")
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Long id;

    // HSQLDB does not support ENUM or TYPE data types
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "TYPE")
    MeasurementType type;

    @Column(name = "VALUE")
    Double value;

    @Column(name = "CREATED_TIME")
    LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "PAYER_ID", nullable = false)
    Payer payer;
}
