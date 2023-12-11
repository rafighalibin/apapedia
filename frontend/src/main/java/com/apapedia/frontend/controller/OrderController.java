package com.apapedia.frontend.controller;

import com.apapedia.frontend.core.Order;
import com.apapedia.frontend.service.OrderService;
import com.apapedia.frontend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Controller
public class OrderController {

    
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

     @GetMapping("/order/history")
     public String sellerHistory(Model model, HttpServletRequest request) throws IOException, InterruptedException {
         //belum dynamic berdasarkan login
         UUID userId = userService.getUser(request).getId();
         List<Order> data = orderService.getOrderHistory(request, userId);
         model.addAttribute("orders",data);
         return "seller-order-history";
     }


    @PostMapping(value = "/order/update/status/{orderId}",params = { "status" })
    public String updateOrderStatus(@PathVariable("orderId") UUID orderId, HttpServletRequest request,
                                    RedirectAttributes redirectAttributes){
        try {
            int status = Integer.parseInt(request.getParameter("status"));
            if(status != 5){
                status = status+1;
            }
            orderService.updateOrderStatus(request, orderId, status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Gagal memperbarui status order");
            System.out.println(e.toString());
            return "redirect:/order/history";
        }
        redirectAttributes.addFlashAttribute("message", "Berhasil memperbarui status order");

        return "redirect:/order/history";
    }

}
