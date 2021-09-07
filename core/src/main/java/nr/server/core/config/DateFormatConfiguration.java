package nr.server.core.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class DateFormatConfiguration {
    private static final String dateFormat = "yyyy-MM-dd";
    private static final String datetimeFormat = "yyyy-MM-dd HH:mm:ss.SSSSSS";

//    @Bean
//    public ObjectMapper objectMapper() {
//        return Jackson2ObjectMapperBuilder
//                .json()
//                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                .modules(createJavaTimeModule())
//                //.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
//                .build();
//    }
//
//    private JavaTimeModule createJavaTimeModule() {
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//
//        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
//            @Override
//            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                gen.writeString(value.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern(datetimeFormat)));
//            }
//        });
//        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
//            @Override
//            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//                return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(datetimeFormat));
//            }
//        });
//
//        return javaTimeModule;
//    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
        return jacksonObjectMapperBuilder -> {
            //jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("UTC"));
            jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
            jacksonObjectMapperBuilder.simpleDateFormat(datetimeFormat);

            // LocalDate
            jacksonObjectMapperBuilder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            jacksonObjectMapperBuilder.deserializerByType(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(dateFormat));
                }
            });
            // LocalDateTime
            jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(datetimeFormat)));
            jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(datetimeFormat));
                }
            });

        };
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer2(){
        return jacksonObjectMapperBuilder -> {};
    }
}