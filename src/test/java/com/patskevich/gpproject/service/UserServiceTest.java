package com.patskevich.gpproject.service;

import com.patskevich.gpproject.MockDataRoom;
import com.patskevich.gpproject.MockDataUser;
import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.converter.NicknameChangeHistoryConverter;
import com.patskevich.gpproject.converter.UserConverter;
import com.patskevich.gpproject.dto.CreateUserDtoUi;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.entity.NicknameChangeHistory;
import com.patskevich.gpproject.entity.RoleEnum;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.NicknameChangeHistoryRepository;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Spy
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserConverter userConverter;

    @Mock
    private NicknameChangeHistoryRepository nicknameChangeHistoryRepository;

    @Mock
    private NicknameChangeHistoryConverter nicknameChangeHistoryConverter;

    @Test
    public void createUser() {
        final String login = "login";
        final String password = "password";
        doReturn(true).when(userRepository).existsByLogin(login);
        String answer = userService.createUser(login, password);

        assertEquals(LanguageMessage.getText("user.exists"), answer);
        doReturn(false).when(userRepository).existsByLogin(login);

        answer = userService.createUser(login, password);
        assertEquals(LanguageMessage.getText("successfully"), answer);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void createUserUi() {
        final CreateUserDtoUi userDto = MockDataUser.getCreateUserDtoUi();
        final User user = MockDataUser.getUser();
        doReturn(true).when(userRepository).existsByLogin(userDto.getLogin());
        doReturn(user).when(userConverter).convertToDbo(userDto);
        String answer = userService.createUser(userDto);

        assertEquals(LanguageMessage.getText("user.exists"), answer);
        doReturn(false).when(userRepository).existsByLogin(userDto.getLogin());
        answer = userService.createUser(userDto);

        verify(userRepository, times(1)).save(user);
        assertEquals(LanguageMessage.getText("successfully"), answer);
    }

    @Test
    public void updateUserUi() {
        final String login = "old login";
        final Room room = MockDataRoom.getRoom();
        final CreateUserDtoUi userDto = MockDataUser.getCreateUserDtoUi();
        final User user = MockDataUser.getUser();
        userDto.setLogin("new login");
        userDto.setNickname("new nickname");
        userDto.setRole(RoleEnum.ROLE_ADMIN.toString());
        userDto.setRoom(room);
        doReturn(true).when(userRepository).existsByLogin(userDto.getLogin());
        doReturn(user).when(userRepository).findByLogin(login);
        String answer = userService.updateUserUi(userDto,login);

        assertEquals(LanguageMessage.getText("error.create"), answer);
        doReturn(false).when(userRepository).existsByLogin(userDto.getLogin());
        answer = userService.updateUserUi(userDto,login);

        assertEquals(LanguageMessage.getText("successfully"), answer);
        assertEquals(userDto.getLogin(), user.getLogin());
        assertEquals(userDto.getNickname(), user.getNickname());
        assertEquals(userDto.getRole(), user.getRole());
        assertEquals(userDto.getRoom(), user.getRoom());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void changeNicknameAndPass() {
        final User user = MockDataUser.getUser();
        final String password = "new password";
        final String nickname = "new nickname";
        doReturn(user).when(userRepository).findByLogin(user.getLogin());
        final String answer = userService.changeNicknameAndPass(nickname, password, user.getLogin());

        assertEquals(LanguageMessage.getText("successfully"), answer);
        assertEquals(nickname, user.getNickname());
        assertNotEquals("password", user.getPassword());
        verify(nicknameChangeHistoryRepository, times(1)).save(any(NicknameChangeHistory.class));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void updateUserLoginAdmin() {
        final String login = "new login";
        final User user = MockDataUser.getUser();
        doReturn(false).when(userRepository).existsByLogin(user.getLogin());
        doReturn(user).when(userRepository).findByLogin(user.getLogin());
        doReturn(true).when(userRepository).existsByLogin(login);
        String answer = userService.updateUserLoginAdmin(login, user.getLogin());

        assertEquals(LanguageMessage.getText("user.not.found"), answer);
        doReturn(true).when(userRepository).existsByLogin(user.getLogin());
        answer = userService.updateUserLoginAdmin(user.getLogin(), user.getLogin());

        assertEquals(LanguageMessage.getText("error.create"), answer);
        answer = userService.updateUserLoginAdmin(login, user.getLogin());

        assertEquals(LanguageMessage.getText("user.exists"), answer);
        doReturn(false).when(userRepository).existsByLogin(login);
        answer = userService.updateUserLoginAdmin(login, user.getLogin());

        assertEquals(LanguageMessage.getText("successfully"), answer);
        assertEquals(login, user.getLogin());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void updateUserNicknameAdmin() {
        final String nickname = "new nickname";
        final User user = MockDataUser.getUser();
        doReturn(false).when(userRepository).existsByLogin(user.getLogin());
        doReturn(user).when(userRepository).findByLogin(user.getLogin());
        String answer = userService.updateUserNicknameAdmin(nickname, user.getLogin());

        assertEquals(LanguageMessage.getText("user.not.found"), answer);
        doReturn(true).when(userRepository).existsByLogin(user.getLogin());
        answer = userService.updateUserNicknameAdmin(user.getNickname(), user.getLogin());

        assertEquals(LanguageMessage.getText("error.create"), answer);
        answer = userService.updateUserNicknameAdmin(nickname, user.getLogin());

        assertEquals(user.getNickname(), nickname);
        verify(userRepository, times(1)).save(user);
        verify(nicknameChangeHistoryRepository, times(1)).save(any(NicknameChangeHistory.class));
        assertEquals(LanguageMessage.getText("successfully"), answer);
    }

    @Test
    public void getHistory() {
        userService.getHistory();
        verify(nicknameChangeHistoryRepository, times(1)).findAll();
    }

    @Test
    public void getUserList() {
        final User user1 = MockDataUser.getUser();
        final User user2 = new User();
        final UserDto userDto1 = MockDataUser.getUserDto();
        final UserDto userDto2 = new UserDto();
        userDto1.setLogin("first");
        userDto2.setLogin("second");
        final List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        doReturn(list).when(userRepository).findAll();
        doReturn(userDto1).when(userConverter).convertToDto(user1);
        doReturn(userDto2).when(userConverter).convertToDto(user2);
        List<UserDto> answer = userService.getUserList(null);

        assertEquals(2, answer.size());
        assertEquals(userDto1, answer.get(0));
        assertEquals(userDto2, answer.get(1));
        answer = userService.getUserList("e");

        assertEquals(1, answer.size());
        assertEquals(userDto2, answer.get(0));
        verify(userRepository, times(2)).findAll();
        verify(userConverter, times(4)).convertToDto(any(User.class));
    }

    @Test
    public void getUserCount(){
        final User user1 = MockDataUser.getUser();
        final User user2 = new User();
        final UserDto userDto1 = MockDataUser.getUserDto();
        final UserDto userDto2 = new UserDto();
        userDto1.setLogin("first");
        userDto2.setLogin("second");
        final List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        doReturn((long)list.size()).when(userRepository).count();
        doReturn(list).when(userRepository).findAll();
        doReturn(userDto1).when(userConverter).convertToDto(user1);
        doReturn(userDto2).when(userConverter).convertToDto(user2);
        Long answer = userService.getUserCount(null);

        assertEquals((long)list.size(), (long)answer);
        verify(userRepository, times(1)).count();

        answer = userService.getUserCount("e");
        assertEquals((long)1, (long)answer);
    }

    @Test
    public void deleteUser() {
        final User user = MockDataUser.getUser();
        doReturn(false).when(userRepository).existsByLogin(user.getLogin());
        doReturn(user).when(userRepository).findByLogin(user.getLogin());
        String answer = userService.deleteUser(user.getLogin());

        assertEquals(LanguageMessage.getText("user.not.found"), answer);
        doReturn(true).when(userRepository).existsByLogin(user.getLogin());
        answer = userService.deleteUser(user.getLogin());

        assertEquals(LanguageMessage.getText("successfully"), answer);
        verify(userRepository, times(1)).delete(user);

        user.setLogin(UserDto.ROOT);
        answer = userService.deleteUser(user.getLogin());
        assertEquals(LanguageMessage.getText("access.error"), answer);
    }

    @Test
    public void getUser(){
        final String name = "name";
        userService.getUser(name);
        verify(userRepository, times(1)).findByLogin(name);
    }

    @Test
    public void changeRoleUser(){
        final User user = MockDataUser.getUser();
        doReturn(false).when(userRepository).existsByLogin(user.getLogin());
        doReturn(user).when(userRepository).findByLogin(user.getLogin());
        String answer = userService.changeRoleUser(user.getLogin());

        assertEquals(LanguageMessage.getText("user.not.found"), answer);
        doReturn(true).when(userRepository).existsByLogin(user.getLogin());
        answer = userService.changeRoleUser(user.getLogin());

        assertEquals(LanguageMessage.getText("successfully"), answer);
        assertEquals(RoleEnum.ROLE_ADMIN.toString(), user.getRole());
        answer = userService.changeRoleUser(user.getLogin());

        assertEquals(LanguageMessage.getText("successfully"), answer);
        assertEquals(RoleEnum.ROLE_USER.toString(), user.getRole());
        user.setLogin(UserDto.ROOT);
        answer = userService.changeRoleUser(user.getLogin());

        assertEquals(LanguageMessage.getText("access.error"), answer);
        verify(userRepository, times(2)).save(user);
    }

    @Test
    public void getNameUserList(){
        final User user1 = MockDataUser.getUser();
        final User user2 = new User();
        final List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        user1.setLogin("first");
        user2.setLogin("second");
        doReturn(list).when(userRepository).findAll();
        final List<String> answer = userService.getNameUserList();

        assertEquals(list.size(), answer.size());
        assertEquals(user1.getLogin(), answer.get(0));
        assertEquals(user2.getLogin(), answer.get(1));
        verify(userRepository, times(1)).findAll();
    }
}
