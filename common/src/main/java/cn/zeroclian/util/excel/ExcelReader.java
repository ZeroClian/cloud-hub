package cn.zeroclian.util.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author ZeroClian
 * @date 2023-01-10 15:35
 */
public class ExcelReader<T> {

    private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);
    private static final DefaultConversionService convert = new DefaultConversionService();

    private final Supplier<T> supplier;
    private boolean evaluateFormula = false;
    private FormulaEvaluator formulaEvaluator = null;

    public ExcelReader(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * 设置自动计算公式
     *
     * @param evaluateFormula true表示执行计算
     */
    public void setEvaluateFormula(boolean evaluateFormula) {
        this.evaluateFormula = evaluateFormula;
    }

    /**
     * 读取Excel
     *
     * @param in 输入流
     * @return {@link List<T> objects} 对象列表
     * @throws IOException
     */
    public List<T> read(InputStream in) throws IOException {
        Workbook workbook = WorkbookFactory.create(in);
        if (evaluateFormula) {
            this.formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        }
        //读取数据
        List<T> list = new ArrayList<>();
        for (Sheet sheet : workbook) {
            List<String> headers = getSheetHeaders(sheet);
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    continue;
                }
                T obj = rowToObject(headers, row, supplier);
                if (null != obj) {
                    list.add(obj);
                }
            }
        }
        return list;
    }

    /**
     * 获取列名（位于第一行）
     *
     * @param sheet 页
     * @return {@link List<String> headers} 列名列表
     */
    public List<String> getSheetHeaders(Sheet sheet) {
        Row row = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (Cell cell : row) {
            String cellValue = cell.getStringCellValue();
            headers.add(cellValue);
        }
        return headers;
    }

    /**
     * 单行转为对象
     *
     * @param headers  列名
     * @param row      行
     * @param supplier 要转换的对象
     * @return {@link Object}
     */
    private T rowToObject(List<String> headers, Row row, Supplier<T> supplier) {
        try {
            T obj = supplier.get();
            //获取字段
            Field[] fields = obj.getClass().getDeclaredFields();
            boolean isEmpty = true;
            for (Field field : fields) {
                SheetColumn ec = field.getAnnotation(SheetColumn.class);
                if (null == ec) continue;
                int columnIndex = headers.indexOf(ec.name());
                if (columnIndex >= 0) {
                    Cell cell = row.getCell(columnIndex);
                    if (null != cell) {
                        isEmpty = setValue(obj, field, cell, ec.dateFormat());
                    }
                }
            }
            return isEmpty ? null : obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向对象设置值
     *
     * @param obj        对象
     * @param field      字段
     * @param cell       单元格
     * @param dateFormat 日期格式
     */
    private boolean setValue(T obj, Field field, Cell cell, String dateFormat) throws IllegalAccessException, ParseException {
        Object value = Excel.getCellValue(cell, formulaEvaluator);
        if (null == value) return true;
        boolean emptyValue = ObjectUtils.isEmpty(value);
        Class<?> valueClass = value.getClass();
        Class<?> fieldClass = field.getType();
        field.setAccessible(true);
        if (fieldClass == valueClass) {
            field.set(obj, value);
            return false;
        }
        //java8 日期、时间
        if (Temporal.class.isAssignableFrom(fieldClass)) {
            if (emptyValue) {
                return true;
            }
            String cellStr = value.toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            if (LocalDateTime.class.isAssignableFrom(fieldClass)) {
                field.set(obj, LocalDateTime.parse(cellStr, formatter));
            }
            if (LocalDate.class.isAssignableFrom(fieldClass)) {
                field.set(obj, LocalDate.parse(cellStr, formatter));
            }
            if (OffsetDateTime.class.isAssignableFrom(fieldClass)) {
                field.set(obj, OffsetDateTime.parse(cellStr, formatter));
            }
        }
        //java.util.Date
        if (Date.class.isAssignableFrom(fieldClass)) {
            if (emptyValue) {
                return true;
            }
            String cellStr = value.toString();
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            Date date = format.parse(cellStr);
            field.set(obj, date);
            return false;
        }
        if (convert.canConvert(valueClass, fieldClass)) {
            Object newValue = ExcelReader.convert.convert(value, fieldClass);
            if (null != newValue) {
                field.set(obj, newValue);
            }
            return false;
        }
        throw new IllegalArgumentException("表数据类型与类字段类型不一致: " + value.getClass() + " -> " + field.getType());
    }


}
