package com.patskevich.gpproject.UI.view;

import com.patskevich.gpproject.UI.window.AddUserWindow;
import com.patskevich.gpproject.UI.window.EditUserWindow;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import lombok.AllArgsConstructor;

import javax.annotation.PostConstruct;

import static java.lang.Math.toIntExact;

@SpringView(name = UsersView.NAME)
@AllArgsConstructor
public class UsersView extends AbstractViewGrid<UserDto> {

    public static final String NAME = "users";
    private UserService userService;
    private RoomService roomService;
    private AddUserWindow addUserWindow;

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
                    final EditUserWindow window = new EditUserWindow(userDto, userService, roomService);
                    window.addCloseListener(closeEvent -> dataProvider.refreshAll());
                    getUI().addWindow(window);
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
