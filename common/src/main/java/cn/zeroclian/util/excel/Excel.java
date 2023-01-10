package cn.zeroclian.util.excel;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
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

}
