package passnote.poli.edu.co.PassNote.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import passnote.poli.edu.co.PassNote.entities.Countries;
import passnote.poli.edu.co.PassNote.exceptions.UsernameIsNotUniqueException;
import passnote.poli.edu.co.PassNote.service.UserService;
import passnote.poli.edu.co.PassNote.service.dto.UsersDTO;

import java.util.Date;
import java.util.TimeZone;

@Component
public class Initializer implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${security.user.name}")
    private String defaultUsername;

    @Value("${security.user.password}")
    private String defaultPassword;

    @Value("${app.default.email}")
    private String defaultEmail;

    @Value("${app.default.names}")
    private String defaultNames;

    @Value("${app.default.surnames}")
    private String defaultSurnames;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent cre) {
        try {
            logger.info("starting initializer");
            setTimeZone();
            createDefaultUsers();
            logger.info("finished initializer");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("error during initialization", e);
        }

    }

    private void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-5:00"));
    }

    private void createDefaultUsers() throws UsernameIsNotUniqueException {
        logger.info("creating default users");
        if (!userService.userExist(defaultUsername)) {
            createDefaultUser();
        }
    }

    private void createDefaultUser() throws UsernameIsNotUniqueException {
        UsersDTO user = new UsersDTO();
        user.setUser(defaultUsername);
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setMail(defaultEmail);
        user.setNames(defaultNames);
        user.setIdCountry(Countries.COLOMBIA.toString());
        user.setRegistrationDate(new Date());
        user.setSurnames(defaultSurnames);

        userService.createUser(user);
    }

}


