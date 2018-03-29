package de.flubba.util.i18n;

import java.util.Locale;

/**
 * Interface to create an enum for i18n with - so that calling EnumImplementation.VALUE.get(...) delivers a localized String.
 *
 * @author FlassakD
 *
 */
public interface I18nEnum {
    String key();

    /**
     * get the localized message based on the current Vaadin UI's language
     *
     * @return localized message
     */
    default String get() {
        return Messages.get(toString());
    }

    /**
     * get a localized message
     *
     * @param locale locale
     * @return localized message
     */
    default String get(Locale locale) {
        return Messages.get(locale, toString());
    }

    /**
     * get a localized message based on the current Vaadin UI's language
     *
     * @param arguments arguments based on MessageFormat.format(...)
     * @return localized message
     */
    default String get(Object... arguments) {
        return Messages.get(toString(), arguments);
    }

    default String get(Locale locale, Object... arguments) {
        return Messages.get(locale, toString(), arguments);
    }
}
