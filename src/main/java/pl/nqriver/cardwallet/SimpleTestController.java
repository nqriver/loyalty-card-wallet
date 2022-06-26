package pl.nqriver.cardwallet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleTestController {

    @GetMapping("/")
    public ResponseEntity<Object> getTestResponse() {
        return ResponseEntity.ok("Test is success");
    }

    @GetMapping("/dummy")
    public ResponseEntity<Object> getDummyResponse() {
        return ResponseEntity.ok("This is dummy");
    }

}
