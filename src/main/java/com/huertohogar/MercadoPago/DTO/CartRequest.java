package com.huertohogar.MercadoPago.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartRequest {
    private List<Item> items;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Item {
        private String title;
        private Integer quantity;
        private Double unitPrice;
    }
}
