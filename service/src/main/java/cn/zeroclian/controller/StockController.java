package cn.zeroclian.controller;

import cn.zeroclian.entity.Stock;
import cn.zeroclian.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZeroClian
 * @date 2022-11-08 00:27
 */
@RestController
@RequestMapping("stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/list")
    public List<Stock> getStockInfo() {
        return stockService.getStockInfo("SZ002866");
    }
}
