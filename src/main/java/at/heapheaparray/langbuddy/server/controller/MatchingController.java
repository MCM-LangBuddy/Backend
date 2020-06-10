package at.heapheaparray.langbuddy.server.controller;

import at.heapheaparray.langbuddy.server.dao.models.Language;
import at.heapheaparray.langbuddy.server.dao.models.User;
import at.heapheaparray.langbuddy.server.dao.repositories.UserRepository;
import at.heapheaparray.langbuddy.server.dto.response.UserSuggestion;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {
    private final UserRepository userRepository;

    public MatchingController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/openMatches/{userId}")
    public List<UserSuggestion> getOpenMatches(@PathVariable(name = "userId") Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        Set<Long> userFilter = new HashSet<>();
        userFilter.add(user.getId());
        if(user.getDiscardedUsers() != null) {

            userFilter.addAll(user.getDiscardedUsers().stream().map(User::getId).collect(Collectors.toList()));
        }
        if(user.getMatchedUsers() != null) {
            userFilter.addAll(user.getMatchedUsers().stream().map(User::getId).collect(Collectors.toList()));
        }

        return userRepository.findDistinctBySpokenLanguagesInAndIdNotIn(user.getLearningLanguages(), userFilter).stream()
                .map(userRaw -> UserSuggestion.builder().emailAddress(userRaw.getEmail())
                    .learningLanguageDtos(userRaw.getLearningLanguages().stream().map(Language::toDto).collect(Collectors.toSet()))
                    .spokenLanguageDtos(userRaw.getSpokenLanguages().stream().map(Language::toDto).collect(Collectors.toSet()))
                    .userId(userRaw.getId())
                    .firstName(userRaw.getFirstName())
                    .profilePictureUrl(userRaw.getProfilePictureUrl()).build())
                .collect(Collectors.toList());
    }

    @PutMapping("/discardMatch/{userId}")
    public void discardMatch(@PathVariable(name = "userId") Long userId,
                             @RequestParam(name = "otherUserId") Long otherUserId) {
        User currentUser = userRepository.findById(userId).orElseThrow();
        User otherUser = userRepository.findById(otherUserId).orElseThrow();

        currentUser.getDiscardedUsers().add(otherUser);
        otherUser.getDiscardedUsers().add(currentUser);

        userRepository.save(currentUser);
        userRepository.save(otherUser);
    }

    @PutMapping("/acceptMatch/{userId}")
    public void acceptMatch(@PathVariable(name = "userId") Long userId,
                             @RequestParam(name = "otherUserId") Long otherUserId) {
        User currentUser = userRepository.findById(userId).orElseThrow();
        User otherUser = userRepository.findById(otherUserId).orElseThrow();

        currentUser.getMatchedUsers().add(otherUser);
        otherUser.getMatchedUsers().add(currentUser);

        userRepository.save(currentUser);
        userRepository.save(otherUser);
    }

    @GetMapping("/matches/{userId}")
    public List<UserSuggestion> getMatches(@PathVariable(name = "userId") Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        if(user.getMatchedUsers() == null) return Collections.emptyList();

        return user.getMatchedUsers().stream()
                .map(userRaw -> UserSuggestion.builder().emailAddress(userRaw.getEmail())
                        .learningLanguageDtos(userRaw.getLearningLanguages() != null ? userRaw.getLearningLanguages().stream().map(Language::toDto).collect(Collectors.toSet()) : null)
                        .spokenLanguageDtos(userRaw.getSpokenLanguages() != null ? userRaw.getSpokenLanguages().stream().map(Language::toDto).collect(Collectors.toSet()) : null)
                        .userId(userRaw.getId())
                        .firstName(userRaw.getFirstName())
                        .profilePictureUrl(userRaw.getProfilePictureUrl()).build())
                .collect(Collectors.toList());
    }
}