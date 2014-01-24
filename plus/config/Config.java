package com.mpay.plus.config;

public class Config {

	public static final String SMS_ACTIVE = "mp agent a ";
	public static final String BKCD = "03";
	public static final String MDCD = "1"; // model code, android = 1
	public static final String sURL_BANK_INFO = "http://www.sacombank.com.vn";
	public static final String sURL_BANK_HELP = "http://m-plus.vn/guide/abbank";

	// test
	 public static String[] aURL_SOCKET = new String[] { "test.m-pay.vn:8092"};
	 public static String[] aURL_HTTP = new String[] { "test.m-pay.vn" };
	 
	 //Test 2 agent
//	 public static String[] aURL_SOCKET = new String[] { "test2.m-pay.vn:6868"};
//	 public static String[] aURL_HTTP = new String[] { "test2.m-pay.vn" };
	 
	// pre
//	public static final String[] aURL_SOCKET = new String[] {"m-plus.com.vn:6868", "m-plus.net.vn:6868" };
//	public static final String[] aURL_HTTP = new String[] { "m-pay.com.vn" };

	// main
//	 public static final String[] aURL_SOCKET = new String[] {"m-plus.com.vn:9488", "m-plus.net.vn:9488" };
//	 public static final String[] aURL_HTTP = new String[] { "m-pay.com.vn" };

	public static final int iHTTP_PORT = 80;
	public static final String sHTTPSERVICE = "/MPay-CoreMServicesNew/CoreMReceiver?code=";

//	public static final String[] DEFAULT_MENU_CARD = new String[] {
//			"8#MOBILE|Thẻ điện thoại di động|Viettel, Vinaphone, Mobifone, Vietnamobile, Gmobile, S-Fone|http://test.m-pay.vn/picture/mobile.png!MOBI|Mobifone|Mua mã thẻ nạp tiền cho thuê bao trả trước hoặc trả sau của Mobifone|http://test.m-pay.vn/picture/card/20130312/mobi.png!VINA|VinaPhone|Mua mã thẻ nạp tiền cho thuê bao trả trước hoặc trả sau của VinaPhone|http://test.m-pay.vn/picture/card/20130312/vina.png!VIETTEL|Viettel|Mua mã thẻ nạp tiền cho thuê bao trả trước hoặc trả sau của Viettel|http://test.m-pay.vn/picture/card/20130312/viettel.png!VNMOBI|Vietnamobile|Mua mã thẻ nạp tiền cho thuê bao trả trước Vietnamobile|http://test.m-pay.vn/picture/card/20130312/vnmobi.png!GMOBILE|Gmobile|Mua mã thẻ nạp tiền cho thuê bao trả trước Gmobile|http://test.m-pay.vn/picture/card/20130312/gmobile.png!S-FONE|S-Fone|Mua mã thẻ nạp tiền cho thuê bao trả trước S-Fone|http://test.m-pay.vn/picture/card/20130312/s-fone.png#GAME|Thẻ game|Zingcard, OnCash, Gate,…|http://test.m-pay.vn/picture/card/20130312/game.png!ZINGCARD|Zing|Mua mã thẻ nạp ZingXu vào tài khoản Zing ID (Vinagame)|http://test.m-pay.vn/picture/card/20130312/zingcard.png!GATE|Gate|Mua mã thẻ nạp cho các game của FPT Online và cho nhiều dịch vụ khác|http://test.m-pay.vn/picture/card/20130312/gate.png!VCOIN|Vcoin|Mua mã thẻ nạp Vcoin dùng cho mọi dịch vụ của VTC|http://test.m-pay.vn/picture/card/20130312/vcoin.png!ONCASH|OnCash|Mua mã thẻ nạp cho các game của VDC-Net2E|http://test.m-pay.vn/picture/card/20130312/oncash.png#INTERNETPHONE|Thẻ điện thoại Internet|Ringvoice, Vietvoiz, SaigonVoiz, I-FoneVNN, Fone1718…|http://test.m-pay.vn/picture/internetphone.png!HRV|RingVoice|Gọi điện thoại quốc tế RingVoice - nói nhiều hơn, nghe rõ hơn|http://test.m-pay.vn/picture/card/20130312/hrv.png!HVV|VietVoiz|Gọi điện thoại quốc tế VietVoiz - nói nhiều hơn, nghe rõ hơn|http://test.m-pay.vn/picture/card/20130312/hvv.png!HSV|SaigonVoiz|Gọi điện thoại quốc tế SaigonVoiz - nói nhiều hơn, nghe rõ hơn|http://test.m-pay.vn/picture/card/20130312/hsv.png!IFONEVNN|iFoneVNN|Thẻ gọi điện thoại quốc tế của VNPT|http://test.m-pay.vn/picture/card/20130312/ifonevnn.png!FONE1718|Fone1718|Thẻ gọi điện thoại quốc tế của VNPT|http://test.m-pay.vn/picture/card/20130312/fone1718.png!FONE1718_5|Fone1718-5QG|Mỹ, Canada, Trung Quốc, Hong Kong, Singapore|http://test.m-pay.vn/picture/card/20130312/fone1718_5.png#OTHER|Các loại thẻ khác|Thẻ nạp đa năng, Thẻ diệt virus BKAV,…|http://test.m-pay.vn/picture/card/20130312/other.png!BKAV|BkavPro|Thẻ diệt virus Bkav|http://test.m-pay.vn/picture/card/20130312/bkav.png!BKAVMB|Bkav Mobile|Thẻ diệt virus cho điện thoại di động|http://test.m-pay.vn/picture/card/20130312/bkavmb.png!BKAVED|Bkav eDict|Từ điển điện tử Bkav eDict|http://test.m-pay.vn/picture/card/20130312/bkaved.png!MEGACARD|Megacard|Thẻ đa năng để nạp tiền cho ĐTDĐ, game,…|http://test.m-pay.vn/picture/card/20130312/megacard.png",
//			"8#MOBILE|Thẻ điện thoại di động|Viettel, Vinaphone, Mobifone, Vietnamobile, Gmobile, S-Fone|http://test.m-pay.vn/picture/mobile.png!MOBI|Mobifone|Mua mã thẻ nạp tiền cho thuê bao trả trước hoặc trả sau của Mobifone|http://test.m-pay.vn/picture/card/20130312/mobi.png!VINA|VinaPhone|Mua mã thẻ nạp tiền cho thuê bao trả trước hoặc trả sau của VinaPhone|http://test.m-pay.vn/picture/card/20130312/vina.png!VIETTEL|Viettel|Mua mã thẻ nạp tiền cho thuê bao trả trước hoặc trả sau của Viettel|http://test.m-pay.vn/picture/card/20130312/viettel.png!VNMOBI|Vietnamobile|Mua mã thẻ nạp tiền cho thuê bao trả trước Vietnamobile|http://test.m-pay.vn/picture/card/20130312/vnmobi.png!GMOBILE|Gmobile|Mua mã thẻ nạp tiền cho thuê bao trả trước Gmobile|http://test.m-pay.vn/picture/card/20130312/gmobile.png!S-FONE|S-Fone|Mua mã thẻ nạp tiền cho thuê bao trả trước S-Fone|http://test.m-pay.vn/picture/card/20130312/s-fone.png#GAME|Thẻ game|Zingcard, OnCash, Gate,…|http://test.m-pay.vn/picture/card/20130312/game.png!ZINGCARD|Zing|Mua mã thẻ nạp ZingXu vào tài khoản Zing ID (Vinagame)|http://test.m-pay.vn/picture/card/20130312/zingcard.png!GATE|Gate|Mua mã thẻ nạp cho các game của FPT Online và cho nhiều dịch vụ khác|http://test.m-pay.vn/picture/card/20130312/gate.png!VCOIN|Vcoin|Mua mã thẻ nạp Vcoin dùng cho mọi dịch vụ của VTC|http://test.m-pay.vn/picture/card/20130312/vcoin.png!ONCASH|OnCash|Mua mã thẻ nạp cho các game của VDC-Net2E|http://test.m-pay.vn/picture/card/20130312/oncash.png#INTERNETPHONE|Thẻ điện thoại Internet|Ringvoice, Vietvoiz, SaigonVoiz, I-FoneVNN, Fone1718…|http://test.m-pay.vn/picture/internetphone.png!HRV|RingVoice|Gọi điện thoại quốc tế RingVoice - nói nhiều hơn, nghe rõ hơn|http://test.m-pay.vn/picture/card/20130312/hrv.png!HVV|VietVoiz|Gọi điện thoại quốc tế VietVoiz - nói nhiều hơn, nghe rõ hơn|http://test.m-pay.vn/picture/card/20130312/hvv.png!HSV|SaigonVoiz|Gọi điện thoại quốc tế SaigonVoiz - nói nhiều hơn, nghe rõ hơn|http://test.m-pay.vn/picture/card/20130312/hsv.png!IFONEVNN|iFoneVNN|Thẻ gọi điện thoại quốc tế của VNPT|http://test.m-pay.vn/picture/card/20130312/ifonevnn.png!FONE1718|Fone1718|Thẻ gọi điện thoại quốc tế của VNPT|http://test.m-pay.vn/picture/card/20130312/fone1718.png!FONE1718_5|Fone1718-5QG|Mỹ, Canada, Trung Quốc, Hong Kong, Singapore|http://test.m-pay.vn/picture/card/20130312/fone1718_5.png#OTHER|Các loại thẻ khác|Thẻ nạp đa năng, Thẻ diệt virus BKAV,…|http://test.m-pay.vn/picture/card/20130312/other.png!BKAV|BkavPro|Thẻ diệt virus Bkav|http://test.m-pay.vn/picture/card/20130312/bkav.png!BKAVMB|Bkav Mobile|Thẻ diệt virus cho điện thoại di động|http://test.m-pay.vn/picture/card/20130312/bkavmb.png!BKAVED|Bkav eDict|Từ điển điện tử Bkav eDict|http://test.m-pay.vn/picture/card/20130312/bkaved.png!MEGACARD|Megacard|Thẻ đa năng để nạp tiền cho ĐTDĐ, game,…|http://test.m-pay.vn/picture/card/20130312/megacard.png" };
//	public static final String[] DEFAULT_MENU_BILL = new String[] {
//			"4#OTHER|Danh sách NCC dịch vụ trả sau|Điện lực TP HCM, AirMekong, Truyền hình AVG, VNPT Hải Phòng,...|http://test.m-pay.vn/picture/bill/other.png!60000101|Điện lực TP Hồ Chí Minh|Thanh toán tiền điện qua số PE in trên hóa đơn hàng tháng|http://test.m-pay.vn/picture/bill/000003.png!00001420|AirMekong|Thanh toán mã đặt chỗ để mua vé máy bay|http://test.m-pay.vn/picture/bill/000094.png!00002101|Truyền hình AVG|Thanh toán cước truyền hình qua số Hợp đồng hoặc số Thiết bị|http://test.m-pay.vn/picture/bill/000021.png!00000301|VNPT Hải Phòng|Thanh toán cước VNPT qua số HPG in trên hóa đơn hàng tháng|http://test.m-pay.vn/picture/bill/000096.png!00000210|iMart|Giải trí cùng Tacke.vn|http://test.m-pay.vn/picture/bill/000002.png",
//			"4#OTHER|Danh sách NCC dịch vụ trả sau|Điện lực TP HCM, AirMekong, Truyền hình AVG, VNPT Hải Phòng,...|http://test.m-pay.vn/picture/bill/other.png!60000101|Điện lực TP Hồ Chí Minh|Thanh toán tiền điện qua số PE in trên hóa đơn hàng tháng|http://test.m-pay.vn/picture/bill/000003.png!00001420|AirMekong|Thanh toán mã đặt chỗ để mua vé máy bay|http://test.m-pay.vn/picture/bill/000094.png!00002101|Truyền hình AVG|Thanh toán cước truyền hình qua số Hợp đồng hoặc số Thiết bị|http://test.m-pay.vn/picture/bill/000021.png!00000301|VNPT Hải Phòng|Thanh toán cước VNPT qua số HPG in trên hóa đơn hàng tháng|http://test.m-pay.vn/picture/bill/000096.png!00000210|iMart|Giải trí cùng Tacke.vn|http://test.m-pay.vn/picture/bill/000002.png" };
//	public static final String[] DEFAULT_MENU_NEWS = new String[] {
//			"4#OTHER|Tin tức|Thông tin sản phẩm, dịch vụ mới, các chương trình khuyến mãi|http://test.m-pay.vn/picture/news/other.png!0001|Chung Một Niềm Vui|Từ ngày... Sacombank tặng 1000 thẻ nạp điện thoại 100K cho 1000 khách hàng đầu tiên đăng ký và sử dụng dịch vụ mobileBanking mPlus|http://test.m-pay.vn/picture/news/0001.png!0002|Chương trình khuyến mãi trên M-Plus|Chương trình khuyến mãi đặc biệt GIẢM GIÁ TỪ 5 ĐẾN 20% trên tất cả các sản phẩm dịch vụ M-Plus|http://test.m-pay.vn/picture/news/0002.png!VINA|Khuyến mãi VinaPhone|Từ ngày 04/01/2013 đến 07/01/2013 VinaPhone khuyến mãi 50% giá trị thẻ nạp|http://test.m-pay.vn/picture/news/vina.png",
//			"4#OTHER|Tin tức|Thông tin sản phẩm, dịch vụ mới, các chương trình khuyến mãi|http://test.m-pay.vn/picture/news/other.png!0001|Chung Một Niềm Vui|Từ ngày... Sacombank tặng 1000 thẻ nạp điện thoại 100K cho 1000 khách hàng đầu tiên đăng ký và sử dụng dịch vụ mobileBanking mPlus|http://test.m-pay.vn/picture/news/0001.png!0002|Chương trình khuyến mãi trên M-Plus|Chương trình khuyến mãi đặc biệt GIẢM GIÁ TỪ 5 ĐẾN 20% trên tất cả các sản phẩm dịch vụ M-Plus|http://test.m-pay.vn/picture/news/0002.png!VINA|Khuyến mãi VinaPhone|Từ ngày 04/01/2013 đến 07/01/2013 VinaPhone khuyến mãi 50% giá trị thẻ nạp|http://test.m-pay.vn/picture/news/vina.png" };
//	public static final String[] DEFAULT_MENU_TOPUP = new String[] {
//			"4#PREPAID|Thuê bao di động trả trước|Viettel, VinaPhone, Mobifone, Vietnamobile, Gmobile, S-Fone|http://test.m-pay.vn/picture/topup/prepaid.png!001|Viettel|Nạp tiền cho thuê bao trả trước Viettel|http://test.m-pay.vn/picture/topup/001.png!002|VinaPhone|Nạp tiền cho thuê bao trả trước VinaPhone|http://test.m-pay.vn/picture/topup/002.png!003|Mobifone|Nạp tiền cho thuê bao trả trước Mobifone|http://test.m-pay.vn/picture/topup/003.png!004|S-Fone|Nạp tiền cho thuê bao trả trước S-Fone|http://test.m-pay.vn/picture/topup/004.png!005|Gmobile (Beeline cũ)|Nạp tiền cho thuê bao trả trước Gmobile|http://test.m-pay.vn/picture/topup/005.png!006|Vietnamobile|Nạp tiền cho thuê bao trả trước Vietnamobile|http://test.m-pay.vn/picture/topup/006.png#POSTPAID|Thuê bao di động trả sau|Mobifone|http://test.m-pay.vn/picture/topup/postpaid.png!003P|Mobifone|Nạp tiền để thanh toán cước cho thuê bao trả sau của Mobifone|http://test.m-pay.vn/picture/topup/003P.png",
//			"4#PREPAID|Thuê bao di động trả trước|Viettel, VinaPhone, Mobifone, Vietnamobile, Gmobile, S-Fone|http://test.m-pay.vn/picture/topup/prepaid.png!001|Viettel|Nạp tiền cho thuê bao trả trước Viettel|http://test.m-pay.vn/picture/topup/001.png!002|VinaPhone|Nạp tiền cho thuê bao trả trước VinaPhone|http://test.m-pay.vn/picture/topup/002.png!003|Mobifone|Nạp tiền cho thuê bao trả trước Mobifone|http://test.m-pay.vn/picture/topup/003.png!004|S-Fone|Nạp tiền cho thuê bao trả trước S-Fone|http://test.m-pay.vn/picture/topup/004.png!005|Gmobile (Beeline cũ)|Nạp tiền cho thuê bao trả trước Gmobile|http://test.m-pay.vn/picture/topup/005.png!006|Vietnamobile|Nạp tiền cho thuê bao trả trước Vietnamobile|http://test.m-pay.vn/picture/topup/006.png#POSTPAID|Thuê bao di động trả sau|Mobifone|http://test.m-pay.vn/picture/topup/postpaid.png!003P|Mobifone|Nạp tiền để thanh toán cước cho thuê bao trả sau của Mobifone|http://test.m-pay.vn/picture/topup/003P.png" };

	public static final String[] DEFAULT_MENU_CARD = new String[] {
		"1#MOBILE|Thẻ điện thoại di động|Viettel, Vinaphone, Mobifone, Vietnamobile, Gmobile, S-Fone|http://m-pay.com.vn/pictures/mobile.png!MOBI|Mobifone|Mua mã thẻ nạp tiền cho thuê bao trả trước hoặc trả sau Mobifone|http://m-pay.com.vn/pictures/card/mobi.png!VINA|VinaPhone|Mua mã thẻ nạp tiền cho thuê bao trả trước hoặc trả sau VinaPhone|http://m-pay.com.vn/pictures/card/vina.png!VIETTEL|Viettel|Mua mã thẻ nạp tiền cho thuê bao trả trước hoặc trả sau Viettel|http://m-pay.com.vn/pictures/card/viettel.png!VNMOBI|Vietnamobile|Mua mã thẻ nạp tiền cho thuê bao trả trước Vietnamobile|http://m-pay.com.vn/pictures/card/vnmobi.png!GMOBILE|Gmobile|Mua mã thẻ nạp tiền cho thuê bao trả trước Gmobile|http://m-pay.com.vn/pictures/card/gmobile.png!S-FONE|S-Fone|Mua mã thẻ nạp tiền cho thuê bao trả trước S-Fone|http://m-pay.com.vn/pictures/card/s-fone.png#GAME|Thẻ game|Zingcard, OnCash, Gate,…|http://m-pay.com.vn/pictures/card/game.png!ZINGCARD|Zing|Mua mã thẻ nạp ZingXu vào tài khoản Zing ID (Vinagame)|http://m-pay.com.vn/pictures/card/zingcard.png!GATE|Gate|Mua mã thẻ nạp cho các game của FPT Online và cho nhiều dịch vụ khác|http://m-pay.com.vn/pictures/card/gate.png!VCOIN|Vcoin|Mua mã thẻ nạp Vcoin dùng cho mọi dịch vụ của VTC|http://m-pay.com.vn/pictures/card/vcoin.png!ONCASH|OnCash|Mua mã thẻ nạp cho các game của VDC-Net2E|http://m-pay.com.vn/pictures/card/oncash.png#INTERNETPHONE|Thẻ điện thoại Internet|Ringvoice, Vietvoiz, SaigonVoiz, I-FoneVNN, Fone1718…|http://m-pay.com.vn/pictures/internetphone.png!HRV|RingVoice|Gọi điện thoại quốc tế RingVoice - nói nhiều hơn, nghe rõ hơn|http://m-pay.com.vn/pictures/card/hrv.png!HVV|VietVoiz|Gọi điện thoại quốc tế VietVoiz - nói nhiều hơn, nghe rõ hơn|http://m-pay.com.vn/pictures/card/hvv.png!HSV|SaigonVoiz|Gọi điện thoại quốc tế SaigonVoiz - nói nhiều hơn, nghe rõ hơn|http://m-pay.com.vn/pictures/card/hsv.png!IFONEVNN|iFoneVNN|Thẻ gọi điện thoại qua internet của VNPT|http://m-pay.com.vn/pictures/card/ifonevnn.png!FONE1718|Fone1718|Thẻ gọi điện thoại trong nước và quốc tế với giá cước rẻ|http://m-pay.com.vn/pictures/card/fone1718.png!FONE1718_5|Fone1718-5QG|Thẻ ưu đãi gọi đi 5 quốc gia: Mỹ, Canada, Hong Kong, Trung Quốc, Singapore|http://m-pay.com.vn/pictures/card/fone1718_5.png#OTHER|Các loại thẻ khác|Thẻ nạp đa năng, Thẻ diệt virus BKAV,…|http://m-pay.com.vn/pictures/card/other.png!BKAV|BkavPro|Thẻ phần mềm diệt virus BKAV|http://m-pay.com.vn/pictures/card/bkav.png!BKAVMB|Bkav Mobile|Thẻ diệt virus cho điện thoại di động BKAV|http://m-pay.com.vn/pictures/card/bkavmb.png!BKAVED|Bkav eDict|Từ điển điện tử BKAV eDict|http://m-pay.com.vn/pictures/card/bkaved.png!MEGACARD|Megacard|Thẻ đa năng để nạp tiền cho ĐTDĐ, game,…|http://m-pay.com.vn/pictures/card/megacard.png",
		"1#MOBILE|Telco prepaid cards|Viettel, Vinaphone, Mobifone, Vietnamobile, Gmobile, S-Fone|http://m-pay.com.vn/pictures/mobile.png!MOBI|Mobifone|Buy Pincode(s) to topup for prepaid or postpaid subscribers of Mobifone|http://m-pay.com.vn/pictures/card/mobi.png!VINA|VinaPhone|Buy Pincode(s) to topup for prepaid or postpaid subscribers of VinaPhone|http://m-pay.com.vn/pictures/card/vina.png!VIETTEL|Viettel|Buy Pincode(s) to topup for prepaid or postpaid subscribers of Viettel|http://m-pay.com.vn/pictures/card/viettel.png!VNMOBI|Vietnamobile|Buy Pincode(s) to topup for prepaid subscribers of Vietnamobile|http://m-pay.com.vn/pictures/card/vnmobi.png!GMOBILE|Gmobile|Buy Pincode(s) to topup for prepaid subscribers of Gmobile|http://m-pay.com.vn/pictures/card/gmobile.png!S-FONE|S-Fone|Buy Pincode(s) to topup for prepaid subscribers of S-Fone|http://m-pay.com.vn/pictures/card/s-fone.png#GAME|Game cards|Zingcard, OnCash, Gate,…|http://m-pay.com.vn/pictures/card/game.png!ZINGCARD|Zing|Buy Pincode(s) to topup ZingXu for any Zing ID account (Vinagame)|http://m-pay.com.vn/pictures/card/zingcard.png!GATE|Gate|Buy Pincode(s) to topup for any game of FPT Online and for many other services|http://m-pay.com.vn/pictures/card/gate.png!VCOIN|Vcoin|Buy Pincode(s) to topup Vcoin using for all services of VTC|http://m-pay.com.vn/pictures/card/vcoin.png!ONCASH|OnCash|Buy Pincode(s) to topup for any game of VDC-Net2E|http://m-pay.com.vn/pictures/card/oncash.png#INTERNETPHONE|Internet phone card|Ringvoice, Vietvoiz, SaigonVoiz, I-FoneVNN, Fone1718…|http://m-pay.com.vn/pictures/internetphone.png!HRV|RingVoice|RingVoice International calls - talk more, hear better|http://m-pay.com.vn/pictures/card/hrv.png!HVV|VietVoiz|VietVoiz International calls - talk more, hear better|http://m-pay.com.vn/pictures/card/hvv.png!HSV|SaigonVoiz|SaigonVoiz International calls - talk more, hear better|http://m-pay.com.vn/pictures/card/hsv.png!IFONEVNN|iFoneVNN|Prepaid card for internet phone service of VNPT|http://m-pay.com.vn/pictures/card/ifonevnn.png!FONE1718|Fone1718|Prepaid card for cheap domestic and international calls|http://m-pay.com.vn/pictures/card/fone1718.png!FONE1718_5|Fone1718 - 5 Countries|Prepaid card for cheap calls to USA, Canada, Hongkong, China, Singapore|http://m-pay.com.vn/pictures/card/fone1718_5.png#OTHER|Other cards|Megacard, BKAV,…|http://m-pay.com.vn/pictures/card/other.png!BKAV|BkavPro|BKAV antivirus software card|http://m-pay.com.vn/pictures/card/bkav.png!BKAVMB|Bkav Mobile|BKAV mobile security cards|http://m-pay.com.vn/pictures/card/bkavmb.png!BKAVED|Bkav eDict|BKAV e-Dictionary|http://m-pay.com.vn/pictures/card/bkaved.png!MEGACARD|Megacard|Multi-purpose prepaid card for mobile phone service, game,...|http://m-pay.com.vn/pictures/card/megacard.png" };
	public static final String[] DEFAULT_MENU_BILL = new String[] {
		"1#OTHER|Danh sách NCC dịch vụ trả sau|Điện lực TP HCM, AirMekong, Truyền hình AVG, VNPT Hải Phòng,...|http://m-pay.com.vn/pictures/bill/other.png!60000101|Điện lực TP Hồ Chí Minh|Thanh toán tiền điện qua số PE in trên hóa đơn hàng tháng|http://m-pay.com.vn/pictures/bill/600001.png!00002001|Truyền hình VTC|Thanh toán gói cước qua Mã Đầu Thu (10 số đầu của Mã Dịch Vụ)|http://m-pay.com.vn/pictures/bill/000020.png!00002101|Truyền hình AVG|Thanh toán cước truyền hình qua số Hợp đồng hoặc số Thiết bị|http://m-pay.com.vn/pictures/bill/000021.png!00000301|VNPT Hải Phòng|Thanh toán cước VNPT qua số HPG in trên hóa đơn hàng tháng|http://m-pay.com.vn/pictures/bill/000003.png!00002210|Sohapay|Thanh toán hóa đơn trên http://muachung.vn qua SohaPay|http://m-pay.com.vn/pictures/bill/000022.png!00001810|SPT Online|Thanh toán mua thẻ gọi quốc tế đa năng|http://m-pay.com.vn/pictures/bill/000018.png",
		"1#OTHER|List of Billers|EVN HCM, AIRMEKONG, AVG Television, VNPT Hai Phong|http://m-pay.com.vn/pictures/bill/other.png!60000101|EVN Ho Chi Minh city|Payment of electricity usage via PE number in your monthly bills|http://m-pay.com.vn/pictures/bill/600001.png!00002001|VTC Digital|Digital TV Receiver Code is the first ten digit number of Service Code|http://m-pay.com.vn/pictures/bill/000020.png!00002101|AVG Television|Payment of TV service via Contract number or Equipment number|http://m-pay.com.vn/pictures/bill/000021.png!00000301|VNPT Hai Phong|Payment of VNPT services via HPG number in your monthly bills|http://m-pay.com.vn/pictures/bill/000003.png!00002210|Sohapay|Payment of http://muachung.vn Bill via SohaPay|http://m-pay.com.vn/pictures/bill/000022.png!00001810|SPT Online|Universal World Phone - All in one|http://m-pay.com.vn/pictures/bill/000018.png" };
	public static final String[] DEFAULT_MENU_NEWS = new String[] {
		"1#OTHER|Tin tức|Thông tin sản phẩm, dịch vụ mới, các chương trình khuyến mãi|http://m-pay.com.vn/pictures/news/other.png!LOAD|Đang tải|Ứng dụng đang tải tin tức, sản phẩm mới nhất|http://m-pay.com.vn/pictures/news/LOAD.png",
		"1#OTHER|News|Information of new products and services, promotion programs,…|http://m-pay.com.vn/pictures/news/other.png!LOAD|Loading|News and products are loading, please wait…|http://m-pay.com.vn/pictures/news/en/LOAD.png" };
	public static final String[] DEFAULT_MENU_TOPUP = new String[] {
		"1#PREPAID|Thuê bao di động trả trước|Viettel, VinaPhone, Mobifone, Vietnamobile, Gmobile, S-Fone|http://m-pay.com.vn/pictures/topup/prepaid.png!003|Mobifone|Nạp tiền cho thuê bao trả trước Mobifone|http://m-pay.com.vn/pictures/topup/003.png!002|VinaPhone|Nạp tiền cho thuê bao trả trước VinaPhone|http://m-pay.com.vn/pictures/topup/002.png!001|Viettel|Nạp tiền cho thuê bao trả trước Viettel|http://m-pay.com.vn/pictures/topup/001.png!006|Vietnamobile|Nạp tiền cho thuê bao trả trước Vietnamobile|http://m-pay.com.vn/pictures/topup/006.png!005|Gmobile (Beeline cũ)|Nạp tiền cho thuê bao trả trước Gmobile|http://m-pay.com.vn/pictures/topup/005.png!004|S-Fone|Nạp tiền cho thuê bao trả trước S-Fone|http://m-pay.com.vn/pictures/topup/004.png#POSTPAID|Thuê bao di động trả sau|Mobifone|http://m-pay.com.vn/pictures/topup/postpaid.png!003P|Mobifone|Nạp tiền để thanh toán cước cho thuê bao trả sau của Mobifone|http://m-pay.com.vn/pictures/topup/003P.png#GAME|Game|Nạp tiền cho game|http://m-pay.com.vn/pictures/topup/game.png!007|VTC Intecom|Nạp Vcoin dùng cho mọi dịch vụ của VTC Intecom|http://m-pay.com.vn/pictures/topup/007.png!008|FPT (GATE)|Nạp Bạc vào tài khoản Gate của FPT Online|http://m-pay.com.vn/pictures/topup/008.png!009|Vinagame (ZING)|Nạp ZingXu vào tài khoản Zing ID (Vinagame)|http://m-pay.com.vn/pictures/topup/009.png!010|VDC-Net2E|Nạp OnCash vào tài khoản OnGate của VDC-Net2E|http://m-pay.com.vn/pictures/topup/010.png",
		"1#PREPAID|Prepaid mobile subscribers|Viettel, VinaPhone, Mobifone, Vietnamobile, Gmobile, S-Fone|http://m-pay.com.vn/pictures/topup/prepaid.png!003|Mobifone|Direct topup for prepaid subscribers of Mobifone|http://m-pay.com.vn/pictures/topup/003.png!002|VinaPhone|Direct topup for prepaid subscribers of VinaPhone|http://m-pay.com.vn/pictures/topup/002.png!001|Viettel|Direct topup for prepaid subscribers of Viettel|http://m-pay.com.vn/pictures/topup/001.png!006|Vietnamobile|Direct topup for prepaid subscribers of Vietnamobile|http://m-pay.com.vn/pictures/topup/006.png!005|Gmobile (Beeline)|Direct topup for prepaid subscribers of Gmobile|http://m-pay.com.vn/pictures/topup/005.png!004|S-Fone|Direct topup for prepaid subscribers of S-Fone|http://m-pay.com.vn/pictures/topup/004.png#POSTPAID|Postpaid mobile subscribers|Mobifone|http://m-pay.com.vn/pictures/topup/postpaid.png!003P|Mobifone|Direct topup to pay bills for postpaid subscribers of Mobifone|http://m-pay.com.vn/pictures/topup/003P.png#GAME|Game|Direct topup for the game account|http://m-pay.com.vn/pictures/topup/game.png!007|VTC Intecom|Topup Vcoin to VTC account to use all services of VTC Intecom|http://m-pay.com.vn/pictures/topup/007.png!008|FPT (GATE)|Topup Silver to Gate account of FPT Online|http://m-pay.com.vn/pictures/topup/008.png!009|Vinagame (ZING)|Direct topup (ZingXu) for Zing ID (Vinagame)|http://m-pay.com.vn/pictures/topup/009.png!010|VDC-Net2E|Topup OnCash to OnGate account of VDC-Net2E|http://m-pay.com.vn/pictures/topup/010.png" };

	
	// <Command Type>
	public static String TYPE_COMMAND = "";
	public static final String DANG_KY = "a";
	public static final String DANG_KY_APP = "a1";
	public static final String ACTIVE_APP = "a2";
	public static final String ACTIVE_APP_FORGOT = "a3";
	public static final String ACTIVE_APP_FORGOT_RE = "a4";
	public static final String DANG_KY_INFO = "in";
	public static final String DANG_KY_APP_KEY = "EMONKEY";
	public static final String B_CK_MID_TRA_CUU_NAME = "x";
	public static final String B_CK_AGENT_TRA_CUU_NAME = "n";

	// Agent
	public static final String A_NAP_TIEN_TU_TK_BANK = "g";
	public static final String A_LAY_PHI_RUT_TIEN_VE_TK_BANK = "x";
	public static final String A_RUT_TIEN_VE_TK_BANK = "w";
	public static final String A_DANG_KY_NAP_TU_TK = "b";
	public static final String A_CK_AGENT = "f";

	public static final String A_GUI_Y_KIEN = "z";
	public static final String A_NHAN_PHAN_HOI = "z";

	public static final String B_CK_MID_CHUYEN_KHOAN = "t";
	public static final String B_CK_TK_TRA_CUU_NAME = "z";
	public static final String B_CK_TK_CHUYEN_KHOAN = "f";
	public static final String B_CK_THE_TRA_CUU_NAME = "z";
	public static final String B_CK_THE_CHUYEN_KHOAN = "j";
	public static final String B_CK_THE_CREDIT_CHUYEN_KHOAN = "o";
	public static final String B_CK_CMND_CHUYEN_KHOAN = "a";
	public static final String B_CHI_TIET_NO_VAY_DS = "m";
	public static final String B_CHI_TIET_NO_VAY_CHI_TIET = "r";
	public static final String B_TRA_CUU_SO_DU = "d";
	public static final String B_SAO_KE = "e";
	public static final String M_HOA_DON_GET_DS_NHACC = "bl";
	public static final String M_HOA_DON_GET = "e";
	public static final String M_HOA_DON_REMOTE_GET = "q";
	public static final String M_HOA_DON_THANH_TOAN = "r";
	public static final String M_HOA_DON_THANH_TOAN_QRCODE = "lq";
	public static final String M_IMART_GET = "m";
	public static final String M_IMART_GET_LIST = "i1";
	public static final String M_IMART_THANH_TOAN = "k";
	public static final String TOPUP_PREPAID_GET = "p";
	public static final String TOPUP_TRA_SAU_GET = "l1";
	public static final String TRA_CUU_THONG_TIN = "bi";
	public static final String H_CAP_NHAT_DS_TAI_KHOAN_GET = "p";
	public static final String H_CAP_NHAT_DS_TAI_KHOAN_CAP_NHAT = "pa";
	public static final String H_UPDATE = "o";
	public static final String H_DOI_MAT_KHAU = "pw";
	public static final String H_DOI_MPIN = "c";
	public static final String H_DOI_NGONNGU = "da";
	public static final String H_DOI_SO_DIEN_THOAI = "u";
	public static final String H_DONG_TK_DAI_LY = "v";
	public static final String H_PING = "ping";
	public static final String L_LINK_CARD = "le";
	public static final String L_LINK_CARD_REMOVE = "ul";
	public static final String L_LINK_CARD_ATM = "la";
	public static final String L_LINK_CARD_ATM_REMOVE = "ua";
	public static final String L_LINK_ADDRESS = "gm";
	public static final String L_MESSAGE = "no";
	public static final String N_BANK_TOPUP_INFO = "ga";
	public static final String N_BANK_TOPUP_ATM = "pa";
	public static final String N_BANK_TOPUP_CAO_INFO = "gc";
	public static final String N_BANK_TOPUP_CAO = "pc";

	// </Command Type>
	// <Quy dinh du lieu nhap vao>
	public static final int iMPIN_MAX_WRONG = 3;// Số lần nhận sai Pin tối đa
												// cho phép
	public static final int iMPIN_MIN_LENGTH = 6;// Độ dài tối thiểu của M-PIN
	public static final int iMPIN_MAX_LENGTH = 20;// Độ dài tối đa của M-PIN
	public static final int iMID_MAX_LENGTH = 12;
	public static final int iPASS_MAX_LENGTH = 20;
	public static final int iPASS_MIN_LENGTH = 6;
	public static final int iCMND_MAX_LENGTH = 20;
	public static final int iCMND_MIN_LENGTH = 6;
	public static final int iTEL_MAX_LENGTH = 15;
	public static final int iTEL_MIN_LENGTH = 10;
	public static final int iNGAYSINH_MIN_LENGTH = 10;
	public static final int iANSWER_ACC_MIN_LENGTH = 6;
	public static final int iANSWER_ACC_MAX_LENGTH = 20;
	public static final int iGAME_ACC_MIN_LENGTH = 6;
	public static final int iGAME_ACC_MAX_LENGTH = 30;
	public static final int iMONEY_MAX_VALUE = 100000000;
	public static final int iMONEY_MIN_VALUE = 1000;
	public static final int iACC_MAX_LENGTH = 20;
	public static final int iACC_MIN_LENGTH = 12;
	public static final int iCARD_MIN_LENGTH = 12;
	public static final int iCARD_MAX_LENGTH = 20;
	public static final int iCARD_EMONKEY_MAX_LENGTH = 16;
	public static final int iCONTENT_MIN_LENGTH = 0;
	public static final int iCONTENT_MAX_LENGTH = 40;
	public static final int iPRODUCT_CODE_MAX_LENGTH = 20;
	public static final int iPRODUCT_CODE_MIN_LENGTH = 1;
	public static final int iAMOUNT_MIN_LENGTH = 1;
	public static final int iAMOUNT_MAX_LENGTH = 4;
	public static final int iBILL_CODE_MIN_LENGTH = 2;
	public static final int iBILL_CODE_MAX_LENGTH = 19;
	public static final int iNGUOI_THU_HUONG_MIN_LENGTH = 3;
	public static final int iSUGGESTION_MIN_LENGTH = 10;

	// </Quy dinh du lieu nhap vao>
	public static final String[] aNhaCungCap = new String[] {
			"AIR MEKONG:000014:2:0", "VNPT HP:000003:0:1" };

	public static final String ecbKey = "123";
	public static final int PRODUCT_NOTIFY_ID = 65005;// 
	public static final int itimeSlide = 8000;// 8s cho 1 slide
	public static final int itimeConnect = 8000;// 8 giây Để Kết nối tới Server
	public static final int itimeSend = 8000;// 8 giây Để gửi dữ liệu lên Server
	public static int itimeReceive = 70000;// 70 giây để nhận dữ liệu từ
													// Server
	public static final byte ihabitLog = 20;// 100 Record
	public static final byte iNhaCC = 20;// 100 Record

	public static final byte iMAX_LOG = 100;// 100 Record
	public static final byte iMAX_PRODUCT = 100;// 100 Record
	public static final byte maxWrongPin = 3;

	public static final String markMoney = ",";// Dấu ngăn cách tiền tệ
	public static final String markMID = "-";// Dấu ngăn cách MID và tài khoản
	public static final String markDate = "/";// Dấu ngăn cách ngày tháng

	public static final String LANG_EN = "en";
	public static final String LANG_VI = "vi";

	public static final String FIELD_SEPERATE = "!";
	public static final String DOMAIN_SEPERATE = "<>";
	public static final String FIELD_RSA = "|";

	public static final String SMS_MSG = "Wellcom to MPay - The top payment service Company";
	public static final String[] aDEFAULT_CONNECT = new String[] { "GPRS", "WEB" };

	public static final String FIELD_REQUIRED = " (*)";
	public static final String ICON_DEFAULT_PRODDUCT = "logo_default";
	public static final String ICON_USER = "icon_user";
	public static final String ICON_SUCCESS = "icon_success";
	public static final String ICON_NOTICE = "icon_notice";
	public static final String ICON_ERROR = "icon_error";
	public static final String ICON_HOT = "icon_bookmark";
	public static final String ICON_USED = "icon_content_read";
	public static final String ICON_NOT_USED = "icon_content_unread";
}