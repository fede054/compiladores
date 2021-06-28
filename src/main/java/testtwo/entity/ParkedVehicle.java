package testtwo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "vehiculo_estacionado")
public class ParkedVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "patente")
    private String patent;

    @Column(name = "piso")
    private Integer floor;

    @Column(name = "posicion")
    private Integer position;

    @Column(name = "ratio")
    private Long ratio;

    @CreationTimestamp
    @Column(name = "fecha_entrada")
    private LocalDateTime entryDateTime;

    @Column(name = "fecha_salida")
    private LocalDateTime exitDateTime;

    public Long getFeeToPay() {
        this.exitDateTime = LocalDateTime.now();
        long hoursAmount = ChronoUnit.HOURS.between(this.entryDateTime, this.exitDateTime);
        return this.ratio * hoursAmount;
    }

}
