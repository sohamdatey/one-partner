package in.onepartner.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import in.onepartner.exception.ImageSaveException;

public class S3Handler {
	private static Logger log = Logger.getLogger(S3Handler.class);

	public static void main(String[] args) {
		setCreds();
	}

	public static void uploadProductImage(InputStream file, String imageId) throws ImageSaveException {
		log.debug("uploadProductImage Method Started");
		final AmazonS3 s3 = builder();
		try {
			setCreds();
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType("image/jpeg");
			s3.putObject(getBucketName(), imageId, file, objectMetadata);
		} catch (AmazonServiceException e) {
			log.error(e);
			throw e;
		}
		log.debug("uploadProductImage Method ended");
	}

	public static void deleteImage(String imageId) {
		log.debug("deleteImage Method Started");
		final AmazonS3 s3 = builder();
		try {
			setCreds();
			s3.deleteObject(getBucketName(), imageId);
		} catch (AmazonServiceException e) {
			log.error(e);
			throw e;
		}
		log.debug("deleteImage Method ended");
	}

	public static String getUrl(String imageName) {
		log.debug("getUrl Method Started");
		return rb.getString("aws.cloudfront.imageurl") + imageName;
	}

	private static AmazonS3 builder() {
		log.debug("AmazonS3 builder Method  Started");
		return AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
	}

	private static final ResourceBundle rb = ResourceBundle
			.getBundle("in.co.raystech.maven.project4.bundle.awscredentials");

	private static String getBucketName() {
		return rb.getString("aws.bucketname");

	}

	private static void setCreds() {

		System.setProperty("aws.accessKeyId",
				new String(Base64.getDecoder().decode(rb.getString("aws.accessKeyId")), StandardCharsets.UTF_8));
		System.setProperty("aws.secretKey",
				new String(Base64.getDecoder().decode(rb.getString("aws.secretKey")), StandardCharsets.UTF_8));
	}

}
