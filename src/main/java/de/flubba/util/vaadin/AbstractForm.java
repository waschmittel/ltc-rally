/*
 * Copyright 2017 Matti Tahvonen, Daniel Flassak.
 *
 * Based on Viritin's AbstractForm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.flubba.util.vaadin;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import java.io.Serializable;

/**
 * @author mstahv
 */
public abstract class AbstractForm<T> extends CustomComponent {

    public interface DeleteHandler<T> extends Serializable {

        void onDelete(T entity);
    }

    public interface ResetHandler<T> extends Serializable {

        void onReset(T entity);
    }

    public interface SavedHandler<T> extends Serializable {

        void onSave(T entity);
    }

    private boolean settingBean;

    private T entity;
    private SavedHandler<T> savedHandler;
    private ResetHandler<T> resetHandler;
    private DeleteHandler<T> deleteHandler;
    private String modalWindowTitle = "Edit entry";
    private String saveCaption = "Save";
    private String deleteCaption = "Delete";
    private String cancelCaption = "Cancel";
    private Window popup;
    private Binder<T> binder;
    private boolean hasChanges = false;

    private Button saveButton;

    private Button resetButton;

    private Button deleteButton;

    public AbstractForm(Class<T> entityType) {
        addAttachListener(new AttachListener() {

            private static final long serialVersionUID = 3193438171004932112L;

            @Override
            public void attach(AttachEvent event) {
                lazyInit();
            }
        });
        binder = new BeanValidationBinder<>(entityType);
        binder.addValueChangeListener(e -> {
            // binder.hasChanges is not really useful so track it manually
            if (!settingBean) {
                hasChanges = true;
            }
        });
        binder.addStatusChangeListener(e -> {
            // Eh, value change listener is called after status change listener, so
            // ensure flag is on...
            if (!settingBean) {
                hasChanges = true;
            }
            adjustResetButtonState();
            adjustSaveButtonState();
        });
    }

    private boolean findFieldAndFocus(Component compositionRoot) {
        boolean fieldFound = false;
        if (compositionRoot instanceof AbstractComponentContainer) {
            AbstractComponentContainer cc = (AbstractComponentContainer) compositionRoot;

            for (Component component : cc) {
                if (component instanceof AbstractTextField) {
                    AbstractTextField abstractTextField = (AbstractTextField) component;
                    if (!abstractTextField.isReadOnly()) {
                        abstractTextField.selectAll();
                        fieldFound = true;
                    }
                }
                if (component instanceof AbstractField) {
                    @SuppressWarnings("rawtypes")
                    AbstractField abstractField = (AbstractField) component;
                    if (!abstractField.isReadOnly()) {
                        abstractField.focus();
                        fieldFound = true;
                    }
                }
                if (component instanceof AbstractComponentContainer) {
                    if (findFieldAndFocus(component)) {
                        fieldFound = true;
                    }
                }
                if (fieldFound) {
                    return true;
                }
            }
        }
        return fieldFound;
    }

    protected void adjustResetButtonState() {
        if (popup != null && popup.getParent() != null) {
            // Assume cancel button in a form opened to a popup also closes
            // it, allows closing via cancel button by default
            getResetButton().setEnabled(true);
            return;
        }
        if (isBound()) {
            boolean modified = hasChanges();
            getResetButton().setEnabled(modified || popup != null);
        }
    }

    protected void adjustSaveButtonState() {
        if (isBound()) {
            boolean valid = binder.isValid();
            getSaveButton().setEnabled(valid);
        }
    }

    /**
     * By default just does simple name based binding. Override this method to
     * customize the binding.
     */
    protected void bind() {
        binder.bindInstanceFields(this);
    }

    /**
     * This method should return the actual content of the form, including
     * possible toolbar.
     *
     * <p>
     * Use setEntity(T entity) to fill in the data. Am example implementation
     * could look like this:
     * </p>
     *
     * <pre>
     * <code>
     * public class PersonForm extends AbstractForm&lt;Person&gt; {
     *
     *     private TextField firstName = new MTextField(&quot;First Name&quot;);
     *     private TextField lastName = new MTextField(&quot;Last Name&quot;);
     *
     *    {@literal @}Override
     *     protected Component createContent() {
     *         return new MVerticalLayout(
     *                 new FormLayout(
     *                         firstName,
     *                         lastName
     *                 ),
     *                 getToolbar()
     *         );
     *     }
     * }
     * </code>
     * </pre>
     *
     * @return the content of the form
     */
    protected abstract Component createContent();

    protected Button createDeleteButton() {
        Button deleteButton = new Button(getDeleteCaption(), VaadinIcons.TRASH);
        deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
        deleteButton.setVisible(false);
        return deleteButton;
    }

    protected Button createResetButton() {
        Button resetButton = new Button(getCancelCaption(), VaadinIcons.CLOSE);
        resetButton.setVisible(false);
        return resetButton;
    }

    protected Button createSaveButton() {
        Button saveButton = new Button(getSaveCaption(), VaadinIcons.CHECK);
        saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.setVisible(false);
        return saveButton;
    }

    protected void delete(Button.ClickEvent e) {
        deleteHandler.onDelete(getEntity());
        hasChanges = false;
    }

    protected boolean isBound() {
        return binder != null && binder.getBean() != null;
    }

    protected void lazyInit() {
        if (getCompositionRoot() == null) {
            setCompositionRoot(createContent());
            bind();
        }
    }

    protected void reset(Button.ClickEvent e) {
        resetHandler.onReset(getEntity());
        hasChanges = false;
        adjustSaveButtonState();
        adjustResetButtonState();
    }

    protected void save(Button.ClickEvent e) {
        savedHandler.onSave(getEntity());
        hasChanges = false;
        adjustSaveButtonState();
        adjustResetButtonState();
    }

    public void closePopup() {
        if (getPopup() != null) {
            getPopup().close();
        }
    }

    /**
     * Focuses the first field found from the form. It often improves UX to call
     * this method, or focus another field, when you assign a bean for editing.
     */
    public void focusFirst() {
        Component compositionRoot = getCompositionRoot();
        findFieldAndFocus(compositionRoot);
    }

    public Binder<T> getBinder() {
        return binder;
    }

    public String getCancelCaption() {
        return cancelCaption;
    }

    public Button getDeleteButton() {
        if (deleteButton == null) {
            setDeleteButton(createDeleteButton());
        }
        return deleteButton;
    }

    public String getDeleteCaption() {
        return deleteCaption;
    }

    public DeleteHandler<T> getDeleteHandler() {
        return deleteHandler;
    }

    /**
     * @return the currently edited entity or null if the form is currently
     * unbound
     */
    public T getEntity() {
        return entity;
    }

    public String getModalWindowTitle() {
        return modalWindowTitle;
    }

    /**
     * @return the last Popup into which the Form was opened with
     * #openInModalPopup method or null if the form hasn't been use in window
     */
    public Window getPopup() {
        return popup;
    }

    public Button getResetButton() {
        if (resetButton == null) {
            setResetButton(createResetButton());
        }
        return resetButton;
    }

    public ResetHandler<T> getResetHandler() {
        return resetHandler;
    }

    public Button getSaveButton() {
        if (saveButton == null) {
            setSaveButton(createSaveButton());
        }
        return saveButton;
    }

    public String getSaveCaption() {
        return saveCaption;
    }

    public SavedHandler<T> getSavedHandler() {
        return savedHandler;
    }

    /**
     * @return A default toolbar containing save/cancel/delete buttons
     */
    public HorizontalLayout getToolbar() {
        return new HorizontalLayout(getSaveButton(),
                getResetButton(),
                getDeleteButton());
    }

    /**
     * @return true if bean has been changed since last setEntity call.
     */
    public boolean hasChanges() {
        return hasChanges;
    }

    public Window openInModalPopup() {
        popup = new Window(getModalWindowTitle(), this);
        popup.setModal(true);
        UI.getCurrent().addWindow(popup);
        focusFirst();
        adjustResetButtonState();
        return popup;
    }

    public void setBinder(Binder<T> binder) {
        this.binder = binder;
    }

    public void setCancelCaption(String cancelCaption) {
        this.cancelCaption = cancelCaption;
        if (resetButton != null) {
            getResetButton().setCaption(getCancelCaption());
        }
    }

    public void setDeleteButton(final Button deleteButton) {
        this.deleteButton = deleteButton;
        deleteButton.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = -2693734056915561664L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                delete(event);
            }
        });
    }

    public void setDeleteCaption(String deleteCaption) {
        this.deleteCaption = deleteCaption;
        if (deleteButton != null) {
            getDeleteButton().setCaption(getDeleteCaption());
        }
    }

    public void setDeleteHandler(DeleteHandler<T> deleteHandler) {
        this.deleteHandler = deleteHandler;
        getDeleteButton().setVisible(this.deleteHandler != null);
    }

    /**
     * Sets the object to be edited by this form. This method binds all fields
     * from this form to given objects.
     *
     * <p>
     * If your form needs to manually configure something based on the state of
     * the edited object, you can override this method to do that either before
     * the object is bound to fields or to do something after the bean binding.
     * </p>
     *
     * @param entity the object to be edited by this form
     */
    public void setEntity(T entity) {
        this.entity = entity;
        settingBean = true;
        lazyInit();
        if (entity != null) {
            binder.setBean(entity);
            hasChanges = false;
            setVisible(true);
        } else {
            binder.setBean(null);
            hasChanges = false;
            setVisible(false);
        }
        settingBean = false;
    }

    public void setModalWindowTitle(String modalWindowTitle) {
        this.modalWindowTitle = modalWindowTitle;
    }

    public void setResetButton(Button resetButton) {
        this.resetButton = resetButton;
        this.resetButton.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = -19755976436277487L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                reset(event);
            }
        });
    }

    public void setResetHandler(ResetHandler<T> resetHandler) {
        this.resetHandler = resetHandler;
        getResetButton().setVisible(this.resetHandler != null);
    }

    public void setSaveButton(Button button) {
        saveButton = button;
        saveButton.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = -2058398434893034442L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                save(event);
            }
        });
    }

    public void setSaveCaption(String saveCaption) {
        this.saveCaption = saveCaption;
        if (saveButton != null) {
            getSaveButton().setCaption(getSaveCaption());
        }
    }

    public void setSavedHandler(SavedHandler<T> savedHandler) {
        this.savedHandler = savedHandler;
        getSaveButton().setVisible(this.savedHandler != null);
    }

}
