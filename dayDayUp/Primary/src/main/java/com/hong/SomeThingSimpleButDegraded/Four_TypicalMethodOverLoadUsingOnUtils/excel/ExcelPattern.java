package com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.excel;

import com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.BaseOperateType;

import java.lang.annotation.*;
import java.util.regex.Pattern;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExcelPattern {

    String regexp();

    Flag[] flags() default {};

    String message() default "not match this pattern.";

    BaseOperateType[] operateType() default {};

    /**
     * Possible Regexp flags.
     */
    public static enum Flag {

        /**
         * Enables Unix lines mode.
         *
         * @see java.util.regex.Pattern#UNIX_LINES
         */
        UNIX_LINES(Pattern.UNIX_LINES),

        /**
         * Enables case-insensitive matching.
         *
         * @see java.util.regex.Pattern#CASE_INSENSITIVE
         */
        CASE_INSENSITIVE(Pattern.CASE_INSENSITIVE),

        /**
         * Permits whitespace and comments in pattern.
         *
         * @see java.util.regex.Pattern#COMMENTS
         */
        COMMENTS(Pattern.COMMENTS),

        /**
         * Enables multiline mode.
         *
         * @see java.util.regex.Pattern#MULTILINE
         */
        MULTILINE(Pattern.MULTILINE),

        /**
         * Enables dotall mode.
         *
         * @see java.util.regex.Pattern#DOTALL
         */
        DOTALL(Pattern.DOTALL),

        /**
         * Enables Unicode-aware case folding.
         *
         * @see java.util.regex.Pattern#UNICODE_CASE
         */
        UNICODE_CASE(Pattern.UNICODE_CASE),

        /**
         * Enables canonical equivalence.
         *
         * @see java.util.regex.Pattern#CANON_EQ
         */
        CANON_EQ(Pattern.CANON_EQ);

        //JDK flag value
        private final int value;

        private Flag(int value) {
            this.value = value;
        }

        /**
         * @return flag value as defined in {@link java.util.regex.Pattern}
         */
        public int getValue() {
            return value;
        }
    }
}

