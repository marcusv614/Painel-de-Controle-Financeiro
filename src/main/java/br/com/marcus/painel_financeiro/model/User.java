package br.com.marcus.painel_financeiro.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.marcus.painel_financeiro.dto.UserRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID userid;

    @OneToMany(mappedBy="user")
    private List<Account> accounts;
    
    private String username;
    private String password;
    private String email;
    
    @CreationTimestamp
    private Instant creationTimeStamp;
    
    @UpdateTimestamp
    private Instant updateTimeStamp;

    public User(UserRequestDTO dto) {
        this.username = dto.username();
        this.password = dto.password();
        this.email = dto.email();
    }
    
    public User(UUID userid, String username, String password, String email,
            Instant creationTimeStamp, Instant updateTimeStamp) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.creationTimeStamp = creationTimeStamp;
        this.updateTimeStamp = updateTimeStamp;
    }

    public User() {}

    public UUID getUserid() {
        return userid;
    }

    public void setUserid(UUID userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(Instant creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public Instant getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(Instant updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

}
