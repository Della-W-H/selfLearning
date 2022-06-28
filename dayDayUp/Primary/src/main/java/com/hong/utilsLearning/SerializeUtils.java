package com.hong.utilsLearning;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.RawSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TimeZone;

public class SerializeUtils {

    private static final ObjectMapper GLOBAL_OBJECTMAPPER = createObjectMapper();

    private static final ObjectMapper REDIS_OBJECTMAPPER = createRedisObjectMapper();

    private static final ObjectMapper UNQUOTE_OBJECTMAPPER = createUnquoteObjectMapper();

    /**
     * 
     * @return
     */
    public static ObjectMapper createNonConfigObjectMapper() {
        return new ObjectMapper();
    }

    /**
     * 
     * @return
     */
    public static ObjectMapper getGlobalObjectMapper() {
        return GLOBAL_OBJECTMAPPER;
    }

    /**
     * 
     * @return
     */
    public static ObjectMapper getRedisObjectMapper() {
        return REDIS_OBJECTMAPPER;
    }

    /**
     * 
     * @return
     */
    public static ObjectMapper getUnquoteObjectMapper() {
        return UNQUOTE_OBJECTMAPPER;
    }

    /**
     * 
     * @return
     */
    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = createNonConfigObjectMapper();
        configObjectMapper(mapper);
        return mapper;
    }

    /**
     * 
     * @return
     */
    public static ObjectMapper createRedisObjectMapper() {
        ObjectMapper mapper = createObjectMapper();
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL);
        return mapper;
    }

    /**
     * 
     * @return
     */
    public static ObjectMapper createUnquoteObjectMapper() {
        ObjectMapper mapper = createObjectMapper();
        mapper.configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), false);
        mapper.configure(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature(), true);
        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(String.class, new UnquoteRawSerializer<String>(String.class));
        mapper.registerModule(simpleModule);
        return mapper;
    }

    /**
     * 
     * @param mapper
     */
    @SuppressWarnings("deprecation")
    public static void configObjectMapper(ObjectMapper mapper) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat(LocalDateTimeUtils.DEFAULT_DATETIME_PATTERN));
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(LocalDateTimeUtils.DEFAULT_DATETIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(LocalDateTimeUtils.DEFAULT_DATETIME_FORMATTER));
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(LocalDateTimeUtils.DEFAULT_DATE_FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(LocalDateTimeUtils.DEFAULT_DATE_FORMATTER));
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(LocalDateTimeUtils.DEFAULT_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(LocalDateTimeUtils.DEFAULT_TIME_FORMATTER));
        mapper.registerModule(javaTimeModule);

        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        mapper.registerModule(simpleModule);
    }

    /**
     * 
     * @author Administrator
     *
     * @param <T>
     */
    private static class UnquoteRawSerializer<T> extends RawSerializer<T> {

        /**
         * 
         */
        private static final long serialVersionUID = 8155264759131118309L;

        public UnquoteRawSerializer(Class<T> cls) {
            super(cls);
        }

        @Override
        public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {
            jgen.writeRawValue(value.toString());
        }
    }
}

