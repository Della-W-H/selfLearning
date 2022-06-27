package com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.excel;

import com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.BaseOperateType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelRange {

    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;

    String message() default "not in this range.";

    BaseOperateType[] operateType() default {};
}

