package com.patskevich.gpproject.UI.view;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.MultiSelectionModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractViewGrid<T> extends VerticalLayout implements View {

    protected HorizontalLayout headerLayout = new HorizontalLayout();
    protected Label tittle = new Label("Menu: ");
    protected Button addButton = new Button("Add");
    protected Button editButton = new Button("Edit");
    protected Button deleteButton = new Button("Delete");
    protected TextField nameFilteringTextField = new TextField();

    protected List<T> dtoList = new ArrayList<>();
    protected DataProvider<T, String> dataProvider;
    protected Grid<T> grid;
    protected MultiSelectionModel<T> selectionModel;
    protected ConfigurableFilterDataProvider<T, Void, String> wrapper;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        grid.setSizeFull();
        headerLayout.addComponents(tittle,addButton,editButton,deleteButton,nameFilteringTextField);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    void settingSelectionModel() {
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
