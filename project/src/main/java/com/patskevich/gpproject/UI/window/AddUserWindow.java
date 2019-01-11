package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.UI.customField.RoomField;
import com.patskevich.gpproject.dto.UserDto.CreateUserDtoUi;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class AddUserWindow extends Window {

    private CreateUserDtoUi user = new CreateUserDtoUi();

    private List<String> rooms = new ArrayList<>();

    private final VerticalLayout mainLayout = new VerticalLayout();
    private final FormLayout addUserForm = new FormLayout();
    private final HorizontalLayout buttonsLayout = new HorizontalLayout();
    private final TextField userLoginField = new TextField("Login: ");
    private final TextField userNicknameField = new TextField("Nickname: ");
    private final TextField userPasswordFiled = new TextField("Password: ");
    private final RoomField roomField;
    private final RadioButtonGroup<String> roleRadioButton = new RadioButtonGroup<>("Select role");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    private Binder<CreateUserDtoUi> userDtoBinder = new Binder<>();

    public AddUserWindow(final UserService userService, final RoomService roomService){
        super("Add user");
        fillRoomsList(roomService);
        roomField = new RoomField(roomService,"Rooms", rooms);
        roleRadioButton.setItems("ROLE_ADMIN","ROLE_USER");
        this.setContent(mainLayout);
        mainLayout.addComponent(addUserForm);
        mainLayout.addComponent(buttonsLayout);
        addUserForm.addComponents(userLoginField, userNicknameField, userPasswordFiled, roomField, roleRadioButton);
        buttonsLayout.addComponent(saveButton);
        buttonsLayout.addComponent(cancelButton);
        settingBinder();
        addEventListener(userService);
    }

    private void settingBinder() {
        userDtoBinder.readBean(user);
        userDtoBinder.forField(userLoginField)
                .withValidator(new StringLengthValidator(
                        "Login must be between 1 and 10 characters long",
                        1, 10))
                .bind(CreateUserDtoUi::getLogin, CreateUserDtoUi::setLogin);
        userDtoBinder.forField(userNicknameField)
                .withValidator(new StringLengthValidator(
                        "Nickname must be between 1 and 10 characters long",
                        1, 10))
                .bind(CreateUserDtoUi::getNickname, CreateUserDtoUi::setNickname);
        userDtoBinder.forField(userPasswordFiled)
                .withValidator(new StringLengthValidator(
                        "Password must be between 1 and 10 characters long",
                        1, 10))
                .bind(CreateUserDtoUi::getPassword, CreateUserDtoUi::setPassword);
        userDtoBinder.forField(roomField)
                .withValidator(room -> !(room==null), "Error")
                .bind(CreateUserDtoUi::getRoom, CreateUserDtoUi::setRoom);
        userDtoBinder.forField(roleRadioButton)
                .withValidator(role-> !(role==null), "Error")
                .bind(CreateUserDtoUi::getRole, CreateUserDtoUi::setRole);
    }

    private void addEventListener(final UserService userService) {

        saveButton.addClickListener(clickEvent -> {
            try {
                if (userDtoBinder.isValid()) {
                    userDtoBinder.writeBean(user);
                    Notification.show(userService.createUser(user));
                    this.close();
                } else {
                    Notification.show("Error", "Form is't validate", Notification.Type.WARNING_MESSAGE);
                }
            } catch (ValidationException e) {
                Notification.show("Wrong value");
            }
        });

        cancelButton.addClickListener(clickEvent -> {
            Notification.show("Cancel");
            this.close();
        });
    }

    private void fillRoomsList(final RoomService roomService) {
        roomService.getRoomList()
                .forEach(roomDto -> rooms.add(roomDto.getName()));
    }
}
