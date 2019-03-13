package de.flubba.rally.view;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import de.flubba.rally.RallyUI;
import de.flubba.rally.component.RunnerEditForm;
import de.flubba.rally.entity.Runner;
import de.flubba.rally.entity.repository.RunnerRepository;
import de.flubba.rally.service.RaceResultsService;
import de.flubba.util.vaadin.EditDeleteButtonsProvider;

import javax.annotation.PostConstruct;

@SpringView(name = ResultsView.VIEW_NAME)
public class ResultsView extends ResultsViewDesign implements View {
    public static final String VIEW_NAME = "results";

    private final RunnerRepository runnerRepository;

    private final RaceResultsService raceResultsService;

    public ResultsView(RunnerRepository runnerRepository, RaceResultsService raceResultsService) {
        this.runnerRepository = runnerRepository;
        this.raceResultsService = raceResultsService;
    }

    @PostConstruct
    private void init() {
        runnersGrid.addComponentColumn(new EditDeleteButtonsProvider<>(this::editRunner));
        calculateButton.addClickListener(e -> {
            raceResultsService.generateResults();
            runnersGrid.refresh();
        });
    }

    private void editRunner(Runner runner) {
        RunnerEditForm runnerEditForm = new RunnerEditForm(runner);
        runnerEditForm.showResultFields();
        runnerEditForm.openInModalPopup();
        runnerEditForm.setSavedHandler(entity -> saveRunner(runner));
        runnerEditForm.setResetHandler(editedServer -> {
            runnersGrid.refresh();
            RallyUI.closeWindows();
        });
    }

    private void saveRunner(Runner runner) {
        runnersGrid.selectRunner(runnerRepository.saveAndFlush(runner));
        RallyUI.closeWindows();
    }
}
