package com.huertohogar.MercadoPago.controller;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huertohogar.MercadoPago.DTO.CartRequest;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mercadopago")
public class MercadoPagoController {

    @Value("${mercado-pago.access.token}")
    private String mercadoPagoToken;

    @PostMapping("/preference")
    public ResponseEntity<?> createPreference(@RequestBody CartRequest cartRequest) throws Exception {
        // Validar que items no sea null o vacío
        if (cartRequest.getItems() == null || cartRequest.getItems().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "La lista de items no puede estar vacía");
            error.put("ejemplo", "{\"items\": [{\"title\": \"Producto\", \"quantity\": 1, \"unitPrice\": 1000}]}");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        MercadoPagoConfig.setAccessToken(mercadoPagoToken);

        // Crear lista de items de la preferencia
        List<PreferenceItemRequest> items = new ArrayList<>();
        
        for (CartRequest.Item item : cartRequest.getItems()) {
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(item.getTitle())
                    .quantity(item.getQuantity())
                    .unitPrice(BigDecimal.valueOf(item.getUnitPrice()))
                    .currencyId("CLP") // Cambiar según tu moneda
                    .build();
            items.add(itemRequest);
        }

        // Crear la preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .build();

        // Crear cliente y obtener preferencia
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        // Retornar respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("id", preference.getId());
        response.put("init_point", preference.getInitPoint());
        
        return ResponseEntity.ok(response);
    }

}
