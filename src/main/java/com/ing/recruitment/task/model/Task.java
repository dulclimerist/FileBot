package com.ing.recruitment.task.model;

import java.util.List;
import java.util.Objects;

public class Task {
    private String name;
    private List<Matcher> matchers;
    private Action action;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Matcher> getMatchers() {
        return matchers;
    }

    public void setMatchers(List<Matcher> matchers) {
        this.matchers = matchers;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name) && matchers.equals(task.matchers) && action.equals(task.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, matchers, action);
    }
}
