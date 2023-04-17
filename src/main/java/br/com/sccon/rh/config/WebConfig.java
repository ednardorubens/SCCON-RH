package br.com.sccon.rh.config;

import br.com.sccon.rh.enums.AgeOutputEnum;
import br.com.sccon.rh.enums.SalaryOutputEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(ageOutputConverter());
        registry.addConverter(salaryOutputConverter());
    }

    private Converter<String, AgeOutputEnum> ageOutputConverter() {
        return new Converter<String, AgeOutputEnum>() {

            @Override
            @Nullable
            public AgeOutputEnum convert(String source) {
                return AgeOutputEnum.valueOf(source.toUpperCase());
            }

        };
    }

    private Converter<String, SalaryOutputEnum> salaryOutputConverter() {
        return new Converter<String, SalaryOutputEnum>() {

            @Override
            @Nullable
            public SalaryOutputEnum convert(String source) {
                return SalaryOutputEnum.valueOf(source.toUpperCase());
            }

        };
    }

}
