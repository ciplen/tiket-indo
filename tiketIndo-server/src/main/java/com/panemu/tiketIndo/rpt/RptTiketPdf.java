package com.panemu.tiketIndo.rpt;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.panemu.tiketIndo.common.CommonUtil;
import com.panemu.tiketIndo.rcd.BookTicket;
import com.panemu.tiketIndo.rcd.TicketDtl;
import com.panemu.tiketIndo.rcd.TicketMaint;
import com.panemu.tiketIndo.rcd.VenueMaint;
import com.panemu.tiketIndo.ws.EmailSender;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bastomi
 */
public class RptTiketPdf {

	private Logger logger = LoggerFactory.getLogger(RptTiketPdf.class);
	private final TicketDtl dtl;
	private VenueMaint venue;
	private TicketMaint ticket;
	private BookTicket booked;

	public RptTiketPdf(BookTicket booked, TicketDtl dtl, VenueMaint venue, TicketMaint ticket) {
		this.dtl = dtl;
		this.venue = venue;
		this.ticket = ticket;
		this.booked = booked;
	}

	private File generatePdf() {
		try {

			String eventName = StringUtils.defaultString(venue.getNama());
			String buyerName = StringUtils.defaultString(dtl.getBuyerName());
			String qty = StringUtils.defaultString(String.valueOf(dtl.getQty()));

			String email = StringUtils.defaultString(dtl.getBuyerEmail());
			String idNumber = StringUtils.defaultString(dtl.getNoKtp());
			String paymentStatus = StringUtils.defaultString("PAID");
			String location = StringUtils.defaultString(venue.getTempat());
			String loc = "";
			String loc2 = "";

			String[] data = location.split(", ", 2);
			loc = data[0];
			loc2 = data[1];

//			String[] array1 = location.split(", ");
//			for (int i = 0; i < array1.length; i++) {
//				loc2 = array1[i];
//			}
//
//			String[] arrSplit = location.split(", ", 2);
//			for (int i = 0; i < arrSplit.length; i++) {
//				loc = arrSplit[i];
//			}
			String merch = " ";
			if (booked != null) {
				merch = booked.getMerchandise();
			}

//			merch = "SADBOYS,L";
			if (merch != null && !merch.equalsIgnoreCase("null,null")) {
				String[] values = merch.split(",");
				merch = Arrays.toString(values);
			} else {
				merch = "";
			}
			String type = StringUtils.defaultString(ticket.getType() + "(" + ticket.getKeterangan() + ")");
			String tax = StringUtils.defaultString("/ Rp." + CommonUtil.formatNumber(ticket.getHargaPeriode1()) + " " + merch + " / Goverment Tax 10% Rp." + CommonUtil.formatNumber(ticket.getHargaPeriode1() / 100 * 10) + " " + merch);
//			String total = StringUtils.defaultString(" / Total Rp." + CommonUtil.formatNumber(ticket.getHargaPeriode1()) + " " + merch);

			DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
			String tanggalEvent = df.format(venue.getTanggalAwal());
			String dateTime = StringUtils.defaultString(tanggalEvent + " 18:00");
			byte[] img = generateQRCodeImage(dtl.getTicketToken(), 140, 140);

			InputStream inputStream = EmailSender.class.getResourceAsStream("/com/panemu/tiketIndo/ticket_template.pdf");
			PDDocument document = PDDocument.load(inputStream);
			PDPage page = document.getPage(0);

			PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
			contentStream.setNonStrokingColor(98, 91, 89);
			contentStream.setFont(PDType1Font.HELVETICA, 20);
			contentStream.beginText();
			contentStream.newLineAtOffset(140, 507);
			contentStream.showText(eventName);
			contentStream.endText();

			contentStream.setFont(PDType1Font.HELVETICA, 11);
			contentStream.beginText();
			contentStream.newLineAtOffset(115, 459);
			contentStream.showText(buyerName);
			contentStream.endText();

			contentStream.beginText();
			contentStream.newLineAtOffset(387, 459);
			contentStream.showText(idNumber);
			contentStream.endText();

			contentStream.beginText();
			contentStream.newLineAtOffset(115, 419.5f);
			contentStream.showText(email);
			contentStream.endText();

			contentStream.beginText();
			contentStream.newLineAtOffset(415, 417.5f);
			contentStream.showText(paymentStatus);
			contentStream.endText();

			contentStream.beginText();
			contentStream.newLineAtOffset(72, 375);
			contentStream.showText(dateTime);
			contentStream.endText();

//			contentStream.beginText();
//			contentStream.newLineAtOffset(348, 415);
//			contentStream.showText(qty + "/Tiket");
//			contentStream.endText();
			contentStream.beginText();
			contentStream.newLineAtOffset(377, 392);
			contentStream.showText(loc);
			contentStream.endText();

			contentStream.beginText();
			contentStream.newLineAtOffset(317, 375);
			contentStream.showText(loc2);
			contentStream.endText();

			contentStream.setFont(PDType1Font.HELVETICA, 10);
			contentStream.beginText();
			contentStream.newLineAtOffset(113, 310.5f);
			contentStream.showText(type);
			contentStream.endText();

			contentStream.beginText();
			contentStream.newLineAtOffset(113, 298.5f);
			contentStream.showText(tax);
			contentStream.endText();

			if (merch.contains("X")) {
				contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 8);//italic
				contentStream.setNonStrokingColor(Color.RED);
				contentStream.beginText();
				contentStream.newLineAtOffset(77, 278.5f);
				contentStream.showText("*Ukuran di atas XL akan dikenakan tambahan biaya (5rb setiap kenaikan ukuran 1 X) , dapat dibayarkan ketika pengambilan.");
				contentStream.endText();
			}

			PDImageXObject image = PDImageXObject.createFromByteArray(document, img, "qrcode_image");

			contentStream.drawImage(image, 570, 345, 130, 130);
			contentStream.close();

			df = new SimpleDateFormat("yyyyMMdd_HHmm");
			df.setTimeZone(TimeZone.getTimeZone("GMT+7"));
			String date = df.format(new Date());
			String tmpFolder = System.getProperty("java.io.tmpdir");
			File outFile = File.createTempFile("tiket" + date, ".pdf", new File(tmpFolder));

			document.save(outFile);
			document.close();
			return outFile;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}

	public File export() throws IOException, Exception {
		File tmpFile = null;
		try {
			tmpFile = generatePdf();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return tmpFile;
	}

	private byte[] generateQRCodeImage(String text, int width, int height)
			  throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
		byte[] pngData = pngOutputStream.toByteArray();
		return pngData;
	}

	public static void main(String... args) {
		try {
			TicketDtl td = new TicketDtl();
			td.setTicketToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZW51ZSI6IkV2ZW50IExhbXBpb24iLCJ0aWNrZXQiOiJWSVAgQ09VUExFIiwiaWQiOiI0MyIsInRpY2tldENvZGUiOiJDT0RFOjIxLzIvOSJ9.gSAqCvln5-5x4-xSHi1Sye7hLlFw3af_WfZJa4Ki6RE");
			td.setBuyerName("Contoh Nama");
			td.setBuyerEmail("contoh.email@mail.com");
			td.setNoKtp("123123123123123123");
			td.setQty(10);

			VenueMaint ven = new VenueMaint();
			ven.setNama("Etnichestra 2019");
			ven.setTempat("Taman Pelangi, Monumen Jogja Kembali, Yogyakarta");
			ven.setTanggalAwal(new Date());

			TicketMaint t = new TicketMaint();
			t.setType("PAKET CIDRO FESTIVAL");
//			t.setKeterangan("1 ORG + (1 Kaos Sadboys/Sadgirls & 1 Tiket CIDRO FESTIVAL)");
			t.setKeterangan("1 ORG/MASUK");
			t.setHargaPeriode1(135000);
			BookTicket booked = new BookTicket();
			RptTiketPdf rpt = new RptTiketPdf(booked, td, ven, t);
			File tmpFile = rpt.export();
			System.out.println(tmpFile.getAbsolutePath());
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(RptTiketPdf.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
