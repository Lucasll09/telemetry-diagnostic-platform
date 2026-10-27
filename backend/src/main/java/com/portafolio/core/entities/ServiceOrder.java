package com.portafolio.core.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación: Muchas órdenes pueden pertenecer a un solo equipo a lo largo del tiempo
    @ManyToOne(optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(columnDefinition = "TEXT")
    private String reportedProblem;
    
    @Column(columnDefinition = "TEXT")
    private String technicalNotes; // Logs del diagnóstico, voltajes, etc.

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime entryDate;
    private LocalDateTime exitDate;

    // Se ejecuta automáticamente justo antes de guardar en PostgreSQL por primera vez
    @PrePersist
    protected void onCreate() {
        this.entryDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = OrderStatus.RECEIVED;
        }
    }
    
}
