package com.patskevich.gpproject.UI.customField;

import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.service.RoomService;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

public class RoomField extends CustomField<Room> {

    private Room value;
    private final List<String> rooms;
    private final RoomService roomService;
    private final VerticalLayout verticalLayout = new VerticalLayout();
    private final ComboBox<String> roomsComboBox = new ComboBox<>();
    private final Button addButton = new Button(VaadinIcons.PLUS);
    private ListDataProvider<String> roomDataProvider;

    public RoomField(final RoomService roomService, final String caption, final List<String> rooms) {
        this.setCaption(caption);
        this.rooms = new ArrayList<>(rooms);
        this.roomService = roomService;
    }

    @Override
    protected Component initContent() {
        roomDataProvider = DataProvider.ofCollection(rooms);
        roomDataProvider.addFilter(room -> {
            if (value==null) {
                return true;
            }
            return !value.getName().equals(room);
        });
        if (value != null) verticalLayout.addComponent(addRoomValue(value));
        roomsComboBox.setDataProvider(roomDataProvider);
        verticalLayout.addComponent(new HorizontalLayout(roomsComboBox, addButton));
        addButton.setEnabled(false);
        addEventsListener();
        return verticalLayout;
    }

    private void addEventsListener() {
        addButton.addClickListener(clickEvent -> {
            final String selectedValue = roomsComboBox.getValue();
            if (value==null) {
                Room room = roomService.getRoomEntity(selectedValue);
                setValue(room);
                roomDataProvider.refreshAll();
                roomsComboBox.setValue(null);
                verticalLayout.addComponent(addRoomValue(room), 0);
                roomsComboBox.setVisible(false);
                addButton.setVisible(false);
            } else {
                Notification.show(LanguageMessage.getText("error"), LanguageMessage.getText("valid.error"), Notification.Type.WARNING_MESSAGE);
            }
        });
        roomsComboBox.addValueChangeListener(valueChangeEvent -> {
            if (valueChangeEvent.getValue() == null) {
                addButton.setEnabled(false);
            } else {
                addButton.setEnabled(true);
            }
        });
    }

    private HorizontalLayout addRoomValue(final Room room) {
        return new HorizontalLayout(new Label(room.getName()),
                new Button(VaadinIcons.CLOSE, clickEvent -> {
                    setValue(null);
                    roomDataProvider.refreshAll();
                    verticalLayout.removeComponent(clickEvent.getButton().getParent());
                    roomsComboBox.setVisible(true);
                    addButton.setVisible(true);
                })
        );
    }

    @Override
    public Room getValue() {
        return value;
    }

    @Override
    protected void doSetValue(Room value) {
        this.value = value;
    }
}
