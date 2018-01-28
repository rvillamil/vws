package es.rvp.web.vws.resources.security;

import static java.util.Collections.emptyList;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.rvp.web.vws.domain.Account;
import es.rvp.web.vws.domain.AccountRepository;

/**
 * The Class AccountDetailsServiceImpl.
 */
@Service
public class AccountDetailsServiceImpl implements UserDetailsService {

    /** The account repository. */
    private final AccountRepository accountRepository;

    /**
     * Instantiates a new account details service impl.
     *
     * @param usuarioRepository the usuario repository
     */
    public AccountDetailsServiceImpl(AccountRepository usuarioRepository) {
        this.accountRepository = usuarioRepository;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String userName) {
        final Optional<Account> account = this.accountRepository.findByUserName(userName);

        if (! account.isPresent() ){
            throw new UsernameNotFoundException(userName);
        }
        return new User(account.get().getUserName(), account.get().getPassword(), emptyList());
    }
}
