package de.flubba.rally.view;

import java.math.BigDecimal;
import java.util.LinkedList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;

import de.flubba.generated.i18n.I18n;
import de.flubba.rally.RallyUI;
import de.flubba.rally.component.RunnerEditForm;
import de.flubba.rally.component.SponsorEditForm;
import de.flubba.rally.entity.Runner;
import de.flubba.rally.entity.Sponsor;
import de.flubba.rally.entity.repository.RunnerRepository;
import de.flubba.rally.entity.repository.SponsorRepository;
import de.flubba.util.vaadin.EditDeleteButtonsProvider;
import de.flubba.util.vaadin.dialog.ConfirmDialog;

@SpringView(name = RunnerView.VIEW_NAME)
public class RunnerView extends RunnerViewDesign implements View {
    public static final String VIEW_NAME = "";

    private Runner selectedRunner = null;

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Value("${de.flubba.rally.shekel-euro-rate}")
    private BigDecimal shekelToEuro;

    @PostConstruct
    public void init() {
        runnersGrid.addSelectionListener(event -> {
            if (!event.getFirstSelectedItem().isPresent()) {
                showNoSponsors();
            }
            event.getFirstSelectedItem().ifPresent(this::showSponsorsFor);
        });

        addRunnerButton.addClickListener(event -> editRunner(new Runner()));

        addSponsorButton.addClickListener(event -> {
            Sponsor newSponsor = new Sponsor();
            newSponsor.setRunner(selectedRunner);
            editSponsor(newSponsor);
        });

        sponsorsGrid.addComponentColumn(new EditDeleteButtonsProvider<>(this::editSponsor, this::confirmDeleteSponsor));
        runnersGrid.addComponentColumn(new EditDeleteButtonsProvider<>(this::editRunner));

        updateRunnerGrid();
    }

    private void showNoSponsors() {
        addSponsorButton.setEnabled(false);
        sponsorsGrid.setDataProvider(new ListDataProvider<>(new LinkedList<>()));
    }

    private void showSponsorsFor(Runner runner) {
        selectedRunner = runner;
        addSponsorButton.setEnabled(true);
        sponsorsGrid.setDataProvider(new ListDataProvider<>(sponsorRepository.findAllByRunner(runner)));
    }

    public void editRunner(Runner runner) {
        RunnerEditForm runnerEditForm = new RunnerEditForm(runner);
        runnerEditForm.openInModalPopup();
        runnerEditForm.setSavedHandler(entity -> saveRunner(runner));
        runnerEditForm.setResetHandler(editedServer -> {
            updateRunnerGrid();
            RallyUI.closeWindows();
        });
    }

    private void saveRunner(Runner runner) {
        selectedRunner = runnerRepository.saveAndFlush(runner);
        updateRunnerGrid();
        RallyUI.closeWindows();
    }

    private void updateRunnerGrid() {
        runnersGrid.setDataProvider(new ListDataProvider<>(runnerRepository.findAll()));
        if (selectedRunner != null) {
            runnersGrid.select(selectedRunner);
        }
    }

    private void confirmDeleteSponsor(Sponsor sponsor) {
        new ConfirmDialog(I18n.SPONSOR_DELETE_QUESTION.get(sponsor.getName()),
                          I18n.SPONSOR_DELETE_CONFIRM.get(),
                          I18n.SPONSOR_DELETE_CANCEL.get(),
                          () -> deleteSponsor(sponsor));
    }

    private void deleteSponsor(Sponsor sponsor) {
        sponsorRepository.delete(sponsor);
        showSponsorsFor(sponsor.getRunner());
    }

    private void editSponsor(Sponsor sponsor) {
        SponsorEditForm sponsorEditForm = new SponsorEditForm(sponsor, shekelToEuro);
        sponsorEditForm.openInModalPopup();
        sponsorEditForm.setSavedHandler(entity -> saveSponsor(sponsor));
        sponsorEditForm.setResetHandler(editedServer -> {
            showSponsorsFor(sponsor.getRunner());
            RallyUI.closeWindows();
        });
    }

    private void saveSponsor(Sponsor sponsor) {
        sponsorRepository.saveAndFlush(sponsor);
        showSponsorsFor(sponsor.getRunner());
        RallyUI.closeWindows();
    }
}
