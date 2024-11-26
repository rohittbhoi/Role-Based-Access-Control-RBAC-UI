package baris.RoleBasedAuthBackEnd.controller;

import baris.RoleBasedAuthBackEnd.model.AuthenticationResponse;
import baris.RoleBasedAuthBackEnd.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
public class ShowController {

    @GetMapping("/show")
    public ResponseEntity<String>showAction()
    {
        return ResponseEntity.ok("Secured URL");
    }
    @GetMapping("/admin_only")
    public ResponseEntity<String>adminOnly() throws AccessDeniedException {
        //oturum bilgisini alır
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                return ResponseEntity.ok("Just Admin Login!");
            }
            else
            {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
            }
        }
        else
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");

        }


    }
}
