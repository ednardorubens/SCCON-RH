package br.com.sccon.rh.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;

@Getter
@RequiredArgsConstructor
public enum AgeOutputEnum {

    DAYS(ChronoUnit.DAYS),
    MONTHS(ChronoUnit.MONTHS),
    YEARS(ChronoUnit.YEARS);

    private final ChronoUnit unit;

}
