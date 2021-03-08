package com.example.demo;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class StreamingController {

    public static final MediaType JSON_SEQ = new MediaType("application", "json-seq");

    @RequestMapping(value = "/", produces = "application/json-seq")
    public ResponseEntity<StreamingResponseBody> handleSeq () {

        return ResponseEntity.ok()
            .contentType(JSON_SEQ)
            .body(out -> {
                for (int i = 0; i < 1000; i++) {
                    out.write(0x1e);
                    out.write("{\"hello\": \"world\"}".getBytes());
                    out.write(0x0a);
                    out.flush();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
