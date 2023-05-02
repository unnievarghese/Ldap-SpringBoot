package com.sampleLdap.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    Config config;

    @Autowired
    LdapAuthentication ldapAuthentication;

    @Autowired
    Utils utils;

    public void addUser(LdapUser ldapUser) {

        DirContext connection = config.makeConnection();
        try {
            connection.createSubcontext("cn="+utils.buildCn()+",ou=People,dc=sample,dc=com", createLdapAttribute(ldapUser));
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public UserResponseModel fetchUser(HttpServletRequest req) throws Exception {
        DirContext connection = config.makeConnection();
        SearchResult searchResult = findUser(req, connection);
        return convertToUserResponseModel(searchResult);
    }

    public UserResponseModel updateUser(HttpServletRequest req, Map<String, String> attributes) throws Exception {
        DirContext connection = config.makeConnection();
        SearchResult searchResult = findUser(req,connection);

        if(searchResult!=null){
            ModificationItem[] mods = createLdapModificationItems(searchResult, attributes);
            connection.modifyAttributes(searchResult.getName()+",ou=People,dc=sample,dc=com", mods);
        }
        SearchResult updatedSearchResult = findUser(req,connection);
        return convertToUserResponseModel(updatedSearchResult);
    }

    public void deleteUser(HttpServletRequest req) throws Exception {
        DirContext connection = config.makeConnection();
        SearchResult searchResult = findUser(req,connection);

        if(searchResult!=null){
            connection.destroySubcontext(searchResult.getName()+",ou=People,dc=sample,dc=com");
        }
    }

    public SearchResult getDn(String uid) throws NamingException {
        DirContext connection = config.makeConnection();
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String filter = "(uid=" + uid + ")";
        NamingEnumeration<SearchResult> results = connection.search("ou=people,dc=sample,dc=com", filter, controls);
        return results.next();
    }

    public static String getCn (String token){
        token = token.replace("Bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey("jf9i4jgu83nfl0")
                .parseClaimsJws(token).getBody();
        return (String) claims.get("sub");
    }

    public UserResponseModel convertToUserResponseModel(SearchResult searchResult) throws NamingException {
        UserResponseModel userResponseModel = new UserResponseModel();
        userResponseModel.setUid((String) searchResult.getAttributes().get("uid").get());
        userResponseModel.setFirstName((String) searchResult.getAttributes().get("givenName").get());
        userResponseModel.setLastName((String) searchResult.getAttributes().get("sn").get());
        return userResponseModel;
    }

    public Attributes createLdapAttribute(LdapUser ldapUser){
        Attributes attributes = new BasicAttributes();
        Attribute attribute = new BasicAttribute("objectClass");
        attribute.add("inetOrgPerson");
        attributes.put(attribute);
        attributes.put("sn", ldapUser.getSn());
        attributes.put("givenName", ldapUser.getGivenName());
        attributes.put("uid", ldapUser.getUid());
        attributes.put("userPassword", ldapUser.getUserPassword());
        attributes.put("description", "Employee");
        return attributes;
    }

    public ModificationItem[] createLdapModificationItems(SearchResult searchResult, Map<String, String> attributes){
        ModificationItem[] mods = new ModificationItem[attributes.size()];
        int i = 0;
        for (String attributeName : attributes.keySet()) {
            String attributeValue = attributes.get(attributeName);
            mods[i++] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(attributeName, attributeValue));
        }
        return mods;
    }

    public SearchResult findUser(HttpServletRequest req, DirContext connection) throws Exception {

        try {
            String cn = getCn(req.getHeader("Authorization"));
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = "(cn=" + cn + ")";
            NamingEnumeration<SearchResult> results = connection.search("ou=people,dc=sample,dc=com", filter, controls);
            return results.next();
        }
        catch (Exception ex){
            throw new NullPointerException("");
        }
    }
    public boolean signIn(SignInRequestModel signInRequestModel, HttpServletResponse res) throws Exception {
        SearchResult searchResult = getDn(signInRequestModel.getUid());
        String cn = (String) searchResult.getAttributes().get("cn").get();

        boolean status = ldapAuthentication.authenticateUser(cn, signInRequestModel.getUserPassword());
        if(status)
            ldapAuthentication.onSuccessFullAuthentication(cn, res, searchResult);
        return status;
    }
}
