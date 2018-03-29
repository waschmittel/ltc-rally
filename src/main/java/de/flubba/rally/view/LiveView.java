package de.flubba.rally.view;

import java.util.LinkedList;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import de.flubba.rally.LapBroadcaster;
import de.flubba.rally.LapBroadcaster.LapBroadcastListener;
import de.flubba.rally.entity.Runner;

@SpringView(name = LiveView.VIEW_NAME)
public class LiveView extends LiveViewDesign implements View, LapBroadcastListener {
    public static final String VIEW_NAME = "live";
    private LinkedList<Label>  recent    = new LinkedList<>();

    public void addRunner(String name) {
        Label newLabel = new Label(name);
        if (recent.size() >= 5) {
            removeComponent(recent.pop());
        }
        addComponent(newLabel);
        recent.add(newLabel);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        LapBroadcaster.register(this);
    }

    @Override
    public void beforeLeave(ViewBeforeLeaveEvent event) {
        LapBroadcaster.unregister(this);
        View.super.beforeLeave(event);
    }

    @Override
    public void receiveBroadcast(Runner runner, long lapTime) {
        UI.getCurrent().access(() -> {
            addRunner(String.format("%s - %s - %s", runner.getId(), lapTime, runner.getName()));
        });
    }

}
