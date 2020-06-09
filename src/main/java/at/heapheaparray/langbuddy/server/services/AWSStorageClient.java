package at.heapheaparray.langbuddy.server.services;

import at.heapheaparray.langbuddy.server.services.interfaces.StorageClient;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Region;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
@Log4j2
public class AWSStorageClient implements StorageClient {

    private AmazonS3 s3client;

    @Value("${aws.access-key-secret}")
    private String secretKey;

    @Value("${aws.access-key-id}")
    private String accessKey;

    @Value("${aws.profile-pic-bucket}")
    private String profilePicBucket;

    private static final String FILE_PATH_PREFIX = "https://s3-"+ Region.EU_Frankfurt+".amazonaws.com/";

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard().withRegion(Region.EU_Frankfurt.toString())
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    @Override
    public String uploadProfilePicture(MultipartFile file, String fileName) {
        return uploadFile(file, fileName, profilePicBucket);
    }
    private String uploadFile(MultipartFile multipartFile, String fileName, String bucketName) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            uploadFileToS3bucket(fileName, file, bucketName);
            fileUrl = FILE_PATH_PREFIX + bucketName + "/" + fileName;
            file.delete();
            return fileUrl;
        } catch (Exception e) {
            log.error("Uploading file " + multipartFile.getOriginalFilename() + " to " + bucketName+ " failed!", e);
            e.printStackTrace();
            return null;
        }
    }
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    private void uploadFileToS3bucket(String fileName, File file, String bucket) {
        PutObjectResult putObjectResult = s3client.putObject(new PutObjectRequest(bucket, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

}
