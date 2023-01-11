package cn.zeroclian.util.excel;

import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * @author Justin
 */
@Data
public class Header {

    private String name;
    private String column;
    private int columnIndex;
    private String dateFormat;
    private int columnWidth;
    private XSSFColor color;
    private XSSFColor bgColor;

    public Header(String name, String column, int columnIndex, String dateFormat, int columnWidth) {
        this.name = name;
        this.column = column;
        this.columnIndex = columnIndex;
        this.dateFormat = dateFormat;
        this.columnWidth = columnWidth;
    }
}
