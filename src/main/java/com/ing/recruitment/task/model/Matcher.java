package com.ing.recruitment.task.model;

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
