package de.flubba.generated.i18n;

/**
 * Auto generated enum type for property file "/src/main/resources/i18n.properties".
 */
public enum I18n implements de.flubba.util.i18n.I18nEnum {

  /**
   * <b>Per Lap in ₪</b><br><br><i>sponsor.perlap.shekel</i>
   */
  SPONSOR_PERLAP_SHEKEL("sponsor.perlap.shekel"),

  /**
   * <b>Receipt</b><br><br><i>sponsor.receipt</i>
   */
  SPONSOR_RECEIPT("sponsor.receipt"),

  /**
   * <b>Tag Assignments</b><br><br><i>navigation.tags</i>
   */
  NAVIGATION_TAGS("navigation.tags"),

  /**
   * <b>Name</b><br><br><i>sponsor.name</i>
   */
  SPONSOR_NAME("sponsor.name"),

  /**
   * <b>Manage Runners</b><br><br><i>navigation.runners</i>
   */
  NAVIGATION_RUNNERS("navigation.runners"),

  /**
   * <b>Delete Sponsor</b><br><br><i>sponsor.delete.confirm</i>
   */
  SPONSOR_DELETE_CONFIRM("sponsor.delete.confirm"),

  /**
   * <b>Save</b><br><br><i>runner.form.button.save</i>
   */
  RUNNER_FORM_BUTTON_SAVE("runner.form.button.save"),

  /**
   * <b>Edit Runner</b><br><br><i>runner.button.edit</i>
   */
  RUNNER_BUTTON_EDIT("runner.button.edit"),

  /**
   * <b>Room No.</b><br><br><i>runner.room</i>
   */
  RUNNER_ROOM("runner.room"),

  /**
   * <b>Cancel</b><br><br><i>runner.form.button.cancel</i>
   */
  RUNNER_FORM_BUTTON_CANCEL("runner.form.button.cancel"),

  /**
   * <b>LTC Rally</b><br><br><i>application.title</i>
   */
  APPLICATION_TITLE("application.title"),

  /**
   * <b>Do you really want to delete the Sponsor {0}?</b><br><br><i>sponsor.delete.question</i>
   */
  SPONSOR_DELETE_QUESTION("sponsor.delete.question"),

  /**
   * <b>Add Runner</b><br><br><i>runner.button.add</i>
   */
  RUNNER_BUTTON_ADD("runner.button.add"),

  /**
   * <b>Country</b><br><br><i>sponsor.country</i>
   */
  SPONSOR_COUNTRY("sponsor.country"),

  /**
   * <b>Name</b><br><br><i>runner.name</i>
   */
  RUNNER_NAME("runner.name"),

  /**
   * <b>Add Sponsor</b><br><br><i>sponsor.button.add</i>
   */
  SPONSOR_BUTTON_ADD("sponsor.button.add"),

  /**
   * <b>One Time in ₪</b><br><br><i>sponsor.onetime.shekel</i>
   */
  SPONSOR_ONETIME_SHEKEL("sponsor.onetime.shekel"),

  /**
   * <b>Cancel</b><br><br><i>sponsor.form.button.cancel</i>
   */
  SPONSOR_FORM_BUTTON_CANCEL("sponsor.form.button.cancel"),

  /**
   * <b>Gender</b><br><br><i>runner.gender</i>
   */
  RUNNER_GENDER("runner.gender"),

  /**
   * <b>ID</b><br><br><i>runner.id</i>
   */
  RUNNER_ID("runner.id"),

  /**
   * <b>Per Lap Donation</b><br><br><i>sponsor.perlap</i>
   */
  SPONSOR_PERLAP("sponsor.perlap"),

  /**
   * <b>Edit Runner</b><br><br><i>runner.form.title</i>
   */
  RUNNER_FORM_TITLE("runner.form.title"),

  /**
   * <b>Street</b><br><br><i>sponsor.street</i>
   */
  SPONSOR_STREET("sponsor.street"),

  /**
   * <b>No, Keep Sponsor</b><br><br><i>sponsor.delete.cancel</i>
   */
  SPONSOR_DELETE_CANCEL("sponsor.delete.cancel"),

  /**
   * <b>Edit Sponsor</b><br><br><i>sponsor.form.title</i>
   */
  SPONSOR_FORM_TITLE("sponsor.form.title"),

  /**
   * <b>One Time Donation</b><br><br><i>sponsor.onetime</i>
   */
  SPONSOR_ONETIME("sponsor.onetime"),

  /**
   * <b>Save</b><br><br><i>sponsor.form.button.save</i>
   */
  SPONSOR_FORM_BUTTON_SAVE("sponsor.form.button.save"),

  /**
   * <b>Live View</b><br><br><i>navigation.live</i>
   */
  NAVIGATION_LIVE("navigation.live"),

  /**
   * <b>City</b><br><br><i>sponsor.city</i>
   */
  SPONSOR_CITY("sponsor.city");

  /**
   * The original key in the property file.
   */
  private final String originalKey;

  /**
   * Constructs a new {@link I18n}.
   * @param originalKey
   *          the property's key as it's denoted in the properties file
   */
  I18n(String originalKey) {
    this.originalKey = originalKey;
  }

  /**
   * @return the source properties file's base name
   */
  public static final String getResourceBaseName() {
    return ".i18n";
  }

  /**
   * @return the property key.
   */
  @Override
  public final String toString() {
    return originalKey;
  }
  /**
   * @return the property key.
   */
  @Override
  public final String key() {
    return originalKey;
  }
}
