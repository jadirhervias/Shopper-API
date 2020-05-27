package com.shopper.shopperapi.auth;

import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationUserService implements UserDetailsService {

//    private final ApplicationUserDao applicationUserDao;
//    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public ApplicationUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @Autowired
//    public ApplicationUserService(@Qualifier("fake") ApplicationUserDao applicationUserDao,
//                                  UserRepository userRepository) {
//        this.applicationUserDao = applicationUserDao;
//        this.userRepository = userRepository;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        user.orElseThrow(() ->
                new UsernameNotFoundException(String.format("Username %s not found", username))
        );

        return user.map(ApplicationUser::new).get();

//        return applicationUserDao
//                .selectApplicationUserByUsername(username)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException(String.format("Username %s not found", username))
//                );
    }
}
