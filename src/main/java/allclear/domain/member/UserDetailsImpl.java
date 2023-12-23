package allclear.domain.member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final Member user;

    public UserDetailsImpl(Member user) {
        this.user = user;
    }

    public Member getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getEmail();
    }

    @Override
    public String getUsername() {
        return null;
    }

    /* 계정 만료 여부
     * true :  만료 안됨
     * false : 만료
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /* 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /* 비밀번호 만료 여부
     * true : 만료 안 됨
     * false : 만료
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /* 사용자 활성화 여부
     * true : 활성화 됨
     * false : 활성화 안 됨
     */
    @Override
    public boolean isEnabled() {
        return false;
    }
}
