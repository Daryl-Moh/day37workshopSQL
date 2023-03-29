package sg.edu.nus.iss.day37workshopSQL.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.day37workshopSQL.model.Post;
import sg.edu.nus.iss.day37workshopSQL.services.FileUploadService;
import sg.edu.nus.iss.day37workshopSQL.services.S3Service;

@Controller
public class FileUploadController {
    @Autowired
    private S3Service s3svc;

    @Autowired
    private FileUploadService flSvc;
    private static final String BASE64_PREFIX_DECODER = "data:image/png;base64,";

    @PostMapping(path = "/upload-ng", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin()
    public ResponseEntity<String> uploadForAngular(
            @RequestPart MultipartFile imageFile,
            @RequestPart String title,
            @RequestPart String complain) throws SQLException {
        String key = "";
        System.out.printf("title: %s", title);
        System.out.printf("complain: %s", complain);

        try {
            key = s3svc.upload(imageFile);
            flSvc.uploadBlob(imageFile, title, complain);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject payload = Json.createObjectBuilder()
                .add("imagekey", key)
                .build();

        return ResponseEntity.ok(payload.toString());
    }

    @PostMapping(path = "/upload-tf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadTF(@RequestPart MultipartFile myfile, @RequestPart String name,
            Model model) {
        String key = "";
        try {
            key = s3svc.upload(myfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("name", name);
        model.addAttribute("file", myfile);
        model.addAttribute("key", key);

        return "upload";
    }

    @GetMapping(path = "/get-image/{postId}")
    @CrossOrigin()
    public ResponseEntity<String> retrieveImage(@PathVariable Integer postId, Model model) {
        Optional<Post> opt = flSvc.getPostById(postId);
        Post p = opt.get();
        String encodedString = Base64.getEncoder().encodeToString(p.getImage());
        JsonObject payload = Json.createObjectBuilder()
                .add("image", BASE64_PREFIX_DECODER + encodedString)
                .build();
        return ResponseEntity.ok(payload.toString());
    }
}
