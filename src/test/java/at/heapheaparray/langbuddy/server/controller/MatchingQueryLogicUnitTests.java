package at.heapheaparray.langbuddy.server.controller;

import at.heapheaparray.langbuddy.server.dao.models.Language;
import at.heapheaparray.langbuddy.server.dao.models.User;
import at.heapheaparray.langbuddy.server.dao.repositories.LanguageRepository;
import at.heapheaparray.langbuddy.server.dao.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MatchingQueryLogicUnitTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LanguageRepository languageRepository;

    private static Language english = new Language(1L,"EN","english");
    private static Language german = new Language(2L,"DE","german");
    private static Language french = new Language(3L,"FR","french");

    @BeforeEach
    public void storeLanguages() {
        languageRepository.save(english);
        languageRepository.save(german);
        languageRepository.save(french);
    }

    @Test
    public void testAllSpokenLangMatchingQuery() {
        HashSet<Language> spokenLang1 = new HashSet<>();
        spokenLang1.add(english);
        spokenLang1.add(german);

        User user1 = User.builder()
                .id(1L)
                .spokenLanguages(spokenLang1)
                .build();

        userRepository.save(user1);
        assertEquals(2, userRepository.findById(1L).orElseThrow().getSpokenLanguages().size());

        List<User> result = this.userRepository.findDistinctBySpokenLanguagesInAndIdNotIn(spokenLang1, Collections.singleton(2L));
        assertEquals(1, result.size());
    }

    @Test
    public void testOneSpokenLangMatchingQuery() {
        HashSet<Language> spokenLang1 = new HashSet<>();
        spokenLang1.add(english);
        spokenLang1.add(german);

        User user1 = User.builder()
                .id(1L)
                .spokenLanguages(spokenLang1)
                .build();

        userRepository.save(user1);
        assertEquals(2, userRepository.findById(1L).orElseThrow().getSpokenLanguages().size());

        List<User> result = this.userRepository.findDistinctBySpokenLanguagesInAndIdNotIn(Collections.singleton(english), Collections.singleton(2L));
        assertEquals(1, result.size());
    }

    @Test
    public void testNoSpokenLangMatchingQuery() {
        HashSet<Language> spokenLang1 = new HashSet<>();
        spokenLang1.add(english);
        spokenLang1.add(german);

        User user1 = User.builder()
                .id(1L)
                .spokenLanguages(spokenLang1)
                .build();

        userRepository.save(user1);
        assertEquals(2, userRepository.findById(1L).orElseThrow().getSpokenLanguages().size());

        List<User> result = this.userRepository.findDistinctBySpokenLanguagesInAndIdNotIn(Collections.singleton(french),
                Collections.emptySet());

        assertEquals(0, result.size());
    }

    @Test
    public void testMatchingButIdInFilter() {
        HashSet<Language> spokenLang1 = new HashSet<>();
        spokenLang1.add(english);
        spokenLang1.add(german);

        User user1 = User.builder()
                .id(1L)
                .spokenLanguages(spokenLang1)
                .build();

        userRepository.save(user1);
        assertEquals(2, userRepository.findById(1L).orElseThrow().getSpokenLanguages().size());

        List<User> result = this.userRepository.findDistinctBySpokenLanguagesInAndIdNotIn(Collections.singleton(german),
                Collections.singleton(1L));

        assertEquals(0, result.size());
    }

    @Test
    public void testMatchingAndIdNotInFilter() {
        HashSet<Language> spokenLang1 = new HashSet<>();
        spokenLang1.add(english);
        spokenLang1.add(german);

        User user1 = User.builder()
                .id(1L)
                .spokenLanguages(spokenLang1)
                .build();

        userRepository.save(user1);
        assertEquals(2, userRepository.findById(1L).orElseThrow().getSpokenLanguages().size());

        List<User> result = this.userRepository.findDistinctBySpokenLanguagesInAndIdNotIn(Collections.singleton(german),
                Collections.singleton(2L));

        assertEquals(1, result.size());
    }
}
