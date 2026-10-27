package com.portafolio.core.entities;

public enum OrderStatus {
    RECEIVED,           // Equipo ingresado al taller
    IN_DIAGNOSIS,       // En banco de pruebas / estrés
    WAITING_PARTS,      // Esperando repuestos (ej. placa AM5 de reemplazo)
    DIAGNOSTIC_FAILED,  // El Worker detectó anomalías térmicas o de voltaje
    QUALITY_PASSED,     // Telemetría estable, pruebas superadas
    READY_FOR_DELIVERY  // Listo para entregar al cliente
}