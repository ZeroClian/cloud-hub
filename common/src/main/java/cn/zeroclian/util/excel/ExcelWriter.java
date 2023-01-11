package cn.zeroclian.util.excel;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * @author ZeroClian
 */
public class ExcelWriter<T> implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(Excel.class);

    private final Class<T> clazz;
    private final SXSSFWorkbook workbook;
    private SXSSFSheet sheet;
    private Map<Field, Header> fieldHeaderMap;

    public ExcelWriter(Class<T> clazz) {
        this.clazz = clazz;
        this.workbook = new SXSSFWorkbook(300);
        this.init();
        this.createSheet();
    }

    /**
     * 获取字段对应的列
     */
    public void init() {
        Map<Field, Header> map = new LinkedHashMap<>();
        Set<Integer> usedIndex = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        for (Field field : clazz.getDeclaredFields()) {
            SheetColumn ec = field.getAnnotation(SheetColumn.class);
            if (null == ec) continue;
            int columnIndex = -1;
            if (!ec.column().isEmpty()) {
                columnIndex = Excel.toColumnIndex(ec.column());
                if (!usedIndex.add(columnIndex)) {
                    logger.warn("重复列编号: {}", ec.column());
                }
            }
            Header header = new Header(ec.name(), ec.column(), columnIndex, ec.dateFormat(), ec.width());
            map.put(field, header);
            queue.add(queue.size());
        }
        queue.removeAll(usedIndex);
        map.forEach((field, header) -> {
            if (header.getColumnIndex() < 0) {
                header.setColumnIndex(queue.remove());
            }
        });
        this.fieldHeaderMap = map;
    }

    /**
     * 创建Sheet和表头
     */
    public void createSheet() {
        String name = "Sheet1";
        String color = "#000000";
        String bgColor = "#CBCBCB";
        Sheet sheetInfo = clazz.getAnnotation(Sheet.class);
        if (null != sheetInfo) {
            if (!sheetInfo.name().isBlank()) {
                name = sheetInfo.name();
            }
            color = sheetInfo.color();
            bgColor = sheetInfo.bgColor();
        }
        this.sheet = workbook.createSheet(name);
        //表头
        Row headerRow = sheet.createRow(0);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(Excel.createColor(bgColor));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setColor(Excel.createColor(color));
        font.setBold(true);
        cellStyle.setFont(font);
        sheet.trackAllColumnsForAutoSizing();
        fieldHeaderMap.forEach((field, header) -> {
            if (header.getColumnWidth() > 0) {
                int width = Math.min(16_384, header.getColumnWidth() * 256);
                sheet.setColumnWidth(header.getColumnIndex(), width);
            } else if (header.getColumnWidth() == 0) {
                sheet.setColumnHidden(header.getColumnIndex(), true);
            } else {
                sheet.autoSizeColumn(header.getColumnIndex());
            }
        });
        fieldHeaderMap.forEach((field, header) -> {
            Cell cell = headerRow.createCell(header.getColumnIndex());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(header.getName());
        });
    }

    /**
     * 写入单个对象
     *
     * @param obj 对象
     * @throws IOException
     */
    public void write(T obj) throws IOException {
        try {
            if (null == obj) {
                return;
            }
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            for (Map.Entry<Field, Header> entry : fieldHeaderMap.entrySet()) {
                Cell cell = row.createCell(entry.getValue().getColumnIndex());
                Field field = entry.getKey();
                Header header = fieldHeaderMap.get(field);
                Object cellValue = field.get(obj);
                String value;
                if (cellValue instanceof Date date) {
                    value = DateFormatUtils.format(date, header.getDateFormat());
                } else if (cellValue instanceof Temporal temporal) {
                    value = DateTimeFormatter.ofPattern(header.getDateFormat()).format(temporal);
                } else {
                    value = Objects.toString(cellValue, "");
                }
                cell.setCellValue(value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(List<T> list) throws IOException {
        for (T obj : list) {
            write(obj);
        }
    }

    public void save(OutputStream out) throws Exception {
        workbook.write(out);
        close();
    }

    @Override
    public void close() throws Exception {
        this.workbook.close();
    }

}
