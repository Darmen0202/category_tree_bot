package com.example.category_tree.command;


import com.example.category_tree.entity.Role;
import com.example.category_tree.service.CategoryService;
import com.example.category_tree.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.Mockito.*;

public class BotCommandsHandlerTest {

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private AbsSender absSender;

    @InjectMocks
    private BotCommandsHandler botCommandsHandler;

    private Update update;
    private Message message;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Setup the common objects
        user = mock(User.class);
        message = mock(Message.class);
        update = mock(Update.class);

        when(update.getMessage()).thenReturn(message);
        when(message.getFrom()).thenReturn(user);
    }

    @Test
    void whenAddElementCommandReceivedByAdmin_thenCategoryIsAdded() {
        when(user.getId()).thenReturn(933005813L); // Admin user ID
        when(message.getText()).thenReturn("/addElement testCategory");
        when(userService.isUserAdmin(933005813L)).thenReturn(true);

        botCommandsHandler.handleCommand(update, absSender);

        verify(categoryService).addCategory("testCategory", null);
    }

    @Test
    void whenHelpCommandReceived_thenHelpMessageIsSent() {
        when(user.getId()).thenReturn(933005813L); // Any user ID
        when(message.getText()).thenReturn("/help");

        botCommandsHandler.handleCommand(update, absSender);

        try {
            verify(absSender).execute((EditMessageMedia) any());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void whenViewTreeCommandReceived_thenTreeIsViewed() {
        when(user.getId()).thenReturn(933005813L); // Any user ID
        when(message.getText()).thenReturn("/viewTree");

        botCommandsHandler.handleCommand(update, absSender);

        verify(categoryService).viewTree();
    }

    @Test
    void whenRemoveElementCommandReceivedByAdmin_thenCategoryIsRemoved() {
        when(user.getId()).thenReturn(933005813L); // Admin user ID
        when(message.getText()).thenReturn("/removeElement testCategory");
        when(userService.isUserAdmin(933005813L)).thenReturn(true);

        botCommandsHandler.handleCommand(update, absSender);

        verify(categoryService).removeCategory("testCategory");
    }

    @Test
    void whenSetRoleCommandReceivedByAdmin_thenUserRoleIsSet() {
        when(user.getId()).thenReturn(933005813L); // Admin user ID
        when(message.getText()).thenReturn("/setRole 789012 USER");
        when(userService.isUserAdmin(933005813L)).thenReturn(true);

        botCommandsHandler.handleCommand(update, absSender);

        verify(userService).changeUserRole(789012L, Role.USER);
    }

}