package com.bphan.ChemicalEquationBalancerApi.auth.jdbc;

import java.util.List;

import com.bphan.ChemicalEquationBalancerApi.auth.models.AuthenticationResponse;
import com.bphan.ChemicalEquationBalancerApi.common.auth.AppUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AuthenticationResponse createUser(AppUser user) {
        AuthenticationResponse response;

        if (userWithUsernameExists(user.getUsername())) {
            response = new AuthenticationResponse("error", "User with username already exists");
        } else {
            try {
                String query = "INSERT INTO Users (id, username, password, role) VALUES (?, ?, ?, ?)";

                int changedRows = jdbcTemplate.update(query, user.getId(), user.getUsername(),
                        passwordEncoder.encode(user.getPassword()), user.getRole());

                if (changedRows > 0) {
                    response = new AuthenticationResponse("success", "");
                } else {
                    response = new AuthenticationResponse("error", "SQL error");
                }
            } catch (Exception e) {
                String errorMessage = e.getLocalizedMessage();
                // logger.log(Level.WARNING, errorMessage);
                e.printStackTrace();
                response = new AuthenticationResponse("error", errorMessage);
            }
        }

        return response;
    }

    public boolean userWithUsernameExists(String username) {
        String query = "SELECT count(*) FROM Users WHERE username=?";

        return jdbcTemplate.queryForObject(query, new Object[] { username }, Integer.class) > 0;
    }

    public AppUser getUserWithUsername(String username) {
        List<AppUser> appUsers = jdbcTemplate.query("SELECT * FROM Users WHERE username='" + username + "'",
                (resource, rowNum) -> new AppUser(resource.getString("id"), resource.getString("username"),
                        resource.getString("password"), resource.getString("role")));

        return appUsers != null && appUsers.size() > 0 ? appUsers.get(0) : null;
    }
}