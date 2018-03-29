package de.flubba.util.vaadin;

import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.flubba.util.vaadin.EditDeleteButtonsProvider.EntityButtons;

public class EditDeleteButtonsProvider<E> implements ValueProvider<E, EntityButtons> {
    public static final double COLUMN_WIDTH = 130;

    private final DeleteButtonClickListener<E> deleteButtonClickListener;
    private final EditButtonClickListener<E>   editButtonClickListener;
    private final ShowEditButtonProvider<E>    showEditButtonProvider;
    private final ShowDeleteButtonProvider<E>  showDeleteButtonProvider;

    public EditDeleteButtonsProvider(EditButtonClickListener<E> editButtonClickListener,
                                     DeleteButtonClickListener<E> deleteButtonClickListener,
                                     ShowEditButtonProvider<E> showEditButtonProvider,
                                     ShowDeleteButtonProvider<E> showDeleteButtonProvider) {
        this.editButtonClickListener = editButtonClickListener;
        this.deleteButtonClickListener = deleteButtonClickListener;
        this.showEditButtonProvider = showEditButtonProvider;
        this.showDeleteButtonProvider = showDeleteButtonProvider;
    }

    public EditDeleteButtonsProvider(EditButtonClickListener<E> editButtonClickListener,
                                     DeleteButtonClickListener<E> deleteButtonClickListener) {
        this.editButtonClickListener = editButtonClickListener;
        this.deleteButtonClickListener = deleteButtonClickListener;
        this.showEditButtonProvider = sourceEntity -> true;
        this.showDeleteButtonProvider = sourceEntity -> true;
    }

    public EditDeleteButtonsProvider(EditButtonClickListener<E> editButtonClickListener) {
        this.editButtonClickListener = editButtonClickListener;
        this.deleteButtonClickListener = null;
        this.showEditButtonProvider = sourceEntity -> true;
        this.showDeleteButtonProvider = sourceEntity -> false;
    }

    @Override
    public EntityButtons apply(E sourceEntity) {
        EntityButtons buttons = new EntityButtons(showEditButtonProvider.showEditButtonFor(sourceEntity),
                                                  showDeleteButtonProvider.showDeleteButtonFor(sourceEntity));
        buttons.editButton.addClickListener(event -> {
            editButtonClickListener.editButtonClick(sourceEntity);
        });
        buttons.deleteButton.addClickListener(event -> {
            deleteButtonClickListener.deleteButtonClick(sourceEntity);
        });
        return buttons;
    }

    static class EntityButtons extends HorizontalLayout {
        private Button editButton   = new Button("", VaadinIcons.PENCIL);
        private Button deleteButton = new Button("", VaadinIcons.TRASH);

        EntityButtons(boolean withEditButton, boolean withDeleteButton) {
            setSpacing(false);
            setMargin(false);
            setSizeFull();
            setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            editButton.addStyleName(ValoTheme.BUTTON_QUIET);
            deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
            deleteButton.addStyleName(ValoTheme.BUTTON_QUIET);
            if (withEditButton) {
                addComponent(editButton);
            }
            if (withDeleteButton) {
                addComponent(deleteButton);
            }
        }
    }

    public interface DeleteButtonClickListener<E> {
        void deleteButtonClick(E sourceEntity);
    }

    public interface EditButtonClickListener<E> {
        void editButtonClick(E sourceEntity);
    }

    public interface ShowEditButtonProvider<E> {
        boolean showEditButtonFor(E sourceEntity);
    }

    public interface ShowDeleteButtonProvider<E> {
        boolean showDeleteButtonFor(E sourceEntity);
    }
}
