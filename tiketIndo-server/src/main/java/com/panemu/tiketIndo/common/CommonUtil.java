package com.panemu.tiketIndo.common;

import com.panemu.tiketIndo.rpt.RptAbstractXlsx;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.CollectionUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amrullah
 */
public class CommonUtil {

	private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
	private static boolean DEV_MODE = false;
	private static String WS_DATE_FORMAT = "yyyy-MM-dd";
	private static String WS_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm";
	private static String WS_TIMESTAMP_SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static Locale locale = new Locale("in", "ID");

	static {
		URL devUrl = CommonUtil.class.getResource("/development.txt");
		DEV_MODE = devUrl != null;
	}

	public static boolean isDevMode() {
		return DEV_MODE;
	}

	public static String getExtensionWithDot(String filename) {
		int dotIndex = filename.lastIndexOf(".");
		if (dotIndex < 0) {
			throw new RuntimeException("File has no extension: " + filename);
		}
		return filename.substring(dotIndex);
	}

	public static boolean isImageExtension(String ext) {
		ext = ext.replace(".", "");
		return ext.equalsIgnoreCase("jpg")
				  || ext.equalsIgnoreCase("jpeg")
				  || ext.equalsIgnoreCase("gif")
				  || ext.equalsIgnoreCase("png")
				  || ext.equalsIgnoreCase("tiff")
				  || ext.equalsIgnoreCase("bmp");
	}

	public static String getExtension(String filename) {
		int dotIndex = filename.lastIndexOf(".");
		if (dotIndex < 0) {
			throw new RuntimeException("File has no extension: " + filename);
		}
		return filename.substring(dotIndex + 1);
	}

	public static Date toDate(String yyyy_MM_dd) {
		if (StringUtils.isNotBlank(yyyy_MM_dd)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(WS_DATE_FORMAT);
				return sdf.parse(yyyy_MM_dd);
			} catch (ParseException ex) {
				log.error("Failed to parse " + yyyy_MM_dd + " as date. " + ex.getMessage(), ex);
				return null;
			}
		}
		return null;
	}

	public static String toString_yyyy_MM_dd(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(WS_DATE_FORMAT);
		return sdf.format(date);
	}

	public static Date toTimeStamp(String yyyy_MM_dd_HH_mm_ss) {
		if (StringUtils.isNotBlank(yyyy_MM_dd_HH_mm_ss)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(WS_TIMESTAMP_FORMAT);
				return sdf.parse(yyyy_MM_dd_HH_mm_ss);
			} catch (ParseException ex) {
				log.error("Failed to parse " + yyyy_MM_dd_HH_mm_ss + " as date. " + ex.getMessage(), ex);
				return null;
			}
		}
		return null;
	}

	public static Date toTimeStampWithSecond(String yyyy_MM_dd_HH_mm_ss) {
		if (StringUtils.isNotBlank(yyyy_MM_dd_HH_mm_ss)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(WS_TIMESTAMP_SECOND_FORMAT);
				return sdf.parse(yyyy_MM_dd_HH_mm_ss);
			} catch (ParseException ex) {
				log.error("Failed to parse " + yyyy_MM_dd_HH_mm_ss + " as date. " + ex.getMessage(), ex);
				return null;
			}
		}
		return null;
	}

	public static String timeToString(Date time) {
		if (time != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(WS_TIMESTAMP_FORMAT);
			return sdf.format(time);
		}
		return "";
	}

	public static String hashText(String text) {
		String pwd = new Md5Hash(text).toHex();
		pwd = StringUtils.reverse(pwd);
		return pwd;
	}

	public static Response buildExcelResponse(RptAbstractXlsx dto, String fileName) {
		StreamingOutput streamingOutput = dto::export;
		DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmm");
		String date = df.format(new Date());
		return Response.ok(streamingOutput, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").header("Content-Disposition", "attachment; filename=" + fileName + "_" + date + ".xlsx").build();
	}

	public static String formatNumber(Object number) {
		if (number == null) {
			return "";
		}
		DecimalFormat formatter = getNumberFormatter();
		return formatter.format(number);
	}

	public static DecimalFormat getNumberFormatter() {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(locale);
		formatter.setMaximumFractionDigits(2);
		return formatter;
	}

	public static String saveToUploadFolder(MultipartFormDataInput input, String attachmentKey, String mainUploadPath, String targetFolder, String uniqueIdentifier) {
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get(attachmentKey);

		if (!CollectionUtils.isEmpty(inputParts)) {
			if (!CollectionUtils.isEmpty(inputParts)) {
				InputStream inputStream = null;
				try {
					InputPart inputPart = inputParts.get(0);
					MultivaluedMap<String, String> header = inputPart.getHeaders();
					inputStream = inputPart.getBody(InputStream.class, null);
					byte[] bytes = IOUtils.toByteArray(inputStream);

					if (!mainUploadPath.endsWith(File.separator)) {
						mainUploadPath = mainUploadPath + File.separator;
					}

					File folder = new File(mainUploadPath + targetFolder);
					if (!folder.exists()) {
						folder.mkdirs();
					}

					String fileName = getFileName(header);
					String extWithDot = getExtensionWithDot(fileName);

					String filePathPic = folder.getAbsolutePath() + File.separator + uniqueIdentifier + extWithDot;
					writeFile(bytes, filePathPic);
					return targetFolder + File.separator + uniqueIdentifier + extWithDot;
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				} finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException ex) {
							log.error(ex.getMessage(), ex);
						}
					}
				}
			}
		}
		return null;
	}

	public static String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	private static void writeFile(byte[] content, String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		fop.flush();
		fop.close();
	}

}
