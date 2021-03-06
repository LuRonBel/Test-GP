package com.patskevich.gpproject.UI.view;

import com.patskevich.gpproject.UI.window.AddUserWindow;
import com.patskevich.gpproject.UI.window.EditUserWindow;
import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.service.UserService;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.access.SecuredViewAccessControl;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.lang.Math.toIntExact;

@SpringView(name = UsersView.NAME)
@AllArgsConstructor
@Secured("ROLE_ADMIN")
@Component(UsersView.NAME)
@UIScope
public class UsersView extends AbstractViewGrid<UserDto> {

    public static final String NAME = "users";
    private UserService userService;
    private AddUserWindow addUserWindow;
    private EditUserWindow editUserWindow;
    private SecuredViewAccessControl accessControl;

    @PostConstruct
    void init() {
        grid = new Grid<>(UserDto.class);
        selectionModel = (MultiSelectionModel<UserDto>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        settingSelectionModel();
        setEventListeners();
        this.addComponent(headerLayout);
        this.addComponent(grid);
        dataProvider = DataProvider.fromFilteringCallbacks(
                query -> {
                    String filter = query.getFilter().orElse(null);
                    if (filter == null) {
                        dtoList = userService.getUserList(null);
                    } else {
                        dtoList = userService.getUserList(filter);
                    }
                    return dtoList.stream();
                },
                query -> {
                    String filter = query.getFilter().orElse(null);
                    return  toIntExact(userService.getUserCount(filter));
                }
        );
        wrapper = dataProvider.withConfigurableFilter();
        grid.setDataProvider(wrapper);
    }

    @Override
    protected void setEventListeners() {
        addButton.addClickListener(clickEvent -> {
            addUserWindow.addCloseListener(closeEvent -> dataProvider.refreshAll());
            getUI().addWindow(addUserWindow);
        });
        editButton.addClickListener(clickEvent -> grid.getSelectedItems().forEach(userDto -> {
            if (accessControl.isAccessGranted(getUI(), "EditUserWindow")) {
                editUserWindow.addCloseListener(closeEvent -> dataProvider.refreshAll());
                editUserWindow.setBean(userDto, userService);
                getUI().addWindow(editUserWindow);
            } else {
                Notification.show(LanguageMessage.getText("access.denied"), Notification.Type.WARNING_MESSAGE);
            }
        }));
        deleteButton.addClickListener(clickEvent -> {
            grid.getSelectedItems().forEach(userDto ->
                    Notification.show(userService.deleteUser(userDto.getLogin())));
            dataProvider.refreshAll();
            selectionModel.deselectAll();
        });
        nameFilteringTextField.addValueChangeListener(valueChangeEvent -> {
            String filter = valueChangeEvent.getValue();
            if (filter.trim().isEmpty()) {
                filter = null;
            }
            wrapper.setFilter(filter);
        });
    }
}
