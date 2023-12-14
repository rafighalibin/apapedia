package com.apapedia.user.dto.response;

import com.apapedia.user.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserBalanceResponse {
    private UserModel buyer;
    private UserModel seller;
}
