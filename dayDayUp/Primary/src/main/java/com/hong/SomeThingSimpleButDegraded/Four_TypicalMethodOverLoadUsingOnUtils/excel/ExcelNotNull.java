package com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.excel;

import com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.BaseOperateType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelNotNull {

    String message() default "can not be null.";

    BaseOperateType[] operateType() default {};
}

