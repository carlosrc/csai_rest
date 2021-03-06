package es.udc.csai.rest.config.security.jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import es.udc.csai.rest.config.ConstantsProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class TokenProvider {

	private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

	private static final String AUTHORITIES_KEY = "auth";

	private String secretKey;

	private long tokenValidityInSeconds;

	private long tokenValidityInSecondsForRememberMe;

	private ConstantsProperties constantsProperties = new ConstantsProperties();

	@PostConstruct
	public void init() {
		// Inicializa las constantes con las que se creará el JWT (la clave
		// secreta y los segundos de validez).
		this.secretKey = constantsProperties.getSecurity().getAuthentication().getJwt().getSecret();

		this.tokenValidityInSeconds = 1000
				* constantsProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
		this.tokenValidityInSecondsForRememberMe = 1000 * constantsProperties.getSecurity().getAuthentication().getJwt()
				.getTokenValidityInSecondsForRememberMe();
	}

	// Crea un JWT a partir de los datos de usuario, firmándolo con el algoritmo
	// HS512
	public String createToken(Authentication authentication, Boolean rememberMe) {
		String authorities = authentication.getAuthorities().stream().map(authority -> authority.getAuthority())
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity;
		if (rememberMe) {
			validity = new Date(now + this.tokenValidityInSecondsForRememberMe);
		} else {
			validity = new Date(now + this.tokenValidityInSeconds);
		}

		return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities)
				.signWith(SignatureAlgorithm.HS512, secretKey).setExpiration(validity).compact();
	}

	// Devuelve un objeto Authentication a partir de un JWT
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

		String principal = claims.getSubject();

		Collection<? extends GrantedAuthority> authorities = Arrays
				.asList(claims.get(AUTHORITIES_KEY).toString().split(",")).stream()
				.map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	// Valida un JWT
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			log.info("Invalid JWT signature: " + e.getMessage());
			return false;
		}
	}
}
