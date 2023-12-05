package com.apapedia.order.dto;

import org.mapstruct.Mapper;

import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order createOrderRequestDTOToOrder(CreateOrderRequestDTO createOrderRequestDTO);
}
