package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.dto.RoomDto.RoomDto;
import com.patskevich.gpproject.service.RoomService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EditRoomWindow extends Window {

    private RoomDto room = new RoomDto();

    private final VerticalLayout mainLayout = new VerticalLayout();
    private final FormLayout editRoomForm = new FormLayout();
    private final HorizontalLayout buttonsLayout = new HorizontalLayout();
    private final TextField roomNameField = new TextField("Name: ");
    private final TextField roomDescriptionFiled = new TextField(" New description");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    private Binder<RoomDto> roomDtoBinder = new Binder<>();

    public EditRoomWindow(final RoomDto roomDto, final RoomService roomService){
        super("Edit room");
        this.room = roomDto;
        this.setContent(mainLayout);
        mainLayout.addComponent(editRoomForm);
        mainLayout.addComponent(buttonsLayout);
        roomNameField.setEnabled(false);
        editRoomForm.addComponent(roomNameField);
        editRoomForm.addComponent(roomDescriptionFiled);
        buttonsLayout.addComponent(saveButton);
        buttonsLayout.addComponent(cancelButton);
        settingBinder();
        addEventListener(roomService);
    }

    private void settingBinder() {
        roomDtoBinder.forField(roomNameField)
                .bind(RoomDto::getName, RoomDto::setName);
        roomDtoBinder.forField(roomDescriptionFiled)
                .bind(RoomDto::getDescription, RoomDto::setDescription);
        roomDtoBinder.readBean(room);
    }

    private void addEventListener(final RoomService roomService) {
        saveButton.addClickListener(clickEvent -> {
            try {
                roomDtoBinder.writeBean(room);
                Notification.show(roomService.updateRoom(room));
            } catch (ValidationException e) {
                Notification.show("Wrong value");
            }
            this.close();
        });
        cancelButton.addClickListener(clickEvent -> {
            roomDtoBinder.readBean(room);
            Notification.show("Cancel");
            this.close();
        });
    }

}
