package com.portafolio.core.repositories;
import com.portafolio.core.entities.OrderStatus;
import com.portafolio.core.entities.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    // SELECT * FROM service_orders WHERE status = ?
    // Ideal para listar todos los equipos que están en "IN_DIAGNOSIS" o "WAITING_PARTS"
    List<ServiceOrder> findByStatus(OrderStatus status);

    
    List<ServiceOrder> findByDeviceId(Long deviceId);
}
