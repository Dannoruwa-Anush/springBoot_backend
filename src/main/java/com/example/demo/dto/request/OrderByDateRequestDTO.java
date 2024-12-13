package com.example.demo.dto.request;

import java.time.LocalDate;

import com.example.demo.common.projectEnum.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class OrderByDateRequestDTO {

    private LocalDate checkedDate; //format: yyyy-MM-dd

    private OrderStatus checkedStatus;
}
