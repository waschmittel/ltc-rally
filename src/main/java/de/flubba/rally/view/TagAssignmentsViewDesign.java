package de.flubba.rally.view;

import javax.annotation.PostConstruct;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;

import de.flubba.rally.entity.TagAssignment;

public abstract class TagAssignmentsViewDesign extends VerticalLayout {
    protected Grid<TagAssignment> tagAssignments = new Grid<>(TagAssignment.class);

    @PostConstruct
    private void init() {
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        tagAssignments.removeColumn("id");
        tagAssignments.setSelectionMode(SelectionMode.NONE);
        addComponentsAndExpand(tagAssignments);
    }
}
