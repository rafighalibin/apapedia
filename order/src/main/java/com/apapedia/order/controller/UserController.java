package com.apapedia.order.controller;

import com.apapedia.order.dto.request.UpdateBalance;
import com.apapedia.order.dto.response.UpdateUserBalanceResponse;
import com.apapedia.order.model.Order;
import com.apapedia.order.model.UserModel;
import com.apapedia.order.service.OrderService;
import com.apapedia.order.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/order/users")
public class UserController {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
     protected UserService userService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
     protected OrderService orderService;



    @PutMapping("/update-balance")
    public ResponseEntity<?> updateBalance(@Valid @RequestBody UpdateBalance updateBalance,
                                           HttpServletRequest request) {
        Order order = orderService.findById(updateBalance.getOrderId());
        UserModel seller = userService.updateSellerBalance(order);
        UserModel buyer = userService.updateBuyerBalance(order);

        UpdateUserBalanceResponse response = new UpdateUserBalanceResponse();
        response.setOrder(order);
        response.setBuyer(buyer);
        response.setSeller(seller);

        return ResponseEntity.ok(response);
    }
}
