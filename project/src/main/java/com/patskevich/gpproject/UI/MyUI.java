package com.patskevich.gpproject.UI;

import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.service.MessageService;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.spring.annotation.SpringUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringUI
@Theme("mytheme")
public class MyUI extends UI {

    private AdminForm adminForm = new AdminForm(this);
    private UserForm userForm = new UserForm(this);
    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    final VerticalLayout layout = new VerticalLayout();
    final Label userInfo = new Label();
    final Label log = new Label();
    final Label description = new Label();
    final ComboBox<String> roomSelected= new ComboBox<>();
    final Grid<MessageOutputDto> messageGrid = new Grid<>(MessageOutputDto.class);
    final TextField inputTextField = new TextField();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        layout.setWidth("100%");
        setContent(layout);
        adminForm.setVisible(false);
        userForm.setVisible(false);
        setHeightUndefined();
        addMainMenu();
        addSelectedRoom();
        addInputTextLayout();
        addGridPanel();
    }

    private void addMainMenu(){
        MenuBar menuBar = new MenuBar();
        MenuBar.MenuItem admin = menuBar.addItem("admin");
        MenuBar.MenuItem settings = menuBar.addItem("settings");
        MenuBar.MenuItem logout = menuBar.addItem("logout");
        admin.setCommand(e->{
            if (adminForm.isVisible()) adminForm.setVisible(false);
            else adminForm.setVisible(true);
        });
        settings.setCommand(e->{
            if (userForm.isVisible()) userForm.setVisible(false);
            else userForm.setVisible(true);
        });
        if (userService.getUser(authentication.getName()).getRole().equals("ROLE_ADMIN"))
            admin.setVisible(true);
        else admin.setVisible(false);
        layout.addComponent(menuBar);
        layout.setComponentAlignment(menuBar, Alignment.BOTTOM_RIGHT);
    }

    private void addSelectedRoom(){
        HorizontalLayout infoAndLog = new HorizontalLayout();
        HorizontalLayout selectedRoom = new HorizontalLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        userInfo.setValue("Name: "+authentication.getName()+" | Role: "+authentication.getAuthorities());
        log.setValue("LOG: ");
        roomSelected.setCaption("Room:");
        updateRoomSelected(roomSelected);
        roomSelected.addValueChangeListener(e->{
            if (!roomSelected.isSelected("")) {
                updateLog(userService.changeRoom(roomSelected.getSelectedItem().get()));
                updateGrid(userService.getUser(authentication.getName()).getRoom());
            }
            else updateLog("");
        });
        description.setValue("Description: "+userService.getUser(authentication.getName()).getRoom().getDescription());
        infoAndLog.addComponents(userInfo,log);
        selectedRoom.addComponents(roomSelected,description);
        selectedRoom.setComponentAlignment(description, Alignment.BOTTOM_LEFT);
        verticalLayout.addComponents(infoAndLog,selectedRoom);
        layout.addComponents(verticalLayout);
    }

    private void addInputTextLayout(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Button inputTextButton = new Button("Отправить");
        inputTextButton.addClickListener(e-> {
            if (!inputTextField.getValue().equals("")){
                updateLog(messageService.addMessage(inputTextField.getValue()));
                updateGrid(userService.getUser(authentication.getName()).getRoom());
            }
            else updateLog("Введите текст!");
        });
        inputTextButton.setWidth("100%");
        inputTextField.setWidth("1165");
        horizontalLayout.addComponents(inputTextButton,inputTextField);
        horizontalLayout.setExpandRatio(inputTextButton, 1);
        horizontalLayout.setWidthUndefined();
        layout.addComponents(horizontalLayout);
    }

    private void addGridPanel(){
        messageGrid.setColumns("author","message","date");
        messageGrid.setFrozenColumnCount(1);
        messageGrid.getColumn("author").setWidth(150);
        messageGrid.getColumn("date").setWidth(150);
        messageGrid.getColumn("message").setExpandRatio(1);
        updateGrid(userService.getUser(authentication.getName()).getRoom());
        adminForm.setMargin(false);
        userForm.setMargin(false);
        HorizontalLayout gridPanel = new HorizontalLayout(messageGrid,adminForm,userForm);
        gridPanel.setSizeFull();
        messageGrid.setSizeFull();
        gridPanel.setExpandRatio(messageGrid, 1);
        layout.addComponents(gridPanel);
    }

    private void updateGrid(Room room){
        messageGrid.setItems(messageService.getRoomMesage(room.getName()));
    }

    private void updateRoomSelected(ComboBox comboBox){
        comboBox.setItems(roomService.getNameRoomList());
        comboBox.setSelectedItem(userService.getUser(authentication.getName()).getRoom().getName());
    }

    private void updateLog(String text){
        log.setValue("LOG: "+text);
    }
 //   @WebServlet(urlPatterns = "/vaadin/", name = "MyUIServlet", asyncSupported = true)
  //  @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
 //   public static class MyUIServlet extends VaadinServlet {
 //   }
}
