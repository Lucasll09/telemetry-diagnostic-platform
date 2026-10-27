package com.portafolio.core.services;

import com.portafolio.core.config.RabbitMQConfig;
import com.portafolio.core.dto.TelemetryEvent;
import com.portafolio.core.entities.OrderStatus;
import com.portafolio.core.entities.ServiceOrder;
import com.portafolio.core.repositories.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j // Lombok: Permite usar log.info() para imprimir en consola
@Service
@RequiredArgsConstructor
public class TelemetryDiagnosticWorker {

    private final ServiceOrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.TELEMETRY_QUEUE)
    @Transactional
    public void processTelemetry(TelemetryEvent event) {
        log.info("📡 Evento de telemetría recibido para Orden #{}: CPU {}°C | GPU {}°C", 
                 event.orderId(), event.cpuTemperature(), event.gpuTemperature());

        // Buscamos la orden en la base de datos
        ServiceOrder order = orderRepository.findById(event.orderId())
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        // Cambiamos el estado a diagnóstico activo si acaba de empezar
        if (order.getStatus() == OrderStatus.RECEIVED) {
            order.setStatus(OrderStatus.IN_DIAGNOSIS);
        }

        // Reglas de negocio: Detectar anomalías térmicas o eléctricas
        if (event.cpuTemperature() > 95.0 || !event.voltageStable()) {
            log.warn("⚠️ Anomalía crítica detectada en Orden #{}. Deteniendo pruebas.", event.orderId());
            order.setStatus(OrderStatus.DIAGNOSTIC_FAILED);
            order.setTechnicalNotes("Fallo detectado por telemetría. CPU Temp: " 
                + event.cpuTemperature() + "°C. Voltaje estable: " + event.voltageStable());
        }

        orderRepository.save(order);
    }
}