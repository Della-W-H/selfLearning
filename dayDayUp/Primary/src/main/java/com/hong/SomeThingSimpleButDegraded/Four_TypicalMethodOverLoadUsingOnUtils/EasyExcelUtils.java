package com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.excel.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import java.io.InputStream;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
public class EasyExcelUtils {

    private final static int BATCH_COUNT = 1000;

    /**
     * 
     * @param is
     * @param headRowNumber
     * @param sheetNo
     * @param clazz
     * @param service      服务对象
     * @param methodName   参数是List<T>的方法
     * @param bot
     * @param defaultValue 当其中有非null属性时，填充表格导入后的null属性
     */
    public static <T> void read(InputStream is, int headRowNumber, int sheetNo, Class<T> clazz,
                                Object service, String methodName, BaseOperateType bot,
                                T defaultValue) {
        read(is, headRowNumber, sheetNo, clazz,
                new ValidListener<T>(service, methodName, bot, defaultValue));
    }

    /**
     * 
     * @param is
     * @param headRowNumber
     * @param clazz
     * @param service
     * @param methodName
     * @param bot
     * @param defaultValue
     */
    public static <T> void read(InputStream is, int headRowNumber, Class<T> clazz, Object service,
                                String methodName, BaseOperateType bot, T defaultValue) {
        read(is, headRowNumber, 0, clazz,
                new ValidListener<T>(service, methodName, bot, defaultValue));
    }

    /**
     * 
     * @param is
     * @param clazz
     * @param service
     * @param methodName
     * @param bot
     * @param defaultValue
     */
    public static <T> void read(InputStream is, Class<T> clazz, Object service, String methodName,
                                BaseOperateType bot, T defaultValue) {
        read(is, 1, 0, clazz, new ValidListener<T>(service, methodName, bot, defaultValue));
    }

    /**
     * 
     * @param is
     * @param headRowNumber
     * @param clazz
     * @param service
     * @param methodName
     * @param bot
     */
    public static <T> void read(InputStream is, int headRowNumber, Class<T> clazz, Object service,
                                String methodName, BaseOperateType bot) {
        read(is, headRowNumber, 0, clazz, new ValidListener<T>(service, methodName, bot, null));
    }

    /**
     * 
     * @param is
     * @param clazz
     * @param methodName
     * @param bot
     */
    public static <T> void read(InputStream is, Class<T> clazz, Object service, String methodName,
                                BaseOperateType bot) {
        read(is, 1, 0, clazz, new ValidListener<T>(service, methodName, bot, null));
    }

    /**
     * 
     * @param is
     * @param headRowNumber
     * @param sheetNo
     * @param clazz
     * @param listener
     */
    public static <T> void read(InputStream is, int headRowNumber, int sheetNo, Class<T> clazz,
                                AnalysisEventListener<T> listener) {
        EasyExcel.read(is, clazz, listener).headRowNumber(headRowNumber).sheet(sheetNo).doRead();
    }

    /**
     * 
     * @author Administrator
     *
     * @param <T>
     */
    static class ValidListener<T> extends AnalysisEventListener<T> {

        private List<T> list = new ArrayList<>();

        private BaseOperateType bot;
        private Object service;
        private String methodName;
        private T t;

        public ValidListener(Object service, String methodName, BaseOperateType bot, T t) {
            this.service = service;
            this.methodName = methodName;
            this.bot = bot;
            this.t = t;
        }

        @Override
        public void invoke(T data, AnalysisContext context) {
            valid(data, bot);

            if (t != null) {
                Field[] fields = t.getClass().getDeclaredFields();
                for (Field field : fields) {
                    ReflectionUtils.makeAccessible(field);
                    if (!Modifier.isFinal(field.getModifiers())
                            && !Modifier.isStatic(field.getModifiers())) {
                        Object fieldValue = null;
                        boolean needSet = false;
                        try {
                            fieldValue = field.get(t);
                            needSet = (fieldValue != null && field.get(data) == null);
                        } catch (IllegalAccessException e) {
                            throw new TransformationException("获取值失败！");
                        }

                        if (needSet) {
                            try {
                                field.set(data, fieldValue);
                            } catch (IllegalArgumentException | IllegalAccessException e) {
                                throw new TransformationException("设置值失败！");
                            }
                        }
                    }
                }
            }
            list.add(data);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止过多数据在内存，容易OOM
            if (list.size() >= BATCH_COUNT) {
                this.execute();
                // 存储完成清理 list
                list.clear();
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            this.execute();
        }

        private void execute() {
            try {
                Class<?> beanClass = service.getClass();
                Method method = beanClass.getMethod(methodName, List.class);
                method.invoke(service, list);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.warn("import error:", e);
                throw new TransformationException("导入执行保存失败");
            }
        }

        @Override
        public void onException(Exception exception, AnalysisContext context) throws Exception {
            if (exception instanceof ExcelDataConvertException) {
                Integer columnIndex = ((ExcelDataConvertException) exception).getColumnIndex() + 1;
                Integer rowIndex = ((ExcelDataConvertException) exception).getRowIndex() + 1;
                String message = String.format("第%s行，第%s列数据格式有误，请核实", rowIndex, columnIndex);
                throw new ValidationException(message);
            } else if (exception instanceof RuntimeException) {
                Integer rowIndex = context.readRowHolder().getRowIndex();
                String message = String.format("第%s行数据，%s", rowIndex, exception.getMessage());
                throw new ValidationException(message);
            } else {
                super.onException(exception, context);
            }
        }
    }

    /**
     * 
     * @param obj
     */
    public static void valid(Object obj) {
        valid(obj, null);
    }

    /**
     * 
     * @param obj
     * @param bot
     */
    public static void valid(Object obj, BaseOperateType bot) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
            if (field.isAnnotationPresent(ExcelProperty.class)
                    && !Modifier.isFinal(field.getModifiers())
                    && !Modifier.isStatic(field.getModifiers())) {
                Object fieldValue = null;
                try {
                    fieldValue = field.get(obj);
                } catch (IllegalAccessException e) {
                    throw new TransformationException("获取值失败！");
                }

                // 是否包含必填校验注解
                boolean isExcelNotNull = field.isAnnotationPresent(ExcelNotNull.class);
                ExcelNotNull excelNotNull = field.getAnnotation(ExcelNotNull.class);
                if (isExcelNotNull
                        && (Objects.isNull(bot)
                                || ArrayUtils.contains(excelNotNull.operateType(), bot))
                        && Objects.isNull(fieldValue)) {
                    throw new ValidationException(excelNotNull.message());
                }

                // 是否包含字符串长度校验注解
                boolean isExcelLength = field.isAnnotationPresent(ExcelLength.class);
                ExcelLength excelLength = field.getAnnotation(ExcelLength.class);
                if (isExcelLength
                        && (Objects.isNull(bot)
                                || ArrayUtils.contains(excelLength.operateType(), bot))
                        && !Objects.isNull(fieldValue)) {
                    String cellStr = fieldValue.toString();
                    int length = excelLength.length();
                    if (StringUtils.hasLength(cellStr) && cellStr.length() > length) {
                        throw new ValidationException(excelLength.message());
                    }
                }

                // 是否包含字符串正则校验注解
                boolean isExcelPattern = field.isAnnotationPresent(ExcelPattern.class);
                ExcelPattern excelPattern = field.getAnnotation(ExcelPattern.class);
                if (isExcelPattern
                        && (Objects.isNull(bot)
                                || ArrayUtils.contains(excelPattern.operateType(), bot))
                        && !Objects.isNull(fieldValue)) {
                    String cellStr = fieldValue.toString();
                    ExcelPattern.Flag[] flags = excelPattern.flags();
                    int intFlag = 0;
                    for (ExcelPattern.Flag flag : flags) {
                        intFlag = intFlag | flag.getValue();
                    }

                    try {
                        Pattern pattern = Pattern.compile(excelPattern.regexp(), intFlag);
                        if (!pattern.matcher(cellStr).matches()) {
                            throw new ValidationException(excelPattern.message());
                        }
                    } catch (PatternSyntaxException e) {
                        throw new ValidationException("Invalid regular expression.");
                    }
                }

                // 是否包含整数类型校验注解
                boolean isExcelRange = field.isAnnotationPresent(ExcelRange.class);
                ExcelRange excelRange = field.getAnnotation(ExcelRange.class);
                if (isExcelRange
                        && (Objects.isNull(bot)
                                || ArrayUtils.contains(excelRange.operateType(), bot))
                        && !Objects.isNull(fieldValue)) {
                    if (fieldValue instanceof Integer || fieldValue instanceof Long
                            || fieldValue instanceof Short || fieldValue instanceof Byte) {
                        long number = Long.parseLong(fieldValue.toString());
                        long min = excelRange.min();
                        long max = excelRange.max();
                        if (number < min || number > max) {
                            throw new ValidationException(excelRange.message());
                        }
                    }
                }

                // 是否包含decimal类型注解
                boolean isExcelDecimal = field.isAnnotationPresent(ExcelDecimal.class);
                ExcelDecimal excelDecimal = field.getAnnotation(ExcelDecimal.class);
                if (isExcelDecimal
                        && (Objects.isNull(bot)
                                || ArrayUtils.contains(excelDecimal.operateType(), bot))
                        && !Objects.isNull(fieldValue)) {
                    try {
                        BigDecimal cellDecimal = new BigDecimal(fieldValue.toString());
                        BigDecimal min = new BigDecimal(excelDecimal.min());
                        BigDecimal max = new BigDecimal(excelDecimal.max());
                        if (cellDecimal.compareTo(min) < 0 || cellDecimal.compareTo(max) > 0) {
                            throw new ValidationException(excelDecimal.message());
                        }
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid decimal.");
                    }
                }

                // 是否包含枚举类型注解
                boolean isExcelEnum = field.isAnnotationPresent(ExcelEnum.class);
                ExcelEnum excelEnum = field.getAnnotation(ExcelEnum.class);
                if (isExcelEnum
                        && (Objects.isNull(bot)
                                || ArrayUtils.contains(excelEnum.operateType(), bot))
                        && !Objects.isNull(fieldValue)) {
                    Class<? extends Enum<?>> enumClass = excelEnum.enumClass();
                    if (!com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils.excel.ExcelEnum.Empty.class.equals(enumClass)) {
                        try {
                            Method method = enumClass.getDeclaredMethod(excelEnum.descMethod());
                            boolean enumMatch = false;
                            for (Enum<?> e : enumClass.getEnumConstants()) {
                                if (fieldValue.toString().equals(method.invoke(e))) {
                                    enumMatch = true;
                                    break;
                                }
                            }
                            if (!enumMatch) {
                                throw new ValidationException(excelEnum.message());
                            }
                        } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                                | IllegalArgumentException | InvocationTargetException e) {
                            throw new TransformationException("Invalid Enumeration.");
                        }
                    } else {
                        if (!ArrayUtils.contains(excelEnum.value(), fieldValue.toString())) {
                            throw new ValidationException(excelEnum.message());
                        }
                    }
                }
            }
        }
    }
}


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@interface ExcelNotNull {

    String message() default "can not be null.";

    BaseOperateType[] operateType() default {};
}

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@interface EnumId {

}

class TransformationException extends RuntimeException {

    private static final long serialVersionUID = -179954557182974550L;

    public TransformationException() {
        super();
    }

    public TransformationException(String message) {
        super(message);
    }
}

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@interface ExcelEnum {

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