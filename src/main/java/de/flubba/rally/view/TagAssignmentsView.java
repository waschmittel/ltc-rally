package de.flubba.rally.view;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import de.flubba.rally.entity.repository.TagAssignmentRepository;

import javax.annotation.PostConstruct;

@SpringView(name = TagAssignmentsView.VIEW_NAME)
public class TagAssignmentsView extends TagAssignmentsViewDesign implements View {
    public static final String VIEW_NAME = "tags";

    private final TagAssignmentRepository tagAssignmentRepository;

    public TagAssignmentsView(TagAssignmentRepository tagAssignmentRepository) {
        this.tagAssignmentRepository = tagAssignmentRepository;
    }

    @PostConstruct
    private void init() {
        tagAssignments.setDataProvider(new ListDataProvider<>(tagAssignmentRepository.findAll()));
    }
}
