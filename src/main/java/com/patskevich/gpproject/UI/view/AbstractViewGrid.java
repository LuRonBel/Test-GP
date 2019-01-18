package com.patskevich.gpproject.UI.view;

import com.patskevich.gpproject.configuration.LanguageMessage;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.MultiSelectionModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractViewGrid<T> extends VerticalLayout implements View {

    protected final HorizontalLayout headerLayout = new HorizontalLayout();
    protected final Label tittle = new Label(LanguageMessage.getText("menu"));
    protected final Button addButton = new Button(LanguageMessage.getText("add"));
    protected final Button editButton = new Button(LanguageMessage.getText("edit"));
    protected final Button deleteButton = new Button(LanguageMessage.getText("delete"));
    protected final Button logoutButton = new Button(LanguageMessage.getText("logout"));
    protected final TextField nameFilteringTextField = new TextField();

    protected List<T> dtoList = new ArrayList<>();
    protected DataProvider<T, String> dataProvider;
    protected Grid<T> grid;
    protected MultiSelectionModel<T> selectionModel;
    protected ConfigurableFilterDataProvider<T, Void, String> wrapper;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        grid.setSizeFull();
        headerLayout.addComponents(tittle,addButton,editButton,deleteButton,nameFilteringTextField, logoutButton);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        addButton.setIcon(VaadinIcons.PLUS_CIRCLE);
        editButton.setIcon(VaadinIcons.COG);
        deleteButton.setIcon(VaadinIcons.CLOSE_CIRCLE);
        logoutButton.setIcon(VaadinIcons.EXIT);
        logoutButton.addClickListener(clickEvent -> {
            Page.getCurrent().setLocation("/gpproject/login?logout");
            VaadinSession.getCurrent().getSession().invalidate();
        });
    }

    protected void settingSelectionModel() {
        selectionModel.addMultiSelectionListener(selectionEvent -> {
            int selectedItemsCount = selectionEvent.getAllSelectedItems().size();
            if (selectedItemsCount == 0) {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
            } else if (selectedItemsCount == 1) {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                editButton.setEnabled(false);
                deleteButton.setEnabled(true);
            }
        });
    }

    protected abstract void setEventListeners();
}
