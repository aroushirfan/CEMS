package com.cems.frontend.models;

public class NavigationMemento {
    private Paths path;
    private Object payload;

    public NavigationMemento(Paths path, Object payload) {
        this.path = path;
        this.payload = payload;
    }

    public Paths getPath() {
        return path;
    }

    public <T> T getPayload(Class<T> type) {
        return type.isInstance(payload) ? type.cast(payload) : null;
    }
}
