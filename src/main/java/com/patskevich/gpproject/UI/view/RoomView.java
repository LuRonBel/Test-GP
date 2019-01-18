package com.patskevich.gpproject.UI.view;

import com.patskevich.gpproject.UI.window.AddRoomWindow;
import com.patskevich.gpproject.UI.window.EditRoomWindow;
import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.service.RoomService;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.access.SecuredViewAccessControl;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.lang.Math.toIntExact;

@SpringView(name = RoomView.NAME)
@AllArgsConstructor
@Component(RoomView.NAME)
@UIScope
public class RoomView extends AbstractViewGrid<RoomDto> {

    public static final String NAME = "rooms";
    private RoomService roomService;
    private AddRoomWindow addRoomWindow;
    private EditRoomWindow editRoomWindow;
    private SecuredViewAccessControl accessControl;

    @PostConstruct
    void init() {
        grid = new Grid<>(RoomDto.class);
        selectionModel = (MultiSelectionModel<RoomDto>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        setEventListeners();
        settingSelectionModel();
        this.addComponent(headerLayout);
        this.addComponent(grid);
        dataProvider = DataProvider.fromFilteringCallbacks(
                query -> {
                    String filter = query.getFilter().orElse(null);
                    if (filter == null) {
                        dtoList = roomService.getRoomList(null);
                    } else {
                        dtoList = roomService.getRoomList(filter);
                    }
                    return dtoList.stream();
                },
                query -> {
                    String filter = query.getFilter().orElse(null);
                    return  toIntExact(roomService.getRoomCount(filter));
                }
        );
        wrapper = dataProvider.withConfigurableFilter();
        grid.setDataProvider(wrapper);
    }

    @Override
    protected void setEventListeners() {
        addButton.addClickListener(clickEvent -> {
            addRoomWindow.addCloseListener(closeEvent -> dataProvider.refreshAll());
            getUI().addWindow(addRoomWindow);
        });
        editButton.addClickListener(clickEvent -> grid.getSelectedItems().forEach(roomDto -> {
            if (accessControl.isAccessGranted(getUI(), "EditRoomWindow")) {
                editRoomWindow.addCloseListener(closeEvent -> dataProvider.refreshAll());
                editRoomWindow.setBean(roomDto);
                getUI().addWindow(editRoomWindow);
            } else {
                Notification.show(LanguageMessage.getText("access.denied"), Notification.Type.WARNING_MESSAGE);
            }
        }));
        deleteButton.addClickListener(clickEvent -> {
            grid.getSelectedItems().forEach(roomDto ->
                    Notification.show(roomService.deleteRoom(roomDto.getName())));
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
