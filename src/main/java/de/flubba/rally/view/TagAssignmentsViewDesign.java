package de.flubba.rally.view;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;
import de.flubba.rally.entity.TagAssignment;

import javax.annotation.PostConstruct;

abstract class TagAssignmentsViewDesign extends VerticalLayout {
    final Grid<TagAssignment> tagAssignments = new Grid<>(TagAssignment.class);

    @PostConstruct
    private void init() {
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        tagAssignments.removeColumn("id");
        tagAssignments.setSelectionMode(SelectionMode.NONE);
        addComponentsAndExpand(tagAssignments);
    }
}
