package ang.neggaw.users.reactiveSecurities.restControllers;

import ang.neggaw.users.entities.UserReact;
import ang.neggaw.users.reactiveSecurities.models.MyHttpResponse;
import ang.neggaw.users.reactiveSecurities.utils.JwtSigner;
import ang.neggaw.users.repositories.UserRepository;
import ang.neggaw.users.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

/**
 * @author ANG
 * @since 13-07-2021 12:02
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Log4j2
public class AuthenticateRestController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtSigner jwtSignerUser;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping(value = "/login")
    public Mono<MyHttpResponse> login(@RequestBody Map<String, String> userData) {

        log.info("Authenticating with username: '{}'", userData.get("username"));
        ObjectMapper objectMapper = new ObjectMapper();

        return Mono.just(userData.get("username"))
                .flatMap(userRepository::findByUsername)
                .doOnNext(infoUser -> log.info("{}", infoUser))
                .filter(userByUsername -> passwordEncoder.matches(userData.get("password"), userByUsername.getPassword()))
                .map(userExists -> {
                    try {
                        return new MyHttpResponse(
                                HttpStatus.OK.value(),
                                "User CONNECTED successfully",
                                jwtSignerUser.createAndGenerateToken(userExists));

                    } catch (Exception e) { // JsonProcessingException
                        log.error("Error authentication/login: " + e.getMessage());
                        return new MyHttpResponse();
                    }
                })
                .onErrorResume(e -> Mono.empty())
                .switchIfEmpty(Mono.just(new MyHttpResponse(HttpStatus.UNAUTHORIZED.value(), "Bad credentials. User with username or password incorrect", null)));
    }

    @GetMapping(value = "/me")
    public Mono<Principal> showMe(Principal principal) {
        return Mono.just(principal);
    }

    @PostMapping(value = "/logout")
    public Mono<MyHttpResponse> logout(@RequestBody UserReact userReact) {
        return Mono.just(userReact)
                .map(userService::createUser)
                .map(userReactMono -> new MyHttpResponse(HttpStatus.OK.value(), "User CREATED successfully", null))
                .onErrorResume(e -> Mono.just(new MyHttpResponse(HttpStatus.UNAUTHORIZED.value(), "User Not CREATED", e)));
    }
}