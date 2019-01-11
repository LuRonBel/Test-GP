package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.dto.RoomDto.RoomDto;
import com.patskevich.gpproject.service.RoomService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddRoomWindow extends Window {

    private RoomDto room = new RoomDto();

    private final VerticalLayout mainLayout = new VerticalLayout();
    private final FormLayout addRoomForm = new FormLayout();
    private final HorizontalLayout buttonsLayout = new HorizontalLayout();
    private final TextField roomNameField = new TextField("Name: ");
    private final TextField roomDescriptionFiled = new TextField("Description");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    private Binder<RoomDto> roomDtoBinder = new Binder<>();

    public AddRoomWindow(final RoomService roomService){
        super("Add Room");
        this.setContent(mainLayout);
        mainLayout.addComponent(addRoomForm);
        mainLayout.addComponent(buttonsLayout);
        addRoomForm.addComponent(roomNameField);
        addRoomForm.addComponent(roomDescriptionFiled);
        buttonsLayout.addComponent(saveButton);
        buttonsLayout.addComponent(cancelButton);
        settingBinder();
        addEventListener(roomService);
    }

    private void settingBinder() {
        roomDtoBinder.readBean(room);
        roomDtoBinder.forField(roomNameField)
                .withValidator(new StringLengthValidator(
                       "Name must be between 1 and 10 characters long",
                       1, 10))
                .bind(RoomDto::getName, RoomDto::setName);
        roomDtoBinder.forField(roomDescriptionFiled)
                .withValidator(new StringLengthValidator(
                        "Description must be between 1 and 20 characters long",
                        1, 20))
                .bind(RoomDto::getDescription, RoomDto::setDescription);
    }


    private void addEventListener(final RoomService roomService) {
        saveButton.addClickListener(clickEvent -> {
            try {
                if (roomDtoBinder.isValid()) {
                    roomDtoBinder.writeBean(room);
                    Notification.show(roomService.createRoom(room));
                    this.close();
                } else {
                    Notification.show("Error", "Form is't validate", Notification.Type.WARNING_MESSAGE);
                }
            } catch (ValidationException e) {
                Notification.show("Wrong value");
            }
        });

        cancelButton.addClickListener(clickEvent -> {
            roomDtoBinder.readBean(room);
            Notification.show("Cancel");
            this.close();
        });
    }

}
