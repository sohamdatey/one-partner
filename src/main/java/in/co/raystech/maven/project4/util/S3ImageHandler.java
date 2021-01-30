package in.co.raystech.maven.project4.util;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;

public class S3ImageHandler {
	public static void main(String[] args) {
		getUrl();

	}

	public static void uploadProductImage() {
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		try {
			setvars();
			PutObjectResult putObject = s3.putObject("test-bucket-soham-dev", "harvey mc with args in code",
					new File("E:\\wallpapers\\harvey specter lucky.jpg"));

		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		}
	}

	public static void deleteImage() {
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		try {
			setvars();
			s3.deleteObject("test-bucket-soham-dev", "harvey mc with args in code");
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		}
	}

	public static void getUrl() {
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		try {
			setvars();
			URL url = s3.getUrl("test-bucket-soham-dev", "harvey mc with args in code");
			System.out.println(url.toString());
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		}
	}

	public static void setvars() {

		ResourceBundle aws_cred = ResourceBundle.getBundle("in.co.raystech.maven.project4.bundle.awscredentials");

		System.setProperty("aws.accessKeyId", aws_cred.getString("aws.accessKeyId"));
		System.setProperty("aws.secretKey", aws_cred.getString("aws.secretKey"));
	}

}
