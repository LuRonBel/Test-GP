package com.patskevich.gpproject.Service;

import com.patskevich.gpproject.MockDataRoom;
import com.patskevich.gpproject.MockDataUser;
import com.patskevich.gpproject.converter.UserConverter;
import com.patskevich.gpproject.dto.RoomDto.NameRoomDto;
import com.patskevich.gpproject.dto.UserDto.CreateUserDto;
import com.patskevich.gpproject.dto.UserDto.UpdateUserDto;
import com.patskevich.gpproject.dto.UserDto.UserDto;
import com.patskevich.gpproject.dto.UserDto.UserNameDto;
import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import com.patskevich.gpproject.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserConverter userConverter;

    @Test
    public void createUser() {

        final CreateUserDto createUserDto = MockDataUser.getCreateUserDto();

        doReturn(false).when(userRepository).existsByLogin(createUserDto.getLogin());

        final String answer1 = userService.createUser(createUserDto);
        verify(userRepository, times(1)).save(null);
        assertEquals("Пользователь "+createUserDto.getLogin()+" был создан!", answer1);

        doReturn(true).when(userRepository).existsByLogin(createUserDto.getLogin());

        final String answer2 = userService.createUser(createUserDto);

        assertEquals("Пользователь "+createUserDto.getLogin()+" уже существует!", answer2);
    }

  /*  @Test
    public void changeUserNickname() {

        final UpdateUserDto updateUserDto = MockDataUser.getUpdateUserDto();
        final User user = MockDataUser.getUser();
        final String oldNickname = user.getNickname();

        doReturn(user).when(userRepository).findByLogin(user.getLogin());

        final String answer = userService.changeUserNickname(updateUserDto, user.getLogin());

        assertEquals(updateUserDto.getNewNickname(), user.getNickname());
        assertEquals("Никнейм пользователя "+user.getLogin()+" был изменён!", answer);
        verify(nicknameLogService, times(1)).createLog(user.getLogin(),oldNickname,updateUserDto.getNewNickname());
        verify(userRepository, times(1)).save(user);
    } */

    @Test
    public void getUserList() {

        final List<UserDto> userList = userService.getUserList();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void deleteUser() {

        final UserNameDto userNameDto = MockDataUser.getUserNameDto();

        doReturn(true).when(userRepository).existsByLogin(userNameDto.getName());

        final String answer1 = userService.deleteUser(userNameDto);
        verify(userRepository, times(1)).delete(null);
        assertEquals("Пользователь "+userNameDto.getName()+" был удален!", answer1);

        doReturn(false).when(userRepository).existsByLogin(userNameDto.getName());

        final String answer2 = userService.deleteUser(userNameDto);

        assertEquals("Пользователя "+userNameDto.getName()+" не существует!", answer2);
    }

    @Test
    public void getUser(){

        final String name = new String("name");
        userService.getUser(name);
        verify(userRepository, times(1)).findByLogin(name);
    }

    @Test
    public void changeRoom(){

        final NameRoomDto nameRoomDto = MockDataRoom.getNameRoomDto();
        final User user = MockDataUser.getUser();
        final Room room = MockDataRoom.getRoom();

        doReturn(true).when(roomRepository).existsByName(nameRoomDto.getName());
        doReturn(user).when(userRepository).findByLogin(user.getLogin());
        doReturn(room).when(roomRepository).findByName(nameRoomDto.getName());

        final String answer1 = userService.changeRoom(nameRoomDto, user.getLogin());

        assertEquals(room, user.getRoom());
        assertEquals("Пользователь "+user.getLogin()+" переместился в комнату "+nameRoomDto.getName()+"!", answer1);
        verify(userRepository, times(1)).save(user);

        doReturn(false).when(roomRepository).existsByName(nameRoomDto.getName());

        final String answer2 = userService.changeRoom(nameRoomDto, user.getLogin());

        assertEquals("Комнаты с именем "+nameRoomDto.getName()+" не существует!", answer2);
    }

    @Test
    public void changeRoleUser() {

        final UserNameDto userNameDto = MockDataUser.getUserNameDto();
        final User user = MockDataUser.getUser();


        doReturn(true).when(userRepository).existsByLogin(userNameDto.getName());
        doReturn(user).when(userRepository).findByLogin(userNameDto.getName());

        final String answer1 = userService.changeRoleUser(userNameDto);

        assertEquals("ROLE_ADMIN", user.getRole());

        final String answer2 = userService.changeRoleUser(userNameDto);

        assertEquals("ROLE_USER", user.getRole());
        verify(userRepository, times(2)).save(user);
    }

    @Test
    public void getNameUserList(){

        final List<String> list = userService.getNameUserList();

        verify(userRepository, times(1)).findAll();
    }
}
