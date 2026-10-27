package com.portafolio.core.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "devices")
@Data // Lombok: Genera getters, setters, toString
@NoArgsConstructor // Lombok: Constructor vacío (requerido por JPA)
@AllArgsConstructor // Lombok: Constructor con todos los argumentos
public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    
    @Column(unique = true)
    private String serialNumber;
    
    private boolean underWarranty;
}
