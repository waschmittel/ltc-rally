package de.flubba.rally.component;

import java.util.EnumSet;

import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.flubba.generated.i18n.I18n;
import de.flubba.rally.entity.Runner;
import de.flubba.rally.entity.Runner.Gender;
import de.flubba.util.vaadin.AbstractForm;

public class RunnerEditForm extends AbstractForm<Runner> {

    private TextField        id         = new TextField(I18n.RUNNER_ID.get());
    private TextField        name       = new TextField(I18n.RUNNER_NAME.get());
    private TextField        roomNumber = new TextField(I18n.RUNNER_ROOM.get());
    private ComboBox<Gender> gender     = new ComboBox<>(I18n.RUNNER_GENDER.get(), EnumSet.allOf(Gender.class));

    public RunnerEditForm(Runner runner) {
        super(Runner.class);

        id.setEnabled(false);
        if (runner.getId() == null) {
            id.setVisible(false);
        }

        gender.setTextInputAllowed(false);

        setModalWindowTitle(I18n.RUNNER_FORM_TITLE.get());
        setSaveCaption(I18n.RUNNER_FORM_BUTTON_SAVE.get());
        setCancelCaption(I18n.RUNNER_FORM_BUTTON_CANCEL.get());

        setSizeUndefined();
        setEntity(runner);
    }

    @Override
    protected void bind() {
        getBinder().forField(id).withConverter(new StringToLongConverter("aaaaaaaaaaaaargh")).withNullRepresentation(0L).bind("id");
        super.bind();
    }

    @Override
    protected Component createContent() {
        return new VerticalLayout(new FormLayout(id, name, gender, roomNumber), getToolbar());
    }
}
