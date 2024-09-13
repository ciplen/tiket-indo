package com.panemu.tiketIndo.ws;

import com.panemu.search.TableCriteria;
import com.panemu.search.TableQuery;
import com.panemu.tiketIndo.common.CommonUtil;
import com.panemu.tiketIndo.common.JwtUtil;
import com.panemu.tiketIndo.common.TableData;
import com.panemu.tiketIndo.dto.DtoBookTicket;
import com.panemu.tiketIndo.dto.DtoCountAge;
import com.panemu.tiketIndo.dto.DtoCountKota;
import com.panemu.tiketIndo.error.ErrorCode;
import com.panemu.tiketIndo.error.ErrorEntity;
import com.panemu.tiketIndo.error.GeneralException;
import com.panemu.tiketIndo.rcd.BookTicket;
import com.panemu.tiketIndo.rcd.TicketDtl;
import com.panemu.tiketIndo.rcd.Confirmasi;
import com.panemu.tiketIndo.rcd.TicketMaint;
import com.panemu.tiketIndo.rcd.TransferCode;
import com.panemu.tiketIndo.rcd.VenueMaint;
import com.panemu.tiketIndo.rpt.RptBookTicket;
import com.panemu.tiketIndo.rpt.RptTiketPdf;
import com.panemu.tiketIndo.srv.SrvBookTicket;
import com.panemu.tiketIndo.srv.SrvTicketDtl;
import com.panemu.tiketIndo.srv.SrvTransferCode;
import com.panemu.tiketIndo.srv.SrvVenueMaint;
import com.panemu.tiketIndo.srv.SrvTicketMaint;
import com.panemu.tiketIndo.srv.SrvConfirm;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Alfin <ahmad.alfin@panemu.com>
 */
@Path("/bookTicket")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class WsBookTicket {

	private Logger logger = LoggerFactory.getLogger(WsBookTicket.class);

	@Inject
	private SrvBookTicket srvBookTicket;
	@Context
	private ServletContext context;
	@Inject
	private SrvTicketDtl srvTicketDtl;
	@Inject
	private SrvConfirm srvConfirm;
	@Inject
	private SrvTransferCode srvTransferCode;
	@Inject
	private SrvTicketMaint srvTicketMaint;
	@Inject
	private EmailSender es;
	@Inject
	private SrvVenueMaint srvVenue;
	@PersistenceContext
	EntityManager em;

	@PUT
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public void saveBookingTic(DtoBookTicket dto) throws Exception {
		List<BookTicket> lstToSave = DtoBookTicket.createRecord(dto);
		Subject currentUser = SecurityUtils.getSubject();

		TicketMaint ticket = srvTicketMaint.findByType(lstToSave.get(0).getTypeTicket());
		int availability = srvTicketDtl.checkAvailableTicket(ticket.getVenueId(), ticket.getId());
		if (availability < 1) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Maaf, tiket tipe (" + ticket.getType() + ") sudah habis"));
		} else if (availability < lstToSave.size()) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Maaf, tiket tipe (" + ticket.getType() + ") tinggal " + availability));
		}

		for (BookTicket booked : lstToSave) {
			/**
			 * create ticket on booking ticket. once created booking data, it means
			 * create new ticket
			 */
			TicketDtl tiketDtl = new TicketDtl();
			tiketDtl.setBuyerEmail(booked.getEmail());
			tiketDtl.setBuyerName(booked.getNama());
			tiketDtl.setNoKtp(booked.getNoKtp());
			tiketDtl.setPhone(booked.getNoTelepon());
			tiketDtl.setKotaAsal(booked.getKotaAsal());
			tiketDtl.setUmur(Integer.valueOf(booked.getUmur()));
			tiketDtl.setTicketCode("-");
			tiketDtl.setVenueId(booked.getVenueId());
			tiketDtl.setModifiedDate(new Date());
			tiketDtl.setModifiedBy("Admin");
			tiketDtl.setStatus("PENDING");
			tiketDtl.setTicketId(booked.getMaintId());
			tiketDtl.setQty(1);
			srvTicketDtl.insertTicketDtl(tiketDtl);

			String ticketToken = JwtUtil.signTicket(tiketDtl.getId(), booked.getEvent(), booked.getTypeTicket(), booked.getTicketCode(), booked.getQty());
			String ticketCode = "CODE:" + tiketDtl.getId() + "/" + tiketDtl.getVenueId() + "/" + tiketDtl.getTicketId();
			tiketDtl.setTicketCode(ticketCode);
			tiketDtl.setTicketToken(ticketToken);
			srvTicketDtl.updateTicketDtl(tiketDtl);

			booked.setStatus("PENDING");
			booked.setCreatedDate(new Date());
			booked.setModifiedBy(currentUser.getPrincipal() + "");
			booked.setIdTicDtl(tiketDtl.getId());
			srvBookTicket.insert(booked);
			dto.id = booked.getId();
		}

		TransferCode transCode = srvTransferCode.findByID(dto.codeUnique);
		transCode.setStatus("USED");
		srvTransferCode.update(transCode);

		sendNotifEmail(dto);
	}

	private void sendNotifEmail(DtoBookTicket bookTicket) throws IOException, Exception {

		String pattern = "EEEE, dd MMMMM yyyy HH:mm";
		SimpleDateFormat df = new SimpleDateFormat(pattern, new Locale("in", "ID"));
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DATE, +1);
		String tomorrow = df.format(cal.getTime());

		String[] recipients = new String[]{bookTicket.email};

		String url = "https://tiketindonesia.id/tiketindo/confirm/";
//		String url = "http://localhost:4200/confirm/";

		String content = "<div style=\"max-width: 800px;\n"
				  + "    margin: auto;\n"
				  + "    padding: 30px;\n"
				  + "    border: 1px solid #eee;\n"
				  + "    font-size: 16px;\n"
				  + "    line-height: 24px;\n"
				  + "    font-family: 'Helvetica Neue','Helvetica',Helvetica,Arial,sans-serif;\n"
				  + "    color: #555;\n"
				  + "    vertical-align: top;\">\n"
				  + "	<hr>\n"
				  + "<div style=\"background: linear-gradient(to right, #cc1573, #5722a9);\n"
				  + "padding: 10px;\n"
				  + "border-radius: 4px;\n"
				  + "margin: 10px;\n"
				  + "font-size: 2em;\n"
				  + "font-weight: 700;\n"
				  + "color: #07304d;\n"
				  + "text-align: center;\">\n"
				  + "                                  <strong style=\"color: white;\" align=\"center\">IDR. " + bookTicket.hargaCodeTaxDis + "</strong>\n"
				  + "                                </div>"
				  + "<div align=\"center\">\n"
				  + "                                  <label>\n"
				  + "                                      Lakukan pembayaran <strong style=\"color:#07304d\">TEPAT Seperti contoh di atas</strong>\n"
				  + "                                  </label><br>\n"
				  + "                                  <label style=\"color:#d41d00\"><i>*Pembelian tiket di atas jam 21:00 WIB akan diproses keesokan harinya.</i></label>\n"
				  + "                                  <hr>\n"
				  + "                                </div>"
				  + "	<table style=\"width: 100%;\n"
				  + "    line-height: inherit;\n"
				  + "    text-align: left;\" cellspacing=\"0\">\n"
				  + "		<tbody>\n"
				  + "\n"
				  + "			<tr style=\"background: #eee;\n"
				  + "    border-bottom: 1px solid #ddd;\n"
				  + "    font-weight: bold;padding: 5px;\n"
				  + "    vertical-align: top;\">\n"
				  + "				<td>\n"
				  + "					Detail Order\n"
				  + "				</td>\n"
				  + "				<td></td>\n"
				  + "			</tr>\n"
				  + "\n"
				  + "			<tr class=\"m_-2032581191197349715item-detail\">\n"
				  + "				<td>\n"
				  + "					Booking ID\n"
				  + "				</td>\n"
				  + "				<td>\n"
				  + "					: <b>" + bookTicket.id + "</b>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "			<tr class=\"m_-2032581191197349715item-detail\">\n"
				  + "				<td>\n"
				  + "					Fullname\n"
				  + "				</td>\n"
				  + "				<td>\n"
				  + "					: <b>" + bookTicket.identityList.get(0).nama + "</b>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "			<tr class=\"m_-2032581191197349715item-detail\">\n"
				  + "				<td>\n"
				  + "					Email Address\n"
				  + "				</td>\n"
				  + "				<td>\n"
				  + "					: <b><a target=\"_blank\">" + bookTicket.email + "</a></b>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "			<tr class=\"m_-2032581191197349715item-detail\">\n"
				  + "				<td>\n"
				  + "					Event\n"
				  + "				</td>\n"
				  + "				<td>\n"
				  + "					: <b>\n" + bookTicket.event + "</b>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "\n"
				  + "			<tr style=\"background: #eee;\n"
				  + "    border-bottom: 1px solid #ddd;\n"
				  + "    font-weight: bold;padding: 5px;\n"
				  + "    vertical-align: top;\">\n"
				  + "				<td>\n"
				  + "					Detail Items\n"
				  + "				</td>\n"
				  + "				<td></td>\n"
				  + "			</tr>\n"
				  + "			<tr class=\"m_-2032581191197349715item\">\n"
				  + "				<td colspan=\"2\">\n"
				  + "					<table style=\"    width: 100%;\n"
				  + "    line-height: inherit;\n"
				  + "    text-align: left;"
				  + "margin-bottom:10px\">\n"
				  + "						<tbody><tr>\n"
				  + "								<th>No</th>\n"
				  + "								<th>Kategori Tiket</th>\n"
				  + "								<th>Harga</th>\n"
				  + "								<th>Qty</th>\n"
				  + "								<th>Discount (%)</th>\n"
				  + "								<th style=\"text-align:right\">Total</th>\n"
				  + "							</tr>\n"
				  + "							<tr>\n"
				  + "								<td>1</td>\n"
				  + "								<td>\n"
				  + "									" + bookTicket.type + " <br/>\n"
				  //				  + "									" + bookTicket.identityList.get(0).tipeKaos + " & " + bookTicket.identityList.get(0).ukuranKaos + "\n"
				  + "								</td>\n"
				  + "								<td>" + bookTicket.hargaPeriode1 + "</td>\n"
				  + "								<td>" + bookTicket.qty + "</td>\n"
				  + "								<td>0</td>\n"
				  + "\n"
				  + "								<td style=\"text-align:right\">" + bookTicket.hargaPeriode1 * Integer.parseInt(bookTicket.qty) + "</td>\n"
				  + "							</tr>\n"
				  + "								<hr></hr>\n"
				  + "							<tr>\n"
				  + "								<td></td>\n"
				  + "								<td>\n"
				  + "									</td>\n"
				  + "								<td>Goverment Tax 10% </td>\n"
				  + "								<td></td>\n"
				  + "								<td></td>\n"
				  + "\n"
				  + "								<td style=\"text-align:right\">" + Integer.parseInt(bookTicket.qty) * (bookTicket.hargaPeriode1 / 100) * 10 + "</td>\n"
				  + "							</tr>\n"
				  + "						</tbody></table>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "\n"
				  + "\n"
				  + "			<tr style=\"    background: #eee;\n"
				  + "    border-bottom: 1px solid #ddd;\n"
				  + "    font-weight: bold;\">\n"
				  + "				<td colspan=\"2\">\n"
				  + "					<span style=\"margin-left:60%\">Subtotal</span>\n"
				  + "					<strong style=\"float:right\"> " + bookTicket.hargaTotal + "</strong>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "\n"
				  + "			<tr class=\"m_-2032581191197349715item m_-2032581191197349715last\">\n"
				  + "				<td colspan=\"2\">\n"
				  + "					<span style=\"margin-left:60%\">Unix code</span>\n"
				  + "					<strong style=\"float:right\"> " + bookTicket.codeUnique + "</strong>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "			<tr style=\"background: linear-gradient(to right, #cc1573, #5722a9);\n"
				  + "    border-bottom: 1px solid #ddd;\n"
				  + "    font-weight: bold;\">\n"
				  + "				<td style=\"color: white;\" colspan=\"2\">\n"
				  + "					<span style=\"margin-left:60%\">Total Bayar</span>\n"
				  + "					<strong style=\"float:right\">" + bookTicket.hargaCodeTaxDis + "</strong>\n"
				  + "				</td>"
				  + "			</tr>\n"
				  + "                <label style=\"color:#d41d00\"><i>* Perbedaan nilai transfer akan menghambat proses verifikasi</i></label>\n"
				  + "<hr>"
				  + "<tr class=\"m_-2032581191197349715bank-transfer\">\n"
				  + "                <td colspan=\"2\" style=\"background: linear-gradient(to right, #cc1573, #5722a9);width: 74%;padding:30px\">\n"
				  + "                    <table style=\"color: white;\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n"
				  + "                                                <tbody><tr>\n"
				  + "                            <th width=\"150\">Nama Bank</th>\n"
				  + "                            <td style=\"text-align:left\">: " + bookTicket.rekTujuan + "</td>\n"
				  + "                          </tr>\n"
				  + "                          <tr>\n"
				  + "                            <th>Atas Nama</th>\n"
				  + "                            <td style=\"text-align:left\">: " + bookTicket.an + "</td>\n"
				  + "                          </tr>\n"
				  + "                          <tr>\n"
				  + "                            <th>No Rekening</th>\n"
				  + "                            <td style=\"text-align:left\">: " + bookTicket.noRek + "</td>\n"
				  + "                          </tr>\n"
				  + "                    </tbody></table>\n"
				  + "                </td>\n"
				  + "            </tr>"
				  + "<hr>"
				  + "		</tbody></table>\n"
				  + "<div style=\"text-align: center; color: white;\" class=\"add-otherticket\">\n"
				  + "										<button type=\" button\"\n"
				  + "											style=\"background: linear-gradient(to right, #cc1573, #5722a9);width: 74%;\n"
				  + "											color: white;\n"
				  + "											padding: 15px;\n"
				  + "											font-weight: bold;\n"
				  + "											font-size: 25px;\n"
				  + "											border-radius: 10px;\">\n"
				  + "											Konfirmasi Pembayaran\n"
				  + "											" + url + "" + bookTicket.id + "/" + bookTicket.email + "\n"
				  + "										</button>\n"
				  + "									</div>"
				  + "                                  <label style=\"text-align: center;\" >\n"
				  + "                                    <h4 style=\"color:#07304d\">TRANSFER/KONFIRMASI MELEBIHI WAKTU YANG DI TENTUKAN BUKAN TANGGUNG JAWAB KAMI</h4>\n"
				  + "                                  <hr>\n"
				  + "  <div class=\"add-otherticket\">\n"
				  + "<div style=\"background: red;\n"
				  + "padding: 30px;\n"
				  + "border-radius: 4px;\n"
				  + "margin: 15px;\n"
				  + "font-size: 2em;\n"
				  + "font-weight: 700;\n"
				  + "color: white;\n"
				  + "text-align: center;\">\n"
				  + "                                  <strong align=\"center\">TRANSFER SEBELUM:\n"
				  + " </strong><br><br>\n"
				  + "                                 <strong style=\"color:white;\">" + tomorrow + "</strong>\n"
				  + "<div align=\"center\">\n"
				  + "                                </div>"
				  + "									</div>"
				  + "		</tbody></table>\n"
				  + "</div>"
				  + "</div>"
				  + "\n";

		es.sendBooking(recipients, content, content);
	}

	@POST
	@Produces({"application/json"})
	@RequiresAuthentication
	public TableData<BookTicket> findAll(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
		TableData<BookTicket> lstResult = srvBookTicket.find(tq, startIndex, maxRecord);
		return lstResult;
	}

	@GET
	@Path("bookTiket/{id}")
	@Produces({MediaType.APPLICATION_JSON})
//	@RequiresAuthentication
	public BookTicket getTicketMaintById(@PathParam("id") int id) {
		BookTicket rcd = null;
		rcd = srvBookTicket.findById(BookTicket.class, id);
		return rcd;
	}

	@GET
	@Path("{getUniqCode}")
	@Produces({MediaType.APPLICATION_JSON})
	public int getUniqCode() {
		Integer code = srvTransferCode.getTransferCode();
		return code;
	}

	@PUT
	@Path("code")
	public void saveConfirm(Integer id) throws Exception {
		//unused code will be cleared after 31 aug
		BookTicket rcd = null;
		if (id != 0) {
			rcd = srvBookTicket.findById(BookTicket.class, id);
		} else {
			rcd = new BookTicket();
		}
		if (rcd.getStatus().equals("CONFIRM")) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Tiket anda sedang dalam proses pengecekan."));
		} else {
			rcd.setStatus("CONFIRM");
		}
		if (id > 0) {
			srvBookTicket.update(rcd);
		} else {
			srvBookTicket.insert(rcd);
		}
	}

	@GET
	@Path("{id}/{email}")
	@Produces({MediaType.APPLICATION_JSON})
	public void confirmToAdmin(@PathParam("email") String email, @PathParam("id") int id) throws Exception {

		BookTicket booking = srvBookTicket.findById(BookTicket.class, id);
		if (booking.getStatus().equals("APPROVED")) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Tiket anda sudah di approve"));
		} else if (booking.getStatus().equals("CONFIRM")) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Anda sudah melakukan konfirmasi. Tiket Anda sedang dalam proses pengecekan."));
		} else if (booking.getStatus().equals("CANCELLED")) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0400, "Pesanan tiket sudah tidak berlaku setelah 24jam dari pemesanan. Silahkan hubungi Customer Service."));
		} else {
			booking.setStatus("CONFIRM");
			booking.setModifiedDate(new Date());
			if (id > 0) {
				srvBookTicket.update(booking);

				Confirmasi confirm = new Confirmasi();

				confirm.setIdBooking(id);
				confirm.setEmail(booking.getEmail());
				confirm.setKodeUnik(booking.getKodeUnik());

				confirm = srvConfirm.insert(confirm);

				TicketDtl td = srvTicketDtl.findById(TicketDtl.class, booking.getIdTicDtl());
				if (td != null) {
					td.setStatus("CONFIRM");
					td.setModifiedDate(new Date());
					srvTicketDtl.updateTicketDtl(td);
				}

			} else {
				//unused code will be cleared after 31 aug
				srvBookTicket.insert(booking);
			}
			sendConfirmtoAdmin(booking);
		}
	}

	private void sendConfirmtoAdmin(BookTicket booking) throws IOException, Exception {
		String[] recipients = new String[]{"indotik.panemu@gmail.com"};
//		String[] recipients = new String[]{booking.getEmail()};
		String content = "<div style=\"max-width: 800px;\n"
				  + "    margin: auto;\n"
				  + "    padding: 30px;\n"
				  + "    border: 1px solid #eee;\n"
				  + "    font-size: 16px;\n"
				  + "    line-height: 24px;\n"
				  + "    font-family: 'Helvetica Neue','Helvetica',Helvetica,Arial,sans-serif;\n"
				  + "    color: #555;\n"
				  + "    vertical-align: top;\">\n"
				  + "	<hr>\n"
				  + "<div style=\"background: rgba(221,221,221,.44);\n"
				  + "padding: 10px;\n"
				  + "border-radius: 4px;\n"
				  + "margin: 10px;\n"
				  + "font-size: 2em;\n"
				  + "font-weight: 700;\n"
				  + "color: #07304d;\n"
				  + "text-align: center;\">\n"
				  + "                                  <strong align=\"center\">ID Booking: " + booking.getId() + "</strong>\n"
				  + "                                </div>"
				  + "<div align=\"center\">\n"
				  + "                                  <label>\n"
				  + "                                      Konfirmasi pembayaran <strong style=\"color:#07304d\">Untuk ID Booking Ticket</strong>\n"
				  + "                                  </label><br>\n"
				  + "                                  <label style=\"color:#d41d00\"><i>* Tolong di cek keseluruhan transaksi dan di update</i></label>\n"
				  + "                                  <hr>\n"
				  + "                                </div>"
				  + "	<table style=\"width: 100%;\n"
				  + "    line-height: inherit;\n"
				  + "    text-align: left;\" cellspacing=\"0\">\n"
				  + "		<tbody>\n"
				  + "\n"
				  + "			<tr style=\"background: #eee;\n"
				  + "    border-bottom: 1px solid #ddd;\n"
				  + "    font-weight: bold;padding: 5px;\n"
				  + "    vertical-align: top;\">\n"
				  + "				<td>\n"
				  + "					Detail Order\n"
				  + "				</td>\n"
				  + "				<td></td>\n"
				  + "			</tr>\n"
				  + "\n"
				  + "			<tr class=\"m_-2032581191197349715item-detail\">\n"
				  + "				<td>\n"
				  + "					Booking ID\n"
				  + "				</td>\n"
				  + "				<td>\n"
				  + "					: <b>" + booking.getId() + "</b>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "			<tr class=\"m_-2032581191197349715item-detail\">\n"
				  + "				<td>\n"
				  + "					Fullname\n"
				  + "				</td>\n"
				  + "				<td>\n"
				  + "					: <b>" + booking.getNama() + "</b>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "			<tr class=\"m_-2032581191197349715item-detail\">\n"
				  + "				<td>\n"
				  + "					No Identitas\n"
				  + "				</td>\n"
				  + "				<td>\n"
				  + "					: <b>" + booking.getNoKtp() + "</b>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "			<tr class=\"m_-2032581191197349715item-detail\">\n"
				  + "				<td>\n"
				  + "					Email Address\n"
				  + "				</td>\n"
				  + "				<td>\n"
				  + "					: <b><a target=\"_blank\">" + booking.getEmail() + "</a></b>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "			<tr class=\"m_-2032581191197349715item-detail\">\n"
				  + "				<td>\n"
				  + "					Event\n"
				  + "				</td>\n"
				  + "				<td>\n"
				  + "					: <b>\n" + booking.getEvent() + "</b>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "			<tr class=\"m_-2032581191197349715item-detail\">\n"
				  + "				<td>\n"
				  + "					Type Ticket\n"
				  + "				</td>\n"
				  + "				<td>\n"
				  + "					: <b>\n" + booking.getTypeTicket() + "</b>\n"
				  + "				</td>\n"
				  + "			</tr>\n"
				  + "		</tbody></table>\n"
				  + "</div>"
				  + "\n";

		es.sendConfirm(content, content, recipients);
	}

	@PUT
	public void saveApproval(Integer id) throws Exception {
		Subject currentUser = SecurityUtils.getSubject();
		if ("null".equals(currentUser.getPrincipal())) {
			throw GeneralException.create(new ErrorEntity(ErrorCode.ER0401, "Silahkan Logout & Login kembali"));
		}
		BookTicket rcd = null;

		rcd = srvBookTicket.findById(BookTicket.class, id);

		rcd.setStatus("APPROVED");
		rcd.setModifiedBy(currentUser.getPrincipal() + "");
		rcd.setModifiedDate(new Date());

		int code = rcd.getKodeUnik();
		if (code > 0) {
			TransferCode trCode = null;
			trCode = srvTransferCode.findById(TransferCode.class, code);
			if (trCode != null) {
				trCode.setModifiedOn(new Date());
				srvTransferCode.update(trCode);
			}
		}
		srvBookTicket.update(rcd);
		TicketDtl ticketDtl = srvTicketDtl.findById(TicketDtl.class, rcd.getIdTicDtl());
		if (ticketDtl != null) {
			ticketDtl.setStatus(null);
			ticketDtl.setModifiedDate(new Date());
			ticketDtl.setModifiedBy("admin-" + currentUser.getPrincipal() + "");
			srvTicketDtl.updateTicketDtl(ticketDtl);
			sendNotifEmail(ticketDtl);
		}
	}

	private void sendNotifEmail(TicketDtl ticketDtl) throws IOException, Exception {
		VenueMaint venue = srvVenue.findById(ticketDtl.getVenueId());
		TicketMaint ticMain = srvTicketMaint.findById(ticketDtl.getTicketId());
		BookTicket booked = srvBookTicket.findByTDId(ticketDtl.getId());
		RptTiketPdf rpt = new RptTiketPdf(booked, ticketDtl, venue, ticMain);
		File tmpFile = rpt.export();
		String[] recipients = new String[]{ticketDtl.getBuyerEmail()};
		String mssg = "Halo " + ticketDtl.getBuyerName() + ", silahkan download/cetak tiket kamu.";
		String subject = "E-Ticket " + venue.getNama() + " - tiketindonesia.id";
		es.send(recipients, mssg, tmpFile, true, subject);
	}

	@Schedule(hour = "*", minute = "*/5", persistent = false)
	public void checkExpiration() {
		logger.info("run on " + (new Date()));
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DATE, -1);
		Date yesterday = cal.getTime();
		List<TransferCode> lstTransferCode = srvTransferCode.findExpired(yesterday);
		lstTransferCode.stream().forEach(item -> {
			item.setStatus("AVAILABLE");
			item.setModifiedOn(new Date());
			srvTransferCode.update(item);
		});
		/**
		 * check book_ticket. find expired record and update as needed
		 */
		List<TicketDtl> lstTicketDtl = srvTicketDtl.findExpPendingTicket(yesterday);
		lstTicketDtl.stream().forEach(item -> {
			BookTicket booked = srvBookTicket.findByTDId(item.getId());
			booked.setStatus("CANCELLED");
//			booked.setIdTicDtl(0);
			booked.setModifiedBy("SYSTEM");
			booked.setModifiedDate(new Date());
			srvBookTicket.update(booked);
			String[] recipients = new String[]{booked.getEmail()};
			try {
				es.sendTemplate("Expired Order", "Halo " + booked.getNama() + ", orderan kamu sudah expired. Silahkan lakukan order ulang atau hubungi customer service jika sudah melakukan transfer.", "", recipients);
			} catch (Exception ex) {
				java.util.logging.Logger.getLogger(WsBookTicket.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		srvTicketDtl.deleteListTD(lstTicketDtl);

		/**
		 * check transfer_code. find expired record and update as needed
		 */
	}

	@POST
	@Path("xls")
	public Response export(
			  @QueryParam("start") int startIndex,
			  @QueryParam("max") int maxRecord,
			  TableQuery tq) {
		final TableCriteria dateCriteria = tq.getTableCriteria().stream().filter(item -> "modifiedDate".equals(item.getAttributeName())).findAny().orElse(null);
		Date startDate = null;
		Date endDate = null;
		if (dateCriteria != null) {
			String stringDate = dateCriteria.getValue();
			String[] twoDates = StringUtils.split(stringDate, "..");
			try {
				if (twoDates != null && twoDates.length > 0) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					startDate = df.parse(twoDates[0]);
				}
				if (twoDates != null && twoDates.length > 1) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					endDate = df.parse(twoDates[1]);
				}
				logger.info("startDate: " + startDate + " end date: " + endDate);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
		TableData<BookTicket> data = this.findAll(startIndex, maxRecord, tq);
		RptBookTicket rpt = new RptBookTicket(data, startIndex, maxRecord, startDate, endDate);

		return CommonUtil.buildExcelResponse(rpt, "bookTicket");
	}

	@POST
	@Path("xlsAll")
	public Response exportAll(TableQuery tq) {
		final TableCriteria dateCriteria = tq.getTableCriteria().stream().filter(item -> "modifiedDate".equals(item.getAttributeName())).findAny().orElse(null);
		Date startDate = null;
		Date endDate = null;
		if (dateCriteria != null) {
			String stringDate = dateCriteria.getValue();
			String[] twoDates = StringUtils.split(stringDate, "..");
			try {
				if (twoDates != null && twoDates.length > 0) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					startDate = df.parse(twoDates[0]);
				}
				if (twoDates != null && twoDates.length > 1) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					endDate = df.parse(twoDates[1]);
				}
				logger.info("startDate: " + startDate + " end date: " + endDate);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
		TableData<BookTicket> data = this.findAll(0, 0, tq);
		RptBookTicket rpt = new RptBookTicket(data, 0, 0, startDate, endDate);

		return CommonUtil.buildExcelResponse(rpt, "bookTicket");
	}

	@GET
	@Path("countBookingStatus/{id}/{status}")
	@Produces({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public Long countBookingStatus(@PathParam("id") int id, @PathParam("status") String status) {
		Long data = srvBookTicket.countBookingStatus(id, status);
		return data;
	}

	@GET
	@Path("sumTiket/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@RequiresAuthentication
	public Long countBookingStatusAll(@PathParam("status") String status) {
		Long data = srvBookTicket.countBookingStatusAll(status);
		return data;
	}

	@GET
	@Path("countAgeBooking/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	//@RequiresAuthentication
	public List<DtoCountAge> countAgeBooking(@PathParam("id") int id) {
		List<DtoCountAge> lstCount = srvBookTicket.countAgeOfBooking(id);
		return lstCount;
	}

	@GET
	@Path("countKotaBooking/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	//@RequiresAuthentication
	public List<DtoCountKota> countKotaBooking(@PathParam("id") int id) {
		List<DtoCountKota> lstCount = srvBookTicket.countKotaOfBooking(id);

		return lstCount;
	}

}
