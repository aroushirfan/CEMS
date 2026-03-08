package com.cems.frontend.models;

import com.cems.frontend.controllers.pages.NavigationController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public Object notifyAllObservers(Paths path) {
        Object[] val = new Object[] {null};
        for (var observer: observers) {
            if (observer instanceof NavigationController) {
                val[0] = ((NavigationController) observer).setPageReturnController(path);
            }
            observer.setPage(path);
        }
        return val[0];
    }

    public void addObserver(NavigationObserver observer) {
        observers.add(observer);
    }

    public void removeAllObservers() {
        observers = new ArrayList<>();
    }
}
