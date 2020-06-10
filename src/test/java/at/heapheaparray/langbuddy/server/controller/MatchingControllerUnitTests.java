package at.heapheaparray.langbuddy.server.controller;

import at.heapheaparray.langbuddy.server.dao.models.User;
import at.heapheaparray.langbuddy.server.dao.repositories.UserRepository;
import at.heapheaparray.langbuddy.server.dto.response.UserSuggestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MatchingController.class})
public class MatchingControllerUnitTests {
    @Autowired
    private MatchingController matchingController;
    @MockBean
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    private void setupUsers() {
        user1 = User.builder().id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max.mustermann@gmx.net")
                .build();

        user2 = User.builder().id(2L)
                .firstName("Maria")
                .lastName("Musterfrau")
                .email("maria.musterfrau@gmx.net")
                .build();

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user1));
        when(userRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(user2));
    }

    @Test
    public void testGetOpenMatchesFilterContainsUserId() {
        matchingController.getOpenMatches(1L);
        verify(userRepository).findDistinctBySpokenLanguagesInAndIdNotIn(argThat(langs -> true),
                argThat(filters -> filters.contains(1L)));
    }

    @Test
    public void testGetOpenMatchesFilterContainsMatchedIds() {
        user1.setMatchedUsers(Collections.singleton(user2));
        matchingController.getOpenMatches(1L);
        verify(userRepository).findDistinctBySpokenLanguagesInAndIdNotIn(argThat(langs -> true),
                argThat(filters -> filters.contains(2L)));
    }

    @Test
    public void testGetOpenMatchesFilterContainsDiscardedIds() {
        user1.setDiscardedUsers(Collections.singleton(user2));
        matchingController.getOpenMatches(1L);
        verify(userRepository).findDistinctBySpokenLanguagesInAndIdNotIn(argThat(langs -> true),
                argThat(filters -> filters.contains(2L)));
    }

    @Test
    public void testDiscardMatch() {
        user1.setDiscardedUsers(new HashSet<>());
        user2.setDiscardedUsers(new HashSet<>());
        matchingController.discardMatch(1L, 2L);

        assertThat(user1.getDiscardedUsers().contains(user2));
        assertThat(user2.getDiscardedUsers().contains(user1));
    }

    @Test
    public void testAcceptMatch() {
        user1.setMatchedUsers(new HashSet<>());
        user2.setMatchedUsers(new HashSet<>());
        matchingController.acceptMatch(1L, 2L);

        assertThat(user1.getMatchedUsers().contains(user2));
        assertThat(user2.getMatchedUsers().contains(user1));
    }

    @Test
    public void testGetMatchesNoMatches() {
        assertEquals(0, matchingController.getMatches(1L).size());
    }

    @Test
    public void testGetMatches() {
        user1.setMatchedUsers(Collections.singleton(user2));
        List<UserSuggestion> result = matchingController.getMatches(1L);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getUserId());
    }
}
