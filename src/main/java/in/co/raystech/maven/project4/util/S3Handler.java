package in.co.raystech.maven.project4.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;

import in.co.raystech.maven.project4.exception.ImageSaveException;

public class S3Handler {

	public static void main(String[] args) {
		// uploadProductImage();
		getUrl();

	}

	public static void uploadProductImage(InputStream file) throws ImageSaveException {
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		try {
			setvarsYash();
			Random rand = new Random();
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType("image/jpeg");
			PutObjectResult putObject = s3.putObject(getBucketName(), "img" + rand.nextInt(1000) + ".jpg", file,
					objectMetadata);
			System.out.println("naya chalaa hain..");
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		}
	}

	public static void uploadProductImage(File file) {
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		try {
			setvarsYash();
			Random rand = new Random();
			PutObjectResult putObject = s3.putObject(getBucketName(), "img" + rand.nextInt(1000) + ".jpeg", file);

		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		}
	}

	public static void deleteImage() {
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		try {
			setvarsYash();
			s3.deleteObject(getBucketName(), "harvey mc with args in code");
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		}
	}

	public static void getUrl() {
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		try {
			setvarsYash();
			URL url = s3.getUrl(getBucketName(), "harvey mc with args in code");
			System.out.println(url.toString());
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		}
	}

	private static String getYashBucketName() {
		return "one-partner-test-one";
	}

	private static String getSohamBucketName() {
		return "test-bucket-soham-dev";
	}

	private static String getBucketName() {
		return getYashBucketName();
	}

	private static void setvarsSoham() {
		System.setProperty("aws.accessKeyId", "AKIAU4WYGB4D3XAY6V6G");
		System.setProperty("aws.secretKey", "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");
	}

	private static void setvarsYash() {
		System.setProperty("aws.accessKeyId", "AKIAUGU4WNIJDORR4TIJ");
		System.setProperty("aws.secretKey", "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");
	}

}
