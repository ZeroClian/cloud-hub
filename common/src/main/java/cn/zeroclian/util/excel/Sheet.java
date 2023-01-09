package cn.zeroclian.util.excel;

import java.lang.annotation.*;

/**
 * Excel表整体格式配置（非必选）
 * 主要用于设置表格格式
 * 修改：字体颜色，背景颜色
 *
 * @author ZeroClian
 * @date 2023-01-06 20:08
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {

    /**
     * 表名
     */
    String name() default "";

    /**
     * 字体颜色
     */
    String color() default "#000000";

    /**
     * 背景颜色
     */
    String bgColor() default "#CBCBCB";
}
