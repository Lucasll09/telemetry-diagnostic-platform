package com.portafolio.core.services;

import com.portafolio.core.entities.Device;
import com.portafolio.core.entities.ServiceOrder;
import com.portafolio.core.repositories.DeviceRepository;
import com.portafolio.core.repositories.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok: Crea un constructor automáticamente con los repositorios "final"
public class ServiceOrderService {

    private final ServiceOrderRepository orderRepository;
    private final DeviceRepository deviceRepository;

    /**
     * Crea una nueva orden de servicio. Si el equipo (Device) es nuevo, lo registra.
     * Si el equipo ya existe en el sistema (por número de serie), lo reutiliza.
     */
    @Transactional // Si algo falla a mitad de camino, revierte todos los cambios en PostgreSQL
    public ServiceOrder createOrder(Device incomingDevice, String reportedProblem) {
        
        // 1. Validar si el equipo ya existe en nuestra base de datos
        Device deviceToProcess;
        Optional<Device> existingDevice = deviceRepository.findBySerialNumber(incomingDevice.getSerialNumber());
        
        if (existingDevice.isPresent()) {
            deviceToProcess = existingDevice.get(); // Reutiliza el historial del equipo
        } else {
            deviceToProcess = deviceRepository.save(incomingDevice); // Guarda el equipo por primera vez
        }

        // 2. Construir la nueva orden de servicio
        ServiceOrder newOrder = new ServiceOrder();
        newOrder.setDevice(deviceToProcess);
        newOrder.setReportedProblem(reportedProblem);
        // Recuerda: status y entryDate se asignan automáticamente por tu @PrePersist en la entidad

        // 3. Guardar en PostgreSQL y retornar
        return orderRepository.save(newOrder);
    }
}