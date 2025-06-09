package com.snaphade.customerportfolio.dto;

import java.util.List;

public record CustomerInformationDTO(Integer id, String name, Integer balance, List<Holding> holdings) {
}
