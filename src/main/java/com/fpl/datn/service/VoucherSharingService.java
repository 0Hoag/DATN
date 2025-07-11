package com.fpl.datn.service;

import com.fpl.datn.models.Voucher;
import com.fpl.datn.repository.VoucherRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoucherSharingService {

    VoucherRepository voucherRepository;

    // Tạo nội dung chia sẻ cho social media
    public Map<String, String> createSocialShareContent(Integer voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
        if (voucher == null) {
            return new HashMap<>();
        }

        Map<String, String> shareContent = new HashMap<>();

        // Facebook share
        String facebookContent = String.format(
                "🎉 VOUCHER GIẢM GIÁ HOT! 🎉\n\n" +
                        "💰 Giảm %s cho đơn hàng từ %s\n" +
                        "🏷️ Mã: %s\n" +
                        "⏰ Có hiệu lực đến: %s\n\n" +
                        "Nhanh tay claim ngay kẻo hết! 🔥\n" +
                        "#voucher #giamgia #sale",
                formatDiscount(voucher.getDiscountValue()),
                formatDiscount(voucher.getMinOrderValue()),
                voucher.getCode(),
                voucher.getEndAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );

        // Twitter share (shorter)
        String twitterContent = String.format(
                "🎁 Voucher %s giảm %s!\n" +
                        "Mã: %s\n" +
                        "Hết hạn: %s\n" +
                        "Claim ngay! 🔥 #voucher #sale",
                voucher.getDescription(),
                formatDiscount(voucher.getDiscountValue()),
                voucher.getCode(),
                voucher.getEndAt().format(DateTimeFormatter.ofPattern("dd/MM"))
        );

        // WhatsApp/Telegram share
        String whatsappContent = String.format(
                "🎁 *VOUCHER GIẢM GIÁ* 🎁\n\n" +
                        "Mã: *%s*\n" +
                        "Giảm: *%s*\n" +
                        "Đơn tối thiểu: *%s*\n" +
                        "Hết hạn: *%s*\n\n" +
                        "Copy mã và sử dụng ngay! 🛒",
                voucher.getCode(),
                formatDiscount(voucher.getDiscountValue()),
                formatDiscount(voucher.getMinOrderValue()),
                voucher.getEndAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );

        shareContent.put("facebook", facebookContent);
        shareContent.put("twitter", twitterContent);
        shareContent.put("whatsapp", whatsappContent);
        shareContent.put("telegram", whatsappContent);

        return shareContent;
    }

    // Tạo QR code content cho voucher
    public String createQRCodeContent(Integer voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
        if (voucher == null) {
            return "";
        }

        // Tạo URL hoặc text cho QR code
        return String.format("VOUCHER:%s|DISCOUNT:%s|MIN:%s|EXPIRES:%s",
                voucher.getCode(),
                voucher.getDiscountValue().toString(),
                voucher.getMinOrderValue().toString(),
                voucher.getEndAt().toString()
        );
    }

    // Tạo deep link cho mobile app
    public String createDeepLink(Integer voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
        if (voucher == null) {
            return "";
        }

        return String.format("myapp://voucher/claim?code=%s", voucher.getCode());
    }

    // Tạo email template cho chia sẻ
    public String createEmailTemplate(Integer voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
        if (voucher == null) {
            return "";
        }

        return String.format(
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                        "<h2 style='color: #FF6B35;'>🎁 Voucher Giảm Giá Đặc Biệt!</h2>" +
                        "<div style='background: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
                        "<h3>%s</h3>" +
                        "<p><strong>Mã voucher:</strong> <span style='background: #FF6B35; color: white; padding: 5px 10px; border-radius: 4px;'>%s</span></p>" +
                        "<p><strong>Giảm giá:</strong> %s</p>" +
                        "<p><strong>Đơn hàng tối thiểu:</strong> %s</p>" +
                        "<p><strong>Có hiệu lực đến:</strong> %s</p>" +
                        "</div>" +
                        "<p>Sao chép mã voucher và sử dụng ngay để nhận ưu đãi!</p>" +
                        "</div>",
                voucher.getDescription(),
                voucher.getCode(),
                formatDiscount(voucher.getDiscountValue()),
                formatDiscount(voucher.getMinOrderValue()),
                voucher.getEndAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
    }

    private String formatDiscount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            return String.format("%.0fK", amount.divide(BigDecimal.valueOf(1000)));
        }
        return amount.toString() + "đ";
    }
}