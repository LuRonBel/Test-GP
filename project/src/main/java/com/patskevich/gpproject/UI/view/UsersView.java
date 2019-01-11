package com.patskevich.gpproject.UI.view;


import com.patskevich.gpproject.UI.window.AddRoomWindow;
import com.patskevich.gpproject.UI.window.AddUserWindow;
import com.patskevich.gpproject.UI.window.EditRoomWindow;
import com.patskevich.gpproject.UI.window.EditUserWindow;
import com.patskevich.gpproject.dto.UserDto.UserDto;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static java.lang.Math.toIntExact;

@SpringView(name = UsersView.NAME)
public class UsersView extends AbstractViewGrid<UserDto> {

    public static final String NAME = "users";

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

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
                        dtoList = userService.getUserList();
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
            final Window window = new AddUserWindow(userService, roomService);
            window.addCloseListener(closeEvent -> dataProvider.refreshAll());
            getUI().addWindow(window);
        });

        editButton.addClickListener(clickEvent ->
                grid.getSelectedItems().forEach(userDto -> {
                    final Window window = new EditUserWindow(userDto, userService, roomService);
                    window.addCloseListener(closeEvent -> dataProvider.refreshAll());
                    getUI().addWindow(window);
                })
        );

        deleteButton.addClickListener(clickEvent -> {
            grid.getSelectedItems().forEach(userDto ->
                    Notification.show(userService.deleteUser(userDto)));
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
