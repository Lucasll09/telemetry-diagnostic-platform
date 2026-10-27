package com.portafolio.core.dto;

public record TelemetryEvent(
    Long orderId,          // Para saber a qué orden asignarle el diagnóstico
    double cpuTemperature, // Temperatura del procesador en °C
    double gpuTemperature, // Temperatura de la gráfica en °C
    boolean voltageStable  // Falso si hay una caída de tensión en la línea de 12V
) {}
