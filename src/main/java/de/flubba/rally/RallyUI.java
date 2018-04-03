package de.flubba.rally;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.teemusa.sidemenu.SideMenu;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

import de.flubba.generated.i18n.I18n;
import de.flubba.rally.view.ResultsView;
import de.flubba.rally.view.LiveView;
import de.flubba.rally.view.RunnerView;
import de.flubba.rally.view.TagAssignmentsView;

@Push
@Theme("mytheme")
@Widgetset("AppWidgetset")
@SpringUI
@Viewport("width=device-width, initial-scale=1")
public class RallyUI extends UI {
    @Autowired
    private SpringViewProvider springViewProvider;

    @Autowired
    private SpringNavigator springNavigator;

    @Override
    protected void init(VaadinRequest request) {
        setLocale(Locale.UK); // because it's English but the first day of the week is Monday
        Page.getCurrent().setTitle(I18n.APPLICATION_TITLE.get());

        initNavigator();
        addSideMenu();
    }

    @Autowired
    private MySideMenu sideMenu;

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

    // TODO: use something more sensible than this to close entity edit windows
    public static void closeWindows() {
        UI.getCurrent().getWindows().stream().forEach(w -> UI.getCurrent().removeWindow(w));
    }
}
