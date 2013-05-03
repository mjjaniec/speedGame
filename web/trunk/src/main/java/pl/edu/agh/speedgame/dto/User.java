package pl.edu.agh.speedgame.dto;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
    public User() { }

    @Id
    private String login;
    private String password;
    private String avatar;
    private String ring;
    private String email;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getRing() {
        return ring;
    }

    public String getEmail() {
        return email;
    }

    public static class UserBuilder {
        private String login;
        private String password;
        private String avatar;
        private String ring;
        private String email;

        public static UserBuilder fromUser(User user) {
            UserBuilder builder = new UserBuilder().login(user.login)
                                                   .password(user.password)
                                                   .email(user.email)
                                                   .avatar(user.avatar)
                                                   .ring(user.ring);
            return builder;
        }

        public UserBuilder login(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public UserBuilder ring(String ring) {
            this.ring = ring;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            User user = new User();

            user.login = login;
            user.password = password;
            user.avatar = avatar;
            user.ring = ring;
            user.email = email;

            return user;
        }
    }

}
