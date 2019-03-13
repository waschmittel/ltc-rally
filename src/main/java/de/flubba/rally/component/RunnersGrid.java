package de.flubba.rally.component;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderRow;
import de.flubba.rally.entity.Runner;
import de.flubba.rally.entity.repository.RunnerRepository;
import org.vaadin.addons.ResetButtonForTextField;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@SpringComponent
@ViewScope
public class RunnersGrid extends Grid<Runner> {
    public interface SelectionListener {
        void onSelect(Runner runner);
    }

    private TextField runnersFilter = new TextField();

    private final RunnerRepository repository;

    private List<SelectionListener> selectionListeners = new LinkedList<>();
    private Runner selectedRunner = null;

    public RunnersGrid(RunnerRepository repository) {
        super(Runner.class);
        setSizeFull();
        initColumns();
        initHeaderRow();
        initSelection();
        this.repository = repository;
    }

    @PostConstruct
    private void init() {
        sort("id");
        refresh();
    }

    private void initSelection() {
        setSelectionMode(SelectionMode.SINGLE);
        addSelectionListener(event -> {
            if (!event.getFirstSelectedItem().isPresent()) {
                selectedRunner = null;
                selectionListeners.forEach(listener -> listener.onSelect(null));
            }
            event.getFirstSelectedItem().ifPresent(runner -> {
                selectedRunner = runner;
                selectionListeners.forEach(listener -> listener.onSelect(runner));
            });
        });
    }

    private void initColumns() {
        removeColumn("sponsors");
        removeColumn("laps");
        getColumn("donations").setResizable(false).setWidth(100);
        getColumn("numberOfSponsors").setResizable(false).setWidth(100);
        getColumn("average").setResizable(false).setWidth(100);
        getColumn("id").setResizable(false).setWidth(100);
        getColumn("name").setResizable(false).setExpandRatio(1);
        getColumn("gender").setResizable(false).setWidth(100);
        getColumn("roomNumber").setResizable(false).setWidth(100);
        getColumn("numberOfLapsRun").setResizable(false).setWidth(100);
        setColumnOrder("id", "name", "gender");
    }

    private void initHeaderRow() {
        HeaderRow runnersHeader = appendHeaderRow();
        runnersHeader.getCell("name").setComponent(runnersFilter);
        runnersFilter.setWidth("100%");
        ResetButtonForTextField.extend(runnersFilter);
        runnersFilter.addValueChangeListener(e -> refresh());
    }

    public void refresh() {
        setDataProvider(new ListDataProvider<>(repository.findByNameIgnoreCaseContaining(runnersFilter.getValue())));
        if (selectedRunner != null) {
            select(selectedRunner);
        }

    }

    public Runner getSelectedRunner() {
        return selectedRunner;
    }

    public void selectRunner(Runner runner) {
        selectedRunner = runner;
        refresh();
        runnersFilter.setValue("");
    }

    public void addRunnerSelectionListener(SelectionListener selectionListener) {
        selectionListeners.add(selectionListener);
    }
}
