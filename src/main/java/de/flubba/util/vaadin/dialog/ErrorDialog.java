package de.flubba.util.vaadin.dialog;

import de.flubba.util.vaadin.dialog.MessageBox.ButtonId;
import de.flubba.util.vaadin.dialog.MessageBox.MessageType;

public class ErrorDialog {

    public ErrorDialog(String errorMessage) {
        MessageBox messageBox = new MessageBox(MessageType.ERROR, errorMessage, null, ButtonId.OK);
        messageBox.getButton(ButtonId.OK).focus();
    }

}
