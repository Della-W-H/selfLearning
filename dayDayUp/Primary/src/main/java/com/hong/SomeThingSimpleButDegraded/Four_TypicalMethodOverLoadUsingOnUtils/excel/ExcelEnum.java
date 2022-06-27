package com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.excel;

import com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.BaseOperateType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelEnum {

    /**
     * e.g. Enum Type {
     * START, PROCESSING, END;
     * }
     * 
     * if there is not an enum in java, you can write as this:
     * { "START", "PROCESSING", "END" }
     * @return
     */
    String[] value() default {};

    /**
     * if there is an enum in java, you can set enum class in it.
     * it has highest priority.
     * @return
     */
    Class<? extends Enum<?>> enumClass() default Empty.class;

    /**
     * invoke this method in enum, if matches, get its code
     * @return
     */
    String descMethod() default "name";

    String message() default "not in enumeration.";

    BaseOperateType[] operateType() default {};

    enum Empty {
    }
}
