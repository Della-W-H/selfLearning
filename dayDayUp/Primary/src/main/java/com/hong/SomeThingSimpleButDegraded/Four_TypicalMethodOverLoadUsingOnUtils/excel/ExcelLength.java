package com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.excel;


import com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.BaseOperateType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelLength {

    int length() default 0;

    String message() default "not match this length.";

    BaseOperateType[] operateType() default {};
}

