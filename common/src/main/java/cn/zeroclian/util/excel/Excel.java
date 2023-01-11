package cn.zeroclian.util.excel;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * @author ZeroClian
 * @date 2023-01-10 15:35
 */
public class Excel {
    private static final Logger logger = LoggerFactory.getLogger(Excel.class);

    public static ExcelReader<T> createReader(Supplier<T> supplier) {
        return new ExcelReader<>(supplier);
    }

    public static ExcelWriter<T> createWriter(Class<T> clazz) {
        return new ExcelWriter<>(clazz);
    }


    public static Object getCellValue(Cell cell, FormulaEvaluator formulaEvaluator) {
        switch (cell.getCellType()) {
            case ERROR:
                return ErrorEval.getText(cell.getErrorCellValue());
            case BLANK:
                return "";
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                if (formulaEvaluator == null) {
                    return cell.getCellFormula();
                } else {
                    CellValue cellValue = formulaEvaluator.evaluate(cell);
                    if (cellValue.getCellType() == CellType.NUMERIC) {
                        return cellValue.getNumberValue();
                    }
                    if (cellValue.getCellType() == CellType.STRING) {
                        return cellValue.getStringValue();
                    }
                    if (cellValue.getCellType() == CellType.BOOLEAN) {
                        return cellValue.getBooleanValue();
                    }
                    return cellValue.toString();
                }
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case STRING:
                return cell.getRichStringCellValue().toString();
            default:
                logger.error("Unknown Cell Type: {}", cell.getCellType());
                return null;
        }
    }

    /**
     * 26进制转换为十进制, 然后减去一(列索引基于0)
     *
     * @param name
     * @return
     */
    public static int toColumnIndex(String name) {
        int n = 0;
        for (int i = name.length() - 1, j = 1; i >= 0; i--, j *= 26) {
            char c = name.charAt(i);
            if (c < 'A' || c > 'Z') return 0;
            n += ((int) c - 64) * j;
        }
        return n - 1;//index要减一
    }

    /**
     * @param nm 例如: #FFCCEE
     * @return
     */
    static XSSFColor createColor(String nm) {
        java.awt.Color color = java.awt.Color.decode(nm);
        byte r = (byte) color.getRed();
        byte g = (byte) color.getGreen();
        byte b = (byte) color.getBlue();
        return new XSSFColor(new byte[]{r, g, b});
    }

}
