package de.flubba.rally.view;

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
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.LinkedList;

@SpringView(name = RunnerView.VIEW_NAME)
public class RunnerView extends RunnerViewDesign implements View {
    public static final String VIEW_NAME = "";

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Value("${de.flubba.rally.shekel-euro-rate}")
    private BigDecimal shekelToEuro;

    @PostConstruct
    public void init() {
        addRunnerButton.addClickListener(event -> editRunner(new Runner()));

        addSponsorButton.addClickListener(event -> {
            Sponsor newSponsor = new Sponsor();
            System.out.println("runner for sponsor: " + runnersGrid.getSelectedRunner().getId());
            newSponsor.setRunner(runnersGrid.getSelectedRunner());
            editSponsor(newSponsor);
        });

        sponsorsGrid.addComponentColumn(new EditDeleteButtonsProvider<>(this::editSponsor, this::confirmDeleteSponsor)).setResizable(false)
                .setWidth(120);
        runnersGrid.addComponentColumn(new EditDeleteButtonsProvider<>(this::editRunner)).setResizable(false).setWidth(100);

        refreshButton.addClickListener(e -> runnersGrid.refresh());

        runnersGrid.addRunnerSelectionListener(this::showSponsorsFor);
    }

    private void showSponsorsFor(Runner runner) {
        if (runner == null) {
            addSponsorButton.setEnabled(false);
            addSponsorButton.setCaption(I18n.SPONSOR_BUTTON_ADD.get());
            sponsorsGrid.setDataProvider(new ListDataProvider<>(new LinkedList<>()));
        } else {
            addSponsorButton.setEnabled(true);
            addSponsorButton.setCaption(I18n.SPONSOR_BUTTON_NAMED_ADD.get(runner.getName()));
            sponsorsGrid.setDataProvider(new ListDataProvider<>(sponsorRepository.findByRunner(runner)));
        }
    }

    private void editRunner(Runner runner) {
        RunnerEditForm runnerEditForm = new RunnerEditForm(runner);
        runnerEditForm.openInModalPopup();
        runnerEditForm.setSavedHandler(entity -> saveRunner(runner));
        runnerEditForm.setResetHandler(editedServer -> {
            runnersGrid.refresh();
            RallyUI.closeWindows();
        });
    }

    private void saveRunner(Runner runner) {
        runner.setName(WordUtils.capitalizeFully(runner.getName()));
        runnersGrid.selectRunner(runnerRepository.saveAndFlush(runner));
        RallyUI.closeWindows();
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
