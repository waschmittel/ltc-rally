package de.flubba.rally.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.flubba.generated.i18n.I18n;
import de.flubba.rally.component.RunnersGrid;

class ResultsViewDesign extends VerticalLayout {
    private HorizontalLayout toolbarLayout = new HorizontalLayout();

    Button refreshButton   = new Button(I18n.RESULTS_REFRESH.get(), VaadinIcons.REFRESH);
    Button calculateButton = new Button(I18n.RESULTS_CALCULATE.get(), VaadinIcons.TROPHY);

    @Autowired
    RunnersGrid runnersGrid;

    @PostConstruct
    private void init() {
        toolbarLayout.addComponents(refreshButton, calculateButton);

        runnersGrid.removeColumn("roomNumber");
        runnersGrid.setSelectionMode(SelectionMode.NONE);

        addComponent(toolbarLayout);
        addComponentsAndExpand(runnersGrid);
    }
}
