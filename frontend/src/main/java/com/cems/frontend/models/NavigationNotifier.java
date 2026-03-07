package com.cems.frontend.models;

import java.util.ArrayList;
import java.util.List;

public class NavigationNotifier {
    private List<NavigationObserver> observers = new ArrayList<>();
    private static NavigationNotifier instance = null;

    private NavigationNotifier() {}

    public static NavigationNotifier getInstance() {
        if (instance == null) {
            instance = new NavigationNotifier();
        }
        return instance;
    }

    public void notifyAllObservers(Paths path) {
        for (var observer: observers) {
            observer.setPage(path);
        }
    }

    public void addObserver(NavigationObserver observer) {
        observers.add(observer);
    }

    public void removeAllObservers() {
        observers = new ArrayList<>();
    }
}
