package com.sampleLdap.auth;

import com.sampleLdap.auth.Exception.ApiResponse;
import com.sampleLdap.auth.Exception.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
public class Controller {

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addNewUser(@RequestBody LdapUser ldapUser) {
        userService.addUser(ldapUser);
        Map data = new HashMap();
        data.put(Constants.DATA, Constants.USER_ADDED);
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, HttpStatus.OK.value()));
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse> fetchUser(HttpServletRequest req) throws Exception {
        Map data = new HashMap();
        data.put(Constants.DATA, userService.fetchUser(req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, HttpStatus.OK.value()));
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updateUser(HttpServletRequest req, @RequestBody Map<String, String> attributes) throws Exception {
        Map data = new HashMap();
        data.put(Constants.DATA, userService.updateUser(req, attributes));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, HttpStatus.OK.value()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteUser(HttpServletRequest req) throws Exception {
        userService.deleteUser(req);
        Map data = new HashMap();
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, HttpStatus.OK.value()));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> signIn(@RequestBody SignInRequestModel signInRequestModel, HttpServletResponse res) throws Exception {
        boolean status = userService.signIn(signInRequestModel, res);
        Map data = new HashMap();
        if(status)
            return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, HttpStatus.OK.value()));
        else
            throw new AccessDeniedException("");
    }
}
