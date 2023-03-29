package sg.edu.nus.iss.day37workshopSQL.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.day37workshopSQL.model.Post;

@Repository
public class FileUploadRepository {
    private static final String INSERT_POSTS_TBL = "INSERT INTO posts (blobc, title, complain) VALUES(?, ?, ?)";
    
    private static final String SQL_GET_POST_BY_POST_ID = 
       "select id, title, complain, blobc from posts where id=?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate template;


    public void uploadBlob(MultipartFile file, String title, String complain) 
            throws SQLException, IOException  {
        
        try (Connection con = dataSource.getConnection();
            PreparedStatement pstmt = con.prepareStatement(INSERT_POSTS_TBL)) {
            InputStream is = file.getInputStream();
            pstmt.setBinaryStream(1, is, file.getSize());
            pstmt.setString(2, title);
            pstmt.setString(3, complain);
            pstmt.executeUpdate();
        }
    }

    public Optional<Post> getPostById(Integer postId){
        return template.query(
            SQL_GET_POST_BY_POST_ID,
            (ResultSet rs) -> {
                if(!rs.next())
                    return Optional.empty();
                final Post post = Post.populate(rs);
                return Optional.of(post);
            },
            postId
        );
    }
}
