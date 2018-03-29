package de.flubba.util.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.vaadin.ui.UI;

/**
 * Helper class to get localized messages
 *
 * @author FlassakD
 *
 */
final class Messages {
    /**
     * get a localized message based on the current Vaadin UI's language
     *
     * @param messageId message id (key in properties file)
     * @return localized message
     */
    public static String get(String messageId) {
        Locale locale = UI.getCurrent().getLocale();
        return get(locale, messageId);
    }

    /**
     * get a localized message
     *
     * @param locale locale
     * @param messageId message id (key in properties file)
     * @return localized message
     */
    public static String get(Locale locale, String messageId) {
        try {
            return ResourceBundle.getBundle("i18n", locale).getString(messageId);
        }
        catch (MissingResourceException e) {
            return MessageFormat.format("MISSING TRANSLATION for \"{0}\" in locale {1}", messageId, locale);
        }
    }

    /**
     * get a localized message based on the current Vaadin UI's language
     *
     * @param messageId message id (key in properties file)
     * @param arguments arguments based on MessageFormat.format(...)
     * @return localized message
     */
    public static String get(String messageId, Object... arguments) {
        return MessageFormat.format(get(messageId), arguments);
    }

    /**
     * get a localized message based on the current Vaadin UI's language
     *
     * @param messageId message id (key in properties file)
     * @param arguments arguments based on MessageFormat.format(...)
     * @param locale locale
     * @return localized message
     */
    public static String get(Locale locale, String messageId, Object... arguments) {
        return MessageFormat.format(get(locale, messageId), arguments);
    }

    private Messages() {
    }
}
