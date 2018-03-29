package de.flubba.rally.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;

import de.flubba.rally.entity.repository.TagAssignmentRepository;

@SpringView(name = TagAssignmentsView.VIEW_NAME)
public class TagAssignmentsView extends TagAssignmentsViewDesign implements View {
    public static final String VIEW_NAME = "tags";

    @Autowired
    TagAssignmentRepository tagAssignmentRepository;

    @PostConstruct
    private void init() {
        tagAssignments.setDataProvider(new ListDataProvider<>(tagAssignmentRepository.findAll()));
    }
}
