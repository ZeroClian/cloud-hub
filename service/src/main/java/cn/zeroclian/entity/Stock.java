package cn.zeroclian.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ZeroClian
 * @date 2022-11-08 00:16
 */
@Data
@Builder
public class Stock {

    private String code;

    private String name;

    private BigDecimal currentPrice;

    private BigDecimal maxPrice;

    private BigDecimal minPrice;

    private String change;
}
