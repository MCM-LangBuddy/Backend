package at.heapheaparray.langbuddy.server.dao.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String firstName;
    private String lastName;
    private String userName;

    private String password;
    private String profilePictureUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Language> spokenLanguages = new HashSet<Language>();

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Language> learningLanguages = new HashSet<Language>();

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}
