package de.flubba.rally.view;

import javax.annotation.PostConstruct;

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
import de.flubba.rally.entity.Runner;
import de.flubba.rally.entity.Sponsor;

abstract class RunnerViewDesign extends VerticalSplitPanel {
    private HorizontalLayout runnersToolbar  = new HorizontalLayout();
    private HorizontalLayout sponsorsToolbar = new HorizontalLayout();

    private VerticalLayout runnersLayout  = new VerticalLayout();
    private VerticalLayout sponsorsLayout = new VerticalLayout();

    Grid<Runner>  runnersGrid  = new Grid<>(Runner.class);
    Grid<Sponsor> sponsorsGrid = new Grid<>(Sponsor.class);

    Button addRunnerButton  = new Button(I18n.RUNNER_BUTTON_ADD.get(), VaadinIcons.PLUS);
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
        runnersToolbar.setWidth("100%");
        runnersToolbar.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addRunnerButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        runnersToolbar.addComponent(addRunnerButton);
    }

    private void initRunnersGrid() {
        runnersGrid.setSizeFull();
        runnersGrid.removeColumn("numberOfSponsors");
        runnersGrid.removeColumn("laps");
        runnersGrid.removeColumn("lapCount");
        runnersGrid.removeColumn("average");
        runnersGrid.removeColumn("donations");
        runnersGrid.removeColumn("sponsors");
        runnersGrid.setColumnOrder("id", "name", "gender");
        runnersGrid.setSelectionMode(SelectionMode.SINGLE);
    }

    private void initSponsorsLayout() {
        initSponsorsGrid();
        initSponsorsToolbar();

        sponsorsLayout.addComponent(sponsorsToolbar);
        sponsorsLayout.addComponentsAndExpand(sponsorsGrid);
    }

    private void initSponsorsToolbar() {
        sponsorsToolbar.setWidth("100%");
        sponsorsToolbar.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addSponsorButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        sponsorsToolbar.addComponent(addSponsorButton);
        addSponsorButton.setEnabled(false);
    }

    private void initSponsorsGrid() {
        sponsorsGrid.setSizeFull();
        sponsorsGrid.removeColumn("runner");
        sponsorsGrid.removeColumn("id");
        sponsorsGrid.setColumnOrder("name", "street", "city", "country", "perLapDonation", "oneTimeDonation", "receipt");
        sponsorsGrid.setSelectionMode(SelectionMode.NONE);
    }

}
