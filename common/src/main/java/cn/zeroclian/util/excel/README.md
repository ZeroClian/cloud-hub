# Excel 的工具类说明

字段添加注解 `@SheetColumn`

```java

// @Sheet 不是必须的, 如果需要设置字体颜色/背景色, 可以使用该注解
@Sheet(bgColor = "#CBCBCB")
public class Demo {

    @SheetColumn(name = "ID")
    public Integer id;

    @SheetColumn(name = "Name")
    public String name;

    @SheetColumn(name = "Description", width = 30) // 宽度接近英文字符数
    public String description;

    // 默认为: yyyy-MM-dd, 根据实际情况设置
    @SheetColumn(name = "Created At", dateFormat = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createdAt;

}
```

## 读取表格数据

```java
ExcelReader<Demo> reader=Excel.createReader(Demo::new);
        reader.setEvaluateFormula(true); // 如果需要计算公式, 请设置为 true
        List<Demo> list=reader.read(inputStream);
```

## 写入表格数据

```java
ExcelWriter<Demo> writer=Excel.createWriter(Demo.class);
        Demo demo=new Demo();
        demo.name="哈哈";
        demo.createdAt=LocalDateTime.now();
        ...
        writer.write(demo);// 也可以是 List
        writer.save(new FileOutputStream("D:/a.xlsx"));
        writer.close();
```

