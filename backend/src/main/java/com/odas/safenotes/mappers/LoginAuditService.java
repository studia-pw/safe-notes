package com.odas.safenotes.mappers;

import com.odas.safenotes.domain.LoginAudit;
import com.odas.safenotes.repositories.LoginAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginAuditService {

    private final LoginAuditRepository loginAuditRepository;

    @Transactional
    public void saveLoginAudit(LoginAudit loginAudit) {
        loginAuditRepository.save(loginAudit);
    }
}
