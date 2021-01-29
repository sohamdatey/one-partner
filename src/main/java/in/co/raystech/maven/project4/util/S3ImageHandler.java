package in.co.raystech.maven.project4.util;

import java.io.File;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3ImageHandler {

	private String getBucketName() {
		return PropertyReader.getValue("productImagesBucketName");
	}

	public static void main(String[] args) {
		String filePath = "C:\\Users\\ramaw\\.aws\\credentials";
		new S3ImageHandler().uploadProductImage(filePath);
		// new File(filePath);
	}

	public void uploadProductImage(String filePath) {

		System.out.format("Uploading %s to S3 bucket %s...\n", filePath, getBucketName());
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
				// .withCredentials(new ProfileCredentialsProvider())
				.withRegion(Regions.US_EAST_1).build();
		try {
			s3.putObject(getBucketName(), "key", new File(filePath));
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);

		}
	}

}
