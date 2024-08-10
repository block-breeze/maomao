package com.auction.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class PasswordDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String newPassword;
    private String confirmPassword;

}
