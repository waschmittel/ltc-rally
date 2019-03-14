package de.flubba.rally;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import de.flubba.generated.i18n.I18n;
import de.flubba.rally.view.LiveView;
import de.flubba.rally.view.ResultsView;
import de.flubba.rally.view.RunnerView;
import de.flubba.rally.view.TagAssignmentsView;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.teemusa.sidemenu.SideMenu;

import java.util.Locale;

@Push
@Theme("mytheme")
@Widgetset("AppWidgetset")
@SpringUI
@Viewport("width=device-width, initial-scale=1")
public class RallyUI extends UI {
    @Value("${server.session.timeout}")
    private Integer sessionTimeout;

    private final SpringViewProvider springViewProvider;
    private final SpringNavigator springNavigator;

    public RallyUI(SpringViewProvider springViewProvider, SpringNavigator springNavigator, MySideMenu sideMenu) {
        this.springViewProvider = springViewProvider;
        this.springNavigator = springNavigator;
        this.sideMenu = sideMenu;
    }

    @Override
    protected void init(VaadinRequest request) {
        setSessionTimeout();
        setLocale(Locale.UK); // because it's English but the first day of the week is Monday
        Page.getCurrent().setTitle(I18n.APPLICATION_TITLE.get());

        initNavigator();
        addSideMenu();
    }

    private void setSessionTimeout() {
        // needs to be set if the application is deployed as a WAR instead of a jar
        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(sessionTimeout);
    }

    private final MySideMenu sideMenu;

    private void initNavigator() {
        springNavigator.addProvider(springViewProvider);
        // springNavigator.setErrorView(ErrorView.class);
    }

    private void addSideMenu() {
        sideMenu.setMenuCaption(I18n.APPLICATION_TITLE.get());
        setContent(sideMenu);
        addSideMenuItems();
    }

    private void addSideMenuItems() {
        sideMenu.addNavigation(I18n.NAVIGATION_RUNNERS.get(), VaadinIcons.HANDS_UP, RunnerView.VIEW_NAME);
        sideMenu.addNavigation(I18n.NAVIGATION_TAGS.get(), VaadinIcons.AUTOMATION, TagAssignmentsView.VIEW_NAME);
        sideMenu.addNavigation(I18n.NAVIGATION_LIVE.get(), VaadinIcons.ROAD, LiveView.VIEW_NAME);
        sideMenu.addNavigation(I18n.NAVIGATION_RESULTS.get(), VaadinIcons.TROPHY, ResultsView.VIEW_NAME);
    }

    @SpringViewDisplay
    public static final class MySideMenu extends SideMenu implements ViewDisplay {
        @Override
        public void showView(View view) {
            setContent(view.getViewComponent());
        }
    }

    public static void closeWindows() {
        UI.getCurrent().getWindows().forEach(w -> UI.getCurrent().removeWindow(w));
    }
}
