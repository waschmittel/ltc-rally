package de.flubba.util.vaadin.dialog;

import com.vaadin.ui.Button;

import de.flubba.util.vaadin.dialog.MessageBox.ButtonId;
import de.flubba.util.vaadin.dialog.MessageBox.MessageType;

public class ConfirmDialog {
    @FunctionalInterface
    public interface ConfirmButtonHandler {
        void onConfirm();
    }

    @FunctionalInterface
    public interface CancelButtonHandler {
        void onCancel();
    }

    private final MessageBox messageBox;

    /**
     * show a confirmation dialog
     *
     * @param question
     *            the question to ask, putting text into a ContentMode.HTML
     *            formatted label
     * @param confirmCaption
     *            the caption of the "cancel" button
     * @param cancelCaption
     *            the caption of the "cancel" button
     * @param confirmButtonHandler
     *            what should be done if the answer is "yes"
     * @param cancelButtonHandler
     *            what should be done if the answer is "no"
     */
    public ConfirmDialog(String question, String confirmCaption, String cancelCaption,
                         final ConfirmButtonHandler confirmButtonHandler, final CancelButtonHandler cancelButtonHandler) {
        messageBox = new MessageBox(MessageType.QUESTION, question, null, ButtonId.YES, ButtonId.CANCEL);
        messageBox.getButton(ButtonId.CANCEL).focus();
        messageBox.getButton(ButtonId.YES).setCaption(confirmCaption);
        messageBox.getButton(ButtonId.YES).addClickListener(event -> confirmButtonHandler.onConfirm());
        messageBox.getButton(ButtonId.CANCEL).setCaption(cancelCaption);
        if (cancelButtonHandler != null) {
            messageBox.getButton(ButtonId.YES).addClickListener(event -> cancelButtonHandler.onCancel());
        }
    }

    /**
     * show a confirmation dialog
     *
     * @param question
     *            the question to ask, putting text into a ContentMode.HTML
     *            formatted label
     * @param confirmCaption
     *            the caption of the "cancel" button
     * @param cancelCaption
     *            the caption of the "cancel" button
     * @param confirmButtonHandler
     *            what should be done if the answer is "yes"
     */
    public ConfirmDialog(String question, String confirmCaption, String cancelCaption,
                         final ConfirmButtonHandler confirmButtonHandler) {
        this(question, confirmCaption, cancelCaption, confirmButtonHandler, null);
    }

    /**
     * get the "no" button (use to style the button or give it an icon)
     *
     * @return the "no" button
     */
    public Button getCancelButton() {
        return messageBox.getButton(ButtonId.CANCEL);
    }

    /**
     * get the "yes" button (use to style the button or give it an icon)
     *
     * @return the "yes" button
     */
    public Button getConfirmButton() {
        return messageBox.getButton(ButtonId.YES);
    }

}
