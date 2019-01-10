package com.patskevich.gpproject.UI.view;


import com.patskevich.gpproject.dto.UserDto.UserDto;
import com.patskevich.gpproject.service.UserService;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static java.lang.Math.toIntExact;

@SpringView(name = UsersView.NAME)
public class UsersView extends AbstractViewGrid<UserDto> {

    public static final String NAME = "users";

    @Autowired
    private UserService userService;

    @PostConstruct
    void init() {
        grid = new Grid<>(UserDto.class);
        selectionModel = (MultiSelectionModel<UserDto>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        settingSelectionModel();
        this.addComponent(headerLayout);
        this.addComponent(grid);

        dataProvider = DataProvider.fromFilteringCallbacks(
                query -> {
                    dtoList = userService.getUserList();
                    return dtoList.stream();
                },
                query -> toIntExact(userService.getUserCount())
        );

        grid.setDataProvider(dataProvider);
    }

    @Override
    protected void setEventListeners() {

    }

}
