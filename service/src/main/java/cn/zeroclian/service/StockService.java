package cn.zeroclian.service;

import cn.zeroclian.entity.Stock;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


/**
 * @author ZeroClian
 * @date 2022-11-07 23:25
 */
@Service
public class StockService {

    public List<Stock> getStockInfo(String stockCode) {
        Document document = null;
        Stock stock = null;
        try {
            document = Jsoup.connect("https://xueqiu.com/S/" + stockCode)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                    .ignoreContentType(true)
                    .get();
            Elements current = document.getElementsByClass("stock-current");
            Elements name = document.getElementsByClass("stock-name");
            BigDecimal currentPrice = new BigDecimal(current.text().substring(1));
            Elements change = document.getElementsByClass("stock-change");
            stock = Stock.builder().currentPrice(currentPrice).name(name.text()).change(change.text()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of(stock);
    }
}
