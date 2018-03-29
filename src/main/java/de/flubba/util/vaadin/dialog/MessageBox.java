package de.flubba.util.vaadin.dialog;

import java.text.MessageFormat;
import java.util.HashMap;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class MessageBox {
    public enum ButtonId {
        // the order of definition in the enum represents the order in which the
        // buttons will apprear
        OK(VaadinIcons.CHECK, ValoTheme.BUTTON_PRIMARY),
        YES(VaadinIcons.CHECK, ValoTheme.BUTTON_FRIENDLY),
        NO(VaadinIcons.BAN, ValoTheme.BUTTON_DANGER),
        CANCEL(null, null);

        private final VaadinIcons icon;
        private final String      style;

        ButtonId(VaadinIcons icon, String style) {
            this.icon = icon;
            this.style = style;
        }
    }

    public enum MessageType {
        QUESTION(VaadinIcons.QUESTION_CIRCLE),
        INFO(VaadinIcons.INFO_CIRCLE),
        WARNING(
                VaadinIcons.EXCLAMATION_CIRCLE),
        ERROR(VaadinIcons.CLOSE_CIRCLE);

        private final VaadinIcons icon;

        MessageType(VaadinIcons icon) {
            this.icon = icon;
        }

        private String getCssClass() {
            return "message-box-" + toString().toLowerCase();
        }
    }

    private static String ICON_CLASS  = "message-box-icon";
    private static String LABEL_CLASS = "message-box-label";

    private final HashMap<ButtonId, Button> buttons      = new HashMap<>();
    private final HorizontalLayout          buttonLayout = new HorizontalLayout();
    private final Label                     messageLabel = new Label();
    private final Label                     iconLabel    = new Label();
    private final Window                    window       = new Window();

    {
        iconLabel.setContentMode(ContentMode.HTML);
        iconLabel.addStyleName(ICON_CLASS);
        messageLabel.addStyleName(LABEL_CLASS);
        messageLabel.setContentMode(ContentMode.HTML);

        HorizontalLayout messageLayout = new HorizontalLayout();
        messageLayout.addComponent(iconLabel);
        messageLayout.addComponent(messageLabel);
        messageLayout.setComponentAlignment(iconLabel, Alignment.MIDDLE_CENTER);
        messageLayout.setComponentAlignment(messageLabel, Alignment.MIDDLE_LEFT);
        messageLayout.setExpandRatio(messageLabel, 1);

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(messageLayout);
        layout.addComponent(buttonLayout);

        buttonLayout.setSpacing(true);

        window.setContent(layout);
        window.setClosable(false);
        window.setModal(true);
        window.setResizable(false);
    }

    /**
     * create a message window that will close when any of the buttons is
     * clicked
     *
     * @param messageType
     *            type of message
     * @param message
     *            the message to show
     * @param title
     *            the title of the message window - may me null
     * @param buttonIds
     *            the types of buttons that should be displayed
     */
    public MessageBox(MessageType messageType, String message, String title, ButtonId... buttonIds) {
        initButtons(buttonIds);
        iconLabel.setValue(messageType.icon.getHtml());
        iconLabel.addStyleName(messageType.getCssClass());
        messageLabel.setValue(message);
        window.setCaption(title);
        UI.getCurrent().addWindow(window);
    }

    private void initButtons(ButtonId... buttonIds) {
        for (ButtonId buttonId : buttonIds) {
            Button button = new Button();
            if (buttonId.icon != null) {
                button.setIcon(buttonId.icon);
            }
            if (buttonId.style != null) {
                button.addStyleName(buttonId.style);
            }
            button.addClickListener(event -> window.close());
            buttons.put(buttonId, button);
        }
        for (ButtonId buttonId : ButtonId.values()) {
            if (buttons.containsKey(buttonId)) {
                buttonLayout.addComponent(buttons.get(buttonId));
            }
        }
    }

    public Button getButton(ButtonId buttonId) {
        if (!buttons.containsKey(buttonId)) {
            throw new RuntimeException(MessageFormat
                                                    .format("Button {0} war requested via getButton but not specified in constructor",
                                                            buttonId));
        }
        return buttons.get(buttonId);
    }

}
