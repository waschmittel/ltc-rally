package de.flubba.rally.component;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.flubba.generated.i18n.I18n;
import de.flubba.rally.entity.Sponsor;
import de.flubba.util.vaadin.AbstractForm;

public class SponsorEditForm extends AbstractForm<Sponsor> {
    private final BigDecimal shekelToEuro;

    private TextField name            = new TextField(I18n.SPONSOR_NAME.get());
    private TextField street          = new TextField(I18n.SPONSOR_STREET.get());
    private TextField city            = new TextField(I18n.SPONSOR_CITY.get());
    private TextField country         = new TextField(I18n.SPONSOR_COUNTRY.get());
    private TextField perLapDonation  = new TextField(I18n.SPONSOR_PERLAP.get());
    private TextField perLapShekels   = new TextField(I18n.SPONSOR_PERLAP_SHEKEL.get());
    private TextField oneTimeDonation = new TextField(I18n.SPONSOR_ONETIME.get());
    private TextField oneTimeShekels  = new TextField(I18n.SPONSOR_ONETIME_SHEKEL.get());
    private CheckBox  receipt         = new CheckBox(I18n.SPONSOR_RECEIPT.get());

    public SponsorEditForm(Sponsor sponsor, BigDecimal shekelToEuro) {
        super(Sponsor.class);
        this.shekelToEuro = shekelToEuro;

        setModalWindowTitle(I18n.SPONSOR_FORM_TITLE.get());
        setSaveCaption(I18n.SPONSOR_FORM_BUTTON_SAVE.get());
        setCancelCaption(I18n.SPONSOR_FORM_BUTTON_CANCEL.get());

        addShekelConversion(oneTimeShekels, oneTimeDonation);
        addShekelConversion(perLapShekels, perLapDonation);

        setSizeUndefined();
        setEntity(sponsor);
    }

    public void addShekelConversion(TextField source, TextField target) {
        source.addValueChangeListener(event -> {
            try {
                source.setComponentError(null);
                BigDecimal shekels = new BigDecimal(source.getValue().trim());
                BigDecimal euros = shekels.multiply(shekelToEuro).setScale(2, RoundingMode.HALF_UP);
                target.setValue(euros.toPlainString());
            }
            catch (NumberFormatException e) {
                source.setComponentError(new ErrorMessage() {

                    @Override
                    public String getFormattedHtmlMessage() {
                        return "aaaaaaaaaaaaaaaargh";
                    }

                    @Override
                    public ErrorLevel getErrorLevel() {
                        return ErrorLevel.ERROR;
                    }
                });
            }
        });
    }

    @Override
    protected void bind() {
        getBinder().forField(oneTimeDonation).withConverter(new StringToBigDecimalConverter("aaaaaaaaargh"))
                   .withNullRepresentation(new BigDecimal(0)).bind("oneTimeDonation");
        getBinder().forField(perLapDonation).withConverter(new StringToBigDecimalConverter("aaaaaaaaargh"))
                   .withNullRepresentation(new BigDecimal(0)).bind("perLapDonation");
        super.bind();
    }

    @Override
    protected Component createContent() {
        return new VerticalLayout(new FormLayout(name,
                                                 street,
                                                 city,
                                                 country,
                                                 perLapDonation,
                                                 perLapShekels,
                                                 oneTimeDonation,
                                                 oneTimeShekels,
                                                 receipt),
                                  getToolbar());
    }
}
