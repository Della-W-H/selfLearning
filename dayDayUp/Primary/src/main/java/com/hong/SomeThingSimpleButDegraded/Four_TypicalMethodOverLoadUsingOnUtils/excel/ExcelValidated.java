package com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.excel;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelValidated {

    /**
     * A string matches this pattern or 
     * a date matches this pattern.
     * 
     * When it is the Date type, it should
     * use like 'yyyy-MM-dd', 'yyyy/MM/dd' etc.
     * @return
     */
    String pattern() default "";

    /**
     * Content can be blank or not.
     * It contains null, empty string, blank string.
     * @return
     */
    boolean blank() default false;

    /**
     * A Number should be greater than or equal this.
     * @return
     */
    ExcelValidatedMin min() default @ExcelValidatedMin;

    /**
     * A Number should be less than or equal this.
     * @return
     */
    ExcelValidatedMax max() default @ExcelValidatedMax;

    /**
     * Return message when validate failed.
     * @return
     */
    String message() default "";

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    @interface ExcelValidatedMin {

        /**
         * A Number should be greater than this.
         * @return
         */
        String value() default "0";

        /**
         * A Number can be equal with min or not.
         * @return
         */
        boolean contains() default true;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    @interface ExcelValidatedMax {

        /**
         * A Number should be less than this.
         * @return
         */
        String value() default "0";

        /**
         * A Number can be equal with max or not.
         * @return
         */
        boolean contains() default true;
    }
}

