package io.morethan.dagger_example.domain.user;

import io.morethan.dagger_example.mail.MailService;

public class RegistrationManager {

    private final UserRepository _userRepository;
    private final MailService _mailService;

    public RegistrationManager(UserRepository userRepository, MailService mailService) {
        _userRepository = userRepository;
        _mailService = mailService;
    }

    public void registerUser(String user) {
        _userRepository.persist(user);
        _mailService.sendMail("Registered user " + user);
    }

}
