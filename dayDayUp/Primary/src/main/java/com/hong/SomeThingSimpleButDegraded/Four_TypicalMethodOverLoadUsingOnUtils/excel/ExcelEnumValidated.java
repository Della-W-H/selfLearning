package com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.excel;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelEnumValidated {

    /**
     * A description of enumeration sorted by index
     * e.g. Enum Type {
     * Start(1, "start"),
     * Approve(2, "approve"),
     * end(3, "end"); }
     * 
     * if there is not an enum in java, you can write as this:
     * { "start", "approve", "end" }
     * and set start index 1
     * 
     * @return
     */
    String[] desc() default { "" };

    /**
     * start index where the code of description start from.
     * @return
     */
    int start() default 0;

    /**
     * If a description can not be found, set defaultVaule.
     * @return
     */
    int defaultVaule() default -1;

    /**
     * if there is an enum in java, you can set enum class in it.
     * @return
     */
    Class<?> enumClass() default Class.class;

    /**
     * Use enumFunc and funcParamType to find out the method,
     * and locate the code of enumeration.
     * Only support one parameter.
     * This method must return integer type.
     * @return
     */
    String enumFunc() default "";

    /**
     * In parameter of method enumFunc.
     * Use enumFunc and funcParamType to find out the method,
     * and locate the code of enumeration.
     * @return
     */
    Class<?> funcParamType() default String.class;
}

