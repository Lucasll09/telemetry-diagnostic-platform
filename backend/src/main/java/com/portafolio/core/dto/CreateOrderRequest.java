package com.portafolio.core.dto;

import com.portafolio.core.entities.Device;

public record CreateOrderRequest(
    Device device,
    String reportedProblem
) {}