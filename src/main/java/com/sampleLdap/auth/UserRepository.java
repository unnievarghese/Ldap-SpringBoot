package com.sampleLdap.auth;

import org.springframework.data.ldap.repository.LdapRepository;

public interface UserRepository extends LdapRepository<LdapUser> {

}

