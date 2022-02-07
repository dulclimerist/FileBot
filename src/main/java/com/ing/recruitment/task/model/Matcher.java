package com.ing.recruitment.task.model;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Matcher {
    private MatcherRule matcherRule;
    private String param;

    public MatcherRule getMatcherRule() {
        return matcherRule;
    }

    public void setMatcherRule(MatcherRule matcherRule) {
        this.matcherRule = matcherRule;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public boolean match(File file) {
        switch (this.getMatcherRule()) {
            case extensionIsNot:
                return !FilenameUtils.isExtension(file.getName(), this.getParam());
            case extensionIs:
                return FilenameUtils.isExtension(file.getName(), this.getParam());
            case nameContains:
                return file.getName().contains(this.getParam());
            case modifiedDateLessThen:
                long dateInMilli = LocalDate.parse(this.getParam(), DateTimeFormatter.BASIC_ISO_DATE)
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                return file.lastModified() < dateInMilli;
            case modifiedDateGreaterThen:
                long dateInMilli1 = LocalDate.parse(this.getParam(), DateTimeFormatter.BASIC_ISO_DATE)
                        .plusDays(1)
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                return file.lastModified() > dateInMilli1;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matcher matcher = (Matcher) o;
        return matcherRule == matcher.matcherRule && Objects.equals(param, matcher.param);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matcherRule, param);
    }
}
