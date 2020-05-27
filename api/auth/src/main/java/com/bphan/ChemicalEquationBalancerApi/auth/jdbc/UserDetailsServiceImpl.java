package com.bphan.ChemicalEquationBalancerApi.auth.jdbc;

import com.bphan.ChemicalEquationBalancerApi.common.auth.AppUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser appUser = userRepository.getUserWithUsername(username);

    if (appUser != null) {
      List<GrantedAuthority> grantedAuthorities =
          AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + appUser.getRole());

      return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
    }

    throw new UsernameNotFoundException("Username: " + username + " not found");
  }
}
