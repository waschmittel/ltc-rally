package de.flubba.rally.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

import de.flubba.generated.i18n.I18n;
import de.flubba.rally.component.RunnersGrid;
import de.flubba.rally.entity.Sponsor;

abstract class RunnerViewDesign extends VerticalSplitPanel {
    private HorizontalLayout runnersToolbar  = new HorizontalLayout();
    private HorizontalLayout sponsorsToolbar = new HorizontalLayout();

    private VerticalLayout runnersLayout  = new VerticalLayout();
    private VerticalLayout sponsorsLayout = new VerticalLayout();

    @Autowired
    RunnersGrid   runnersGrid;
    Grid<Sponsor> sponsorsGrid = new Grid<>(Sponsor.class);

    Button addRunnerButton  = new Button(I18n.RUNNER_BUTTON_ADD.get(), VaadinIcons.PLUS);
    Button refreshButton    = new Button(I18n.RUNNER_BUTTON_REFRESH.get(), VaadinIcons.REFRESH);
    Button addSponsorButton = new Button(I18n.SPONSOR_BUTTON_ADD.get(), VaadinIcons.PLUS);

    @PostConstruct
    private void init() {
        initRunnersLayout();
        initSponsorsLayout();

        addStyleName(ValoTheme.SPLITPANEL_LARGE);
        setFirstComponent(runnersLayout);
        setSecondComponent(sponsorsLayout);
    }

    private void initRunnersLayout() {
        initRunnersGrid();
        initRunnersToolbar();

        runnersLayout.addComponent(runnersToolbar);
        runnersLayout.addComponentsAndExpand(runnersGrid);
    }

    private void initRunnersToolbar() {
        runnersToolbar.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        addRunnerButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        runnersToolbar.addComponents(addRunnerButton, refreshButton);
    }

    private void initRunnersGrid() {
        runnersGrid.removeColumn("bonusLaps");
        runnersGrid.removeColumn("donations");
        runnersGrid.removeColumn("numberOfSponsors");
        runnersGrid.removeColumn("average");
    }

    private void initSponsorsLayout() {
        initSponsorsGrid();
        initSponsorsToolbar();

        sponsorsLayout.addComponent(sponsorsToolbar);
        sponsorsLayout.addComponentsAndExpand(sponsorsGrid);
    }

    private void initSponsorsToolbar() {
        sponsorsToolbar.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        addSponsorButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        sponsorsToolbar.addComponent(addSponsorButton);
        addSponsorButton.setEnabled(false);
    }

    private void initSponsorsGrid() {
        sponsorsGrid.setSizeFull();
        sponsorsGrid.removeColumn("runner");
        sponsorsGrid.removeColumn("id");
        sponsorsGrid.removeColumn("totalDonation");
        sponsorsGrid.getColumn("name").setResizable(false).setExpandRatio(1);
        sponsorsGrid.getColumn("street").setResizable(false).setExpandRatio(1);
        sponsorsGrid.getColumn("city").setResizable(false).setExpandRatio(1);
        sponsorsGrid.getColumn("country").setResizable(false).setExpandRatio(1);
        sponsorsGrid.getColumn("perLapDonation").setResizable(false).setWidth(100);
        sponsorsGrid.getColumn("oneTimeDonation").setResizable(false).setWidth(100);
        sponsorsGrid.setColumnOrder("name", "street", "city", "country", "perLapDonation", "oneTimeDonation");
        sponsorsGrid.setSelectionMode(SelectionMode.NONE);
    }

}
