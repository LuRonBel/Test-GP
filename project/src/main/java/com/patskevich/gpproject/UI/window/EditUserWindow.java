package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.UI.customField.RoomField;
import com.patskevich.gpproject.dto.UserDto.CreateUserDtoUi;
import com.patskevich.gpproject.dto.UserDto.UserDto;
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
public class EditUserWindow extends Window {

    private CreateUserDtoUi user = new CreateUserDtoUi();
    private List<String> rooms = new ArrayList<>();

    private final VerticalLayout mainLayout = new VerticalLayout();
    private final FormLayout editUserForm = new FormLayout();
    private final HorizontalLayout buttonsLayout = new HorizontalLayout();
    private final TextField userLoginField = new TextField("Login: ");
    private final TextField userNewLoginField = new TextField("New login: ");
    private final TextField userNicknameField = new TextField("Nickname: ");
    private final TextField userPasswordField = new TextField("Password: ");
    private final RoomField roomField;
    private final RadioButtonGroup<String> roleRadioButton = new RadioButtonGroup<>("Select role");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    private Binder<CreateUserDtoUi> userDtoBinder = new Binder<>();

    public EditUserWindow(final UserDto userDto,
                          final UserService userService,
                          final RoomService roomService){
        super("Edit user");
        final CreateUserDtoUi user = new CreateUserDtoUi();
        user.setLogin(userDto.getLogin());
        user.setNickname(userDto.getNickname());
        user.setPassword("");
        user.setRoom(userService.getUserRepository().findByLogin(userDto.getLogin()).getRoom());
        user.setRole(userDto.getRole());
        this.user = user;
        roleRadioButton.setItems("ROLE_ADMIN","ROLE_USER");
        userLoginField.setValue(userDto.getLogin());
        fillRoomsList(roomService);
        roomField = new RoomField(roomService,"Rooms", rooms);
        this.setContent(mainLayout);
        mainLayout.addComponent(editUserForm);
        mainLayout.addComponent(buttonsLayout);
        userLoginField.setEnabled(false);
        editUserForm.addComponents(userLoginField,userNewLoginField,userNicknameField,userPasswordField,roomField,roleRadioButton);
        buttonsLayout.addComponent(saveButton);
        buttonsLayout.addComponent(cancelButton);
        settingBinder();
        addEventListener(roomService, userService);
    }

    private void settingBinder() {
        userDtoBinder.forField(userNewLoginField)
                .withValidator(new StringLengthValidator(
                        "Login must be between 1 and 10 characters long",
                        1, 10))
                .bind(CreateUserDtoUi::getLogin, CreateUserDtoUi::setLogin);
        userDtoBinder.forField(userNicknameField)
                .withValidator(new StringLengthValidator(
                        "Nickname must be between 1 and 10 characters long",
                        1, 10))
                .bind(CreateUserDtoUi::getNickname, CreateUserDtoUi::setNickname);
        userDtoBinder.forField(userPasswordField)
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
        userDtoBinder.readBean(user);
    }

    private void addEventListener(final RoomService roomService, final UserService userService) {

        saveButton.addClickListener(clickEvent -> {
            try {
                if (userDtoBinder.isValid()) {
                    userDtoBinder.writeBean(user);
                    Notification.show(userService.updateUserUi(user, userLoginField.getValue()));
                    this.close();
                } else{
                    Notification.show("Error", "Form is't validate", Notification.Type.WARNING_MESSAGE);
                }
            } catch (ValidationException e) {
                Notification.show("Wrong value");
            }
        });

        cancelButton.addClickListener(clickEvent -> {
            userDtoBinder.readBean(user);
            Notification.show("Cancel");
            this.close();
        });
    }

    private void fillRoomsList(final RoomService roomService) {
        roomService.getRoomList()
                .forEach(roomDto -> rooms.add(roomDto.getName()));
    }
}
