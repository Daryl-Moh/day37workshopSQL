package sg.edu.nus.iss.day37workshopSQL.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.day37workshopSQL.model.Post;
import sg.edu.nus.iss.day37workshopSQL.repository.FileUploadRepository;

@Service
public class FileUploadService {
    @Autowired
    private FileUploadRepository flRepo;

    public void uploadBlob(MultipartFile file, String title, String complain)
            throws SQLException, IOException {
        flRepo.uploadBlob(file, title, complain);
    }

    public Optional<Post> getPostById(Integer postId) {
        return flRepo.getPostById(postId);
    }
}
