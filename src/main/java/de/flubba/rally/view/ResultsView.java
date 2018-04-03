package de.flubba.rally.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;

import de.flubba.rally.RallyUI;
import de.flubba.rally.component.RunnerEditForm;
import de.flubba.rally.entity.Runner;
import de.flubba.rally.entity.repository.RunnerRepository;
import de.flubba.rally.service.RaceResultsService;
import de.flubba.util.vaadin.EditDeleteButtonsProvider;

@SpringView(name = ResultsView.VIEW_NAME)
public class ResultsView extends ResultsViewDesign implements View {
    public static final String VIEW_NAME = "results";

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private RaceResultsService raceResultsService;

    @PostConstruct
    private void init() {
        runnersGrid.addComponentColumn(new EditDeleteButtonsProvider<>(this::editRunner));
        calculateButton.addClickListener(e -> {
            raceResultsService.generateResults();
            runnersGrid.refresh();
        });
    }

    public void editRunner(Runner runner) {
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
