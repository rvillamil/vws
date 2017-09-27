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

@Service
public class AccountDetailsServiceImpl implements UserDetailsService {

	private final AccountRepository accountRepository;

	public AccountDetailsServiceImpl(AccountRepository usuarioRepository) {
		this.accountRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		final Optional<Account> account = accountRepository.findByUserName(userName);

		if (! account.isPresent() ){
			throw new UsernameNotFoundException(userName);
		}
		return new User(account.get().getUserName(), account.get().getPassword(), emptyList());
	}
}
