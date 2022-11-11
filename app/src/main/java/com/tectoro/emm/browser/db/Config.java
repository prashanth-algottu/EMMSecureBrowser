package com.tectoro.emm.browser.db;

import java.util.List;
import java.util.Map;

public class Config {
    private boolean allowByDefault;
    private List<String> exceptionList;

    public Config(Map<String, Object> config) {
        if (config == null){
            return;
        }

        allowByDefault = (Boolean) config.get("allowByDefault");
        exceptionList = (List) config.get("exceptionList");

    }
    public Config() {

    }

    public boolean isAllowByDefault() {
        return allowByDefault;
    }

    public void setAllowByDefault(boolean allowByDefault) {
        this.allowByDefault = allowByDefault;
    }

    public List<String> getExceptionlist() {
        return exceptionList;
    }

    public void setExceptionlist(List<String> exceptionlist) {
        this.exceptionList = exceptionlist;
    }

    public Config(boolean allowByDefault, List<String> exceptionlist) {
        this.allowByDefault = allowByDefault;
        this.exceptionList = exceptionlist;
    }
}
