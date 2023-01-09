package cn.zeroclian.util.excel;

import java.lang.annotation.*;

/**
 * 列注解
 *
 * @author ZeroClian
 * @date 2023-01-06 20:20
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SheetColumn {

    String name() default "";

    String column() default "";

    int width() default 16;

    String dateFormat() default "yyyy-MM-dd";
}
