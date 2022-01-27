package com.ing.recruitment.task.model;

import java.util.Objects;

public class Action {
    private ActionCode actionName;
    private String actionParam;

    public ActionCode getActionName() {
        return actionName;
    }

    public void setActionName(ActionCode actionName) {
        this.actionName = actionName;
    }

    public String getActionParam() {
        return actionParam;
    }

    public void setActionParam(String actionParam) {
        this.actionParam = actionParam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return actionName == action.actionName && Objects.equals(actionParam, action.actionParam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionName, actionParam);
    }
}
