package com.portafolio.core.repositories;

import com.portafolio.core.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    // Spring genera la consulta SQL automáticamente basándose en el nombre del método
    // Útil para verificar si un equipo ya ingresó al taller antes
    Optional<Device> findBySerialNumber(String serialNumber);
    
}
