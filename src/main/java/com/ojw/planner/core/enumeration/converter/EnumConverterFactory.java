package com.ojw.planner.core.enumeration.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * code -> enum 변환(Request Parameter에서 작동)
 */
@SuppressWarnings({"rawtypes"})
public class EnumConverterFactory implements ConverterFactory<String, Enum> {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter(targetType);
    }

    private record StringToEnumConverter<T extends Enum<T>>(Class<T> enumType) implements Converter<String, T> {

        @Override
        public T convert(String source) {

            T result = null;

            try {
                //code 값을 통해, 해당 code 값을 가진 enum으로 변환시킴
                result = (T) this.enumType.getDeclaredMethod("ofCode", String.class).invoke(enumType.getEnumConstants()[0], source);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            return result;

        }

    }

}
