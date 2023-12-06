package com.blocker.blocker_server.user.domain;

import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.chat.domain.ChatUser;
import com.blocker.blocker_server.commons.BaseEntity;
import com.blocker.blocker_server.signature.domain.Signature;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "USER")
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Id
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String picture;

    @Setter
    @Column(name = "refreshtoken_value", unique = true) // UUID를 사용 => 중복될 확률 매ㅐㅐ우 희박. 유니크키로 설정해서 검색 속도 올림.
    private String refreshtokenValue;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Signature> signatures = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgreementSign> agreementSigns = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatUser> chatUsers = new ArrayList<>();

    @Builder
    public User(String email, String name, String picture, String refreshtokenValue,List<String> roles) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.refreshtokenValue = refreshtokenValue;
        this.roles = roles;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePicture(String picture) {
        this.picture = picture;
    }

    public void updateRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
