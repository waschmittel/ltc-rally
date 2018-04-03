package de.flubba.rally.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;

import de.flubba.rally.LapBroadcaster;
import de.flubba.rally.LapBroadcaster.LapBroadcastListener;
import de.flubba.rally.entity.Runner;

@SpringView(name = LiveView.VIEW_NAME)
public class LiveView extends LiveViewDesign implements View, LapBroadcastListener {
    public static final String VIEW_NAME = "live";
    private LinkedList<Label>  recent    = new LinkedList<>();

    public void addRunner(String name) {
        Label newLabel = new Label(name);
        newLabel.addStyleName("liveLap");
        if (recent.size() >= 10) {
            removeComponent(recent.pop());
        }
        addComponentAsFirst(newLabel);
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
        getUI().access(() -> {
            BigDecimal seconds = new BigDecimal(lapTime).divide(new BigDecimal(1000)).setScale(1, RoundingMode.HALF_UP);
            addRunner(String.format("%ss - %s - %s", seconds.toString(), runner.getId(), runner.getName()));
        });
    }

}
