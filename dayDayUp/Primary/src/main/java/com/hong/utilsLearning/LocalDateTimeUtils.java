package com.hong.utilsLearning;

import cn.hutool.core.map.MapUtil;
import org.springframework.util.CollectionUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

public class LocalDateTimeUtils {

    public static final String DEFAULT_YEAR_PATTERN = "yyyy";
    public static final String DEFAULT_YEAR_MONTH_PATTERN = "yyyy-MM";
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter DEFAULT_YEAR_FORMATTER = DateTimeFormatter
            .ofPattern(DEFAULT_YEAR_PATTERN);
    public static final DateTimeFormatter DEFAULT_YEAR_MONTH_FORMATTER = DateTimeFormatter
            .ofPattern(DEFAULT_YEAR_MONTH_PATTERN);
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter
            .ofPattern(DEFAULT_DATE_PATTERN);
    public static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter
            .ofPattern(DEFAULT_TIME_PATTERN);
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter
            .ofPattern(DEFAULT_DATETIME_PATTERN);

    private static final Map<String, DateTimeFormatter> FORMATTER_MAP = Collections
            .unmodifiableMap(MapUtil.builder(DEFAULT_YEAR_PATTERN, DEFAULT_YEAR_FORMATTER)
                    .put(DEFAULT_YEAR_MONTH_PATTERN, DEFAULT_YEAR_MONTH_FORMATTER)
                    .put(DEFAULT_DATE_PATTERN, DEFAULT_DATE_FORMATTER)
                    .put(DEFAULT_TIME_PATTERN, DEFAULT_TIME_FORMATTER)
                    .put(DEFAULT_DATETIME_PATTERN, DEFAULT_DATETIME_FORMATTER).build());

    public static final String DEFAULT_DATETIME_REGEXP = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|"
            + "[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|"
            + "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})"
            + "(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+"
            + "([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

    public static final String DEFAULT_DATE_REGEXP = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|"
            + "[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|"
            + "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})"
            + "(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))$";

    public static final String DEFAULT_TIME_REGEXP = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

    public static final String DEFAULT_DATETIME_OR_TIME_REGEXP = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|"
            + "[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|"
            + "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})"
            + "(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+"
            + "([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])|([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

    /**
     * yyyy-MM-dd 字符长度
     */
    public static final int DEFAULT_DATE_LENGTH = 10;

    /**
     * HH:mm:ss 字符长度
     */
    public static final int DEFAULT_TIME_LENGTH = 8;

    /**
     * yyyy-MM-dd HH:mm:ss 字符长度
     */
    public static final int DEFAULT_DATETIME_LENGTH = 19;

    /**
     * yyyyMMdd 字符长度
     */
    public static final int DAY_STRING_LENGTH = 8;

    /**
     * yyyyMMddHH 字符长度
     */
    public static final int HOUR_STRING_LENGTH = 10;

    /**
     * yyyyMMddHHmm 字符长度
     */
    public static final int MINUTE_STRING_LENGTH = 12;

    private static final Pattern HOUR_MINITE_REGEXP_PATTERN = Pattern
            .compile("^(([0-1]\\d)|(2[0-4])):[0-5]\\d$");
    private static final Pattern YEAR_REGEXP_PATTERN = Pattern.compile("^20\\d\\d$");

    /**
     * parse yyyy-MM-dd to LocalDate
     * @param date
     * @return LocalDate
     */
    public static LocalDate parse2LocalDate(String date) {
        return LocalDate.parse(date, DEFAULT_DATE_FORMATTER);
    }

    /**
     * Parse string to LocalDate
     * @param date the date
     * @param formatter the pattern
     * @return LocalDate
     */
    public static LocalDate parse2LocalDate(String date, DateTimeFormatter formatter) {
        return LocalDate.parse(date, formatter);
    }

    /**
     * Parse HH:mm:ss to LocalTime
     * @param time the time
     * @return the LocalTime
     */
    public static LocalTime parse2LocalTime(String time) {
        return parse2LocalTime(time, DEFAULT_TIME_FORMATTER);
    }

    /**
     * Parse string to date LocalTime
     * @param time the time
     * @param formatter the pattern
     * @return LocalTime
     */
    public static LocalTime parse2LocalTime(String time, DateTimeFormatter formatter) {
        return LocalTime.parse(time, formatter);
    }

    /**
     * Parse yyyy-MM-dd HH:mm:ss to LocalDateTime
     * @param datetime the datetime
     * @return LocalDateTime
     */
    public static LocalDateTime parse2LocalDateTime(String datetime) {
        return parse2LocalDateTime(datetime, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * Parse string to LocalDateTime
     * @param datetime the datetime
     * @param formatter the pattern
     * @return LocalDateTime
     */
    public static LocalDateTime parse2LocalDateTime(String datetime, DateTimeFormatter formatter) {
        return LocalDateTime.parse(datetime, formatter);
    }

    /**
     * Parse local date local date time.
     * @param time the time
     * @param pattern the pattern
     * @return the local date time
     */
    public static LocalDateTime parse2LocalDateTime(String time, String pattern) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     *
     * @param offset
     * @param df
     * @return
     */
    public static String parseSec2Str(long sec, ZoneOffset offset, DateTimeFormatter df) {
        return formatLocalDateTime(LocalDateTime.ofEpochSecond(sec, 0, offset), df);
    }

    /**
     * format LocalDateTime to yyyy-MM-dd HH:mm:ss
     * @param dateTime
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return formatLocalDateTime(dateTime, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 
     * @param dateTime
     * @param df
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime dateTime, DateTimeFormatter df) {
        return df.format(dateTime);
    }

    /**
     * format LocalDate to yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formatLocalDate(LocalDate date) {
        return formatLocalDate(date, DEFAULT_DATE_FORMATTER);
    }

    /**
     * 
     * @param date
     * @param df
     * @return
     */
    public static String formatLocalDate(LocalDate date, DateTimeFormatter df) {
        return df.format(date);
    }

    /**
     * format LocalTime to HH:mm:ss
     * @param time
     * @return
     */
    public static String formatLocalTime(LocalTime time) {
        return formatLocalTime(time, DEFAULT_TIME_FORMATTER);
    }

    /**
     * 
     * @param time
     * @param df
     * @return
     */
    public static String formatLocalTime(LocalTime time, DateTimeFormatter df) {
        return df.format(time);
    }

    /**
     * 按传入时间模板 进行格式化
     * @param date 本地日期
     * @param format 格式化模板
     * @return
     */
    public static String formatLocalDate(LocalDate date, String format) {
        return DateTimeFormatter.ofPattern(format).format(date);
    }

    /**
     * 对之前本地日期 进行格式化输出
     * @param daysToSubtract 往前的天数
     * @param format 格式化模板
     * @return
     */
    public static String formatMinusNowLocalDateFormat(long daysToSubtract, String format) {
        return formatLocalDate(minusNowLocalDate(daysToSubtract), format);
    }

    /**
     * 获取当前日期的 前面天数的时间
     * @param daysToSubtract 往前的天数，正数表示之前 过去的时间
     * @return
     */
    public static LocalDate minusNowLocalDate(long daysToSubtract) {
        return LocalDate.now().minusDays(daysToSubtract);
    }

    /**
     * Is valid time boolean.
     * @param hourMinite in format hh:mm
     * @return boolean boolean
     */
    public static boolean isValidTime(String hourMinite) {
        return HOUR_MINITE_REGEXP_PATTERN.matcher(hourMinite).matches();
    }

    /**
     * Is valid year boolean.
     * @param year the year
     * @return the boolean
     */
    public static boolean isValidYear(String year) {
        return YEAR_REGEXP_PATTERN.matcher(year).matches();
    }

    /**
     * get the interval minutes of two days
     * @param date1
     * @param date2
     * @return
     */
    public static long getMinutesMinusof2Date(LocalDateTime date1, LocalDateTime date2) {
        return date1.toEpochSecond(ZonedDateTime.now().getOffset())
                - date2.toEpochSecond(ZonedDateTime.now().getOffset());
    }

    /**
     * parse LocalDate to string, yyyy-MM-dd 00:00:00
     * @param date
     * @return
     */
    public static String getBeginnigOfDay(LocalDate date) {
        return formatLocalDateTime(LocalDateTime.of(date, LocalTime.MIN),
                DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * parse LocalDate to string, yyyy-MM-dd 23:59:59
     * @param date
     * @return
     */
    public static String getEndingOfDay(LocalDate date) {
        return formatLocalDateTime(LocalDateTime.of(date, LocalTime.MAX),
                DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * parse string to unix timestamp
     * @param date
     * @return
     */
    public static long getTime(String date, DateTimeFormatter formatter) {
        return getTime(parse2LocalDateTime(date, formatter));
    }

    /**
     * parse string(yyyy-MM-dd HH:mm:ss) to unix timestamp
     * @param date
     * @return
     */
    public static long getTime(String date) {
        return getTime(parse2LocalDateTime(date, DEFAULT_DATETIME_FORMATTER));
    }

    /**
     * parse localDateTime to unix timestamp
     * @param localDateTime
     * @return
     */
    public static long getTime(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 
     * @param time
     * @return
     */
    public static Date toDate(LocalDateTime time) {
        return new Date(time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }

    /**
     * 
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofEpochSecond(date.getTime() / 1000L, 0, ZoneOffset.of("+8"));
    }

    /**
     * 获取start和end之间的所有日期时间
     * 
     * @param start        开始时间
     * @param end          结束时间
     * @param includeStart 是否包含开始
     * @param includeEnd   是否包含结束
     * @param patternIn    入参日期格式
     * @param patternOut   出参日期格式
     * @param unit         跨度单位
     * @return
     */
    public static List<String> getAllTemporals(String start, String end, ChronoUnit unit,
                                               boolean includeStart, boolean includeEnd,
                                               String patternIn, String patternOut) {
        LocalDateTime startTemporal;
        LocalDateTime endTemporal;
        DateTimeFormatter formatterIn = FORMATTER_MAP.getOrDefault(patternIn,
                DateTimeFormatter.ofPattern(patternIn));
        if (unit.getDuration().compareTo(ChronoUnit.DAYS.getDuration()) < 0) {
            startTemporal = parse2LocalDateTime(start, formatterIn);
            endTemporal = parse2LocalDateTime(end, formatterIn);
        } else {
            startTemporal = LocalDateTime.of(parse2LocalDate(start, formatterIn), LocalTime.MIN);
            endTemporal = LocalDateTime.of(parse2LocalDate(end, formatterIn), LocalTime.MIN);
        }

        List<String> dates = new ArrayList<>();
        if (!startTemporal.isAfter(endTemporal)) {
            DateTimeFormatter formatterOut = FORMATTER_MAP.getOrDefault(patternOut,
                    DateTimeFormatter.ofPattern(patternOut));
            if (includeStart) {
                dates.add(formatLocalDateTime(startTemporal, formatterOut));
            }

            long minus = unit.between(startTemporal, endTemporal);
            for (int i = 0; i < minus - 1; i++) {
                LocalDateTime next = null;
                switch (unit) {
                    case YEARS:
                        next = startTemporal.plusYears(i + 1);
                        break;
                    case MONTHS:
                        next = startTemporal.plusMonths(i + 1);
                        break;
                    case DAYS:
                        next = startTemporal.plusDays(i + 1);
                        break;
                    case HOURS:
                        next = startTemporal.plusHours(i + 1);
                        break;
                    case MINUTES:
                        next = startTemporal.plusMinutes(i + 1);
                        break;
                    case SECONDS:
                        next = startTemporal.plusSeconds(i + 1);
                        break;
                    default:
                        throw new UnsupportedOperationException(
                                "Contact the developer and evaluate whether it will be supported in next version.");
                }
                dates.add(formatLocalDateTime(next, formatterOut));
            }

            if (includeEnd) {
                String endStr = formatLocalDateTime(endTemporal, formatterOut);
                if (CollectionUtils.isEmpty(dates) || !endStr.equals(dates.get(0))) {
                    dates.add(endStr);
                }
            }
        }
        return dates;
    }

    /**
     * 获取start和end之间的所有日期（默认DEFAULT_DATE_PATTERN），包含首尾
     * @param start
     * @param end
     * @return
     */
    public static List<String> getAllDates(String start, String end) {
        return getAllDates(start, end, true, true);
    }

    /**
     *  获取start和end之间的所有日期（默认DEFAULT_DATE_PATTERN）
     * @param start
     * @param end
     * @param includeStart
     * @param includeEnd
     * @return
     */
    public static List<String> getAllDates(String start, String end, boolean includeStart,
                                           boolean includeEnd) {
        return getAllDates(start, end, includeStart, includeEnd, DEFAULT_DATE_PATTERN,
                DEFAULT_DATE_PATTERN);
    }

    /**
     * 获取start和end之间的所有日期
     * @param start
     * @param end
     * @param includeStart
     * @param includeEnd
     * @param patternIn
     * @param patternOut
     * @return
     */
    public static List<String> getAllDates(String start, String end, boolean includeStart,
                                           boolean includeEnd, String patternIn,
                                           String patternOut) {
        return getAllTemporals(start, end, ChronoUnit.DAYS, includeStart, includeEnd, patternIn,
                patternOut);
    }

    /**
     * 获取start和end之间的所有月份（入参默认DEFAULT_DATE_PATTERN，出参默认DEFAULT_YEAR_MONTH_PATTERN），包含首尾
     * @param start
     * @param end
     * @return
     */
    public static List<String> getAllMonths(String start, String end) {
        return getAllMonths(start, end, true, true);
    }

    /**
     *  获取start和end之间的所有月份（入参默认DEFAULT_DATE_PATTERN，出参默认DEFAULT_YEAR_MONTH_PATTERN）
     * @param start
     * @param end
     * @param includeStart
     * @param includeEnd
     * @return
     */
    public static List<String> getAllMonths(String start, String end, boolean includeStart,
                                            boolean includeEnd) {
        return getAllMonths(start, end, includeStart, includeEnd, DEFAULT_DATE_PATTERN,
                DEFAULT_YEAR_MONTH_PATTERN);
    }

    /**
     * 获取start和end之间的所有月份
     * @param start
     * @param end
     * @param includeStart
     * @param includeEnd
     * @param patternIn
     * @param patternOut
     * @return
     */
    public static List<String> getAllMonths(String start, String end, boolean includeStart,
                                            boolean includeEnd, String patternIn,
                                            String patternOut) {
        return getAllTemporals(start, end, ChronoUnit.MONTHS, includeStart, includeEnd, patternIn,
                patternOut);
    }

    /**
     * 获取start和end之间的所有年份（入参默认DEFAULT_DATE_PATTERN，出参默认DEFAULT_YEAR_PATTERN），包含首尾
     * @param start
     * @param end
     * @return
     */
    public static List<String> getAllYears(String start, String end) {
        return getAllYears(start, end, true, true);
    }

    /**
     *  获取start和end之间的所有年份（入参默认DEFAULT_DATE_PATTERN，出参默认DEFAULT_YEAR_PATTERN）
     * @param start
     * @param end
     * @param includeStart
     * @param includeEnd
     * @return
     */
    public static List<String> getAllYears(String start, String end, boolean includeStart,
                                           boolean includeEnd) {
        return getAllYears(start, end, includeStart, includeEnd, DEFAULT_DATE_PATTERN,
                DEFAULT_YEAR_PATTERN);
    }

    /**
     * 获取start和end之间的所有年份
     * @param start
     * @param end
     * @param includeStart
     * @param includeEnd
     * @param patternIn
     * @param patternOut
     * @return
     */
    public static List<String> getAllYears(String start, String end, boolean includeStart,
                                           boolean includeEnd, String patternIn,
                                           String patternOut) {
        return getAllTemporals(start, end, ChronoUnit.YEARS, includeStart, includeEnd, patternIn,
                patternOut);
    }

    /**
     * 判断时间[start1, end1]是否与[start2, end2]重叠
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return
     */
    public static boolean isOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2,
                                    LocalDateTime end2) {
        return !(end1.isBefore(start2) || start1.isAfter(end2));
    }

    public static void main(String[] args) {
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-01", ChronoUnit.DAYS,
                        false, false, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-01", ChronoUnit.DAYS,
                        true, false, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-01", ChronoUnit.DAYS,
                        false, true, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-01", ChronoUnit.DAYS,
                        true, true, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));

        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-02", ChronoUnit.DAYS,
                        false, false, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-02", ChronoUnit.DAYS,
                        true, false, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-02", ChronoUnit.DAYS,
                        false, true, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-02", ChronoUnit.DAYS,
                        true, true, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));

        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-21", "2021-11-11", ChronoUnit.DAYS,
                        false, false, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-21", "2021-11-11", ChronoUnit.DAYS,
                        true, false, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-21", "2021-11-11", ChronoUnit.DAYS,
                        false, true, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-21", "2021-11-11", ChronoUnit.DAYS,
                        true, true, DEFAULT_DATE_PATTERN, DEFAULT_DATE_PATTERN).toArray()));

        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-01", ChronoUnit.MONTHS,
                        false, false, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-01", ChronoUnit.MONTHS,
                        true, false, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-01", ChronoUnit.MONTHS,
                        false, true, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-11-01", "2021-11-01", ChronoUnit.MONTHS,
                        true, true, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));

        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-01", "2021-11-01", ChronoUnit.MONTHS,
                        false, false, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-01", "2021-11-01", ChronoUnit.MONTHS,
                        true, false, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-01", "2021-11-01", ChronoUnit.MONTHS,
                        false, true, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-01", "2021-11-01", ChronoUnit.MONTHS,
                        true, true, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));

        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-01", "2022-11-01", ChronoUnit.MONTHS,
                        false, false, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-01", "2022-11-01", ChronoUnit.MONTHS,
                        true, false, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-01", "2022-11-01", ChronoUnit.MONTHS,
                        false, true, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));
        System.out.println(
                Arrays.deepToString(getAllTemporals("2021-10-01", "2022-11-01", ChronoUnit.MONTHS,
                        true, true, DEFAULT_DATE_PATTERN, DEFAULT_YEAR_MONTH_PATTERN).toArray()));

        System.out.println("2020-02-29 19:24:56".matches(DEFAULT_DATETIME_REGEXP));
        System.out.println("2020-02-29".matches(DEFAULT_DATE_REGEXP));
        System.out.println("00:00:00".matches(DEFAULT_TIME_REGEXP));
        System.out.println("2020-02-29 19:24:56".matches(DEFAULT_DATETIME_OR_TIME_REGEXP));
        System.out.println("2020-02-29".matches(DEFAULT_DATETIME_OR_TIME_REGEXP));
        System.out.println("19:24:56".matches(DEFAULT_DATETIME_OR_TIME_REGEXP));
        System.out.println("2020-02-29 19:24:56".substring(DEFAULT_DATE_LENGTH));
    }
}
