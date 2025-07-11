package com.fpl.datn.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.fpl.datn.dto.request.OrderIdRequest;
import com.fpl.datn.dto.request.OrderRequest;
import com.fpl.datn.dto.response.OrderItemResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.models.Address;
import com.fpl.datn.models.Voucher;
import com.fpl.datn.repository.AddressRepository;
import com.fpl.datn.repository.OrderRepository;
import com.fpl.datn.repository.PaymentMethodRepository;
import com.fpl.datn.repository.ProductVariantRepository;
import com.fpl.datn.repository.VoucherRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExportPDFService {
    OrderRepository orderRepository;
    AddressRepository addressRepository;
    VoucherRepository voucherRepository;
    PaymentMethodRepository paymentMethodRepository;
    ProductVariantRepository productVariantRepository;
    SpringTemplateEngine templateEngine;

    private static final String ORDER_TEMPLATE = "OrderPdf";

    public String renderOrderHtmlById(int id) {
        var order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (orderRepository.existsByIdAndIsDeleteTrue(id)) throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        BigDecimal total = order.getOrderDetails().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Voucher voucher = order.getVoucher();
        BigDecimal discountValue = voucher != null ? voucher.getDiscountValue() : BigDecimal.ZERO;
        Context context = buildOrderContext(
                order.getId(),
                order.getUser().getFullName(),
                order.getAddress().getAddressLine(),
                order.getCreatedAt(),
                discountValue,
                order.getOrderDetails(),
                order.getPaymentMethod().getName(),
                total,
                order.getTotalAmount());
        return templateEngine.process(ORDER_TEMPLATE, context);
    }

    public String renderRequestOrderHtml(OrderRequest request) {
        Address address;
        if (request.getAddressId() == null && request.getInputAddress() != null) {
            address = Address.builder()
                    .addressLine(request.getInputAddress())
                    .fullName(request.getInputFullname())
                    .phone(request.getInputPhone())
                    .build();
        } else {
            address = addressRepository
                    .findById(request.getAddressId())
                    .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        }
        Voucher voucher = null;
        if (request.getVoucherId() != null) {
            voucher = voucherRepository
                    .findById(request.getVoucherId())
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        }
        var paymentMethod = paymentMethodRepository
                .findById(request.getPaymentMethodId())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND));
        BigDecimal total = BigDecimal.ZERO;
        List<Map<String, Object>> itemViewList = new ArrayList<>();
        var items = request.getItems();
        for (OrderItemResponse item : items) {
            var variant = productVariantRepository
                    .findById(item.getProductVariantId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
            var price = variant.getPrice();
            var quantity = item.getQuantity();
            var itemTotal = price.multiply(BigDecimal.valueOf(quantity));

            total = total.add(itemTotal);

            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("productVariant", variant);
            itemMap.put("price", price);
            itemMap.put("quantity", quantity);
            itemMap.put("total", itemTotal);

            itemViewList.add(itemMap);
        }
        BigDecimal totalAmount = applyVoucher(total, voucher);
        Context context = buildOrderContext(
                request.getUserId(),
                address.getFullName(),
                address.getAddressLine(),
                LocalDateTime.now(),
                voucher != null ? voucher.getDiscountValue() : BigDecimal.ZERO,
                itemViewList,
                paymentMethod.getName(),
                total,
                totalAmount);

        return templateEngine.process(ORDER_TEMPLATE, context);
    }

    public byte[] exportPdf(int id) throws Exception {
        return renderToPdf(renderOrderHtmlById(id));
    }

    public byte[] exportMultiplePdf(OrderIdRequest request) throws Exception {
        PDFMergerUtility merger = new PDFMergerUtility();
        ByteArrayOutputStream mergedOutput = new ByteArrayOutputStream();
        int validCount = 0;
        for (int id : request.getOrderIds()) {
            try {
                String html = renderOrderHtmlById(id);
                byte[] pdfBytes = renderToPdf(html);
                merger.addSource(new ByteArrayInputStream(pdfBytes));
                validCount++;
            } catch (AppException ex) {
                if (ex.getErrorCode() == ErrorCode.ORDER_NOT_FOUND) {
                    log.warn("Order " + id + " Not Found");
                    continue;
                }
                throw ex;
            }
        }
        // Nếu không có hóa đơn nào hợp lệ
        if (validCount == 0) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        merger.setDestinationStream(mergedOutput);
        merger.mergeDocuments(null);
        return mergedOutput.toByteArray();
    }

    public byte[] exportRequestPdf(OrderRequest request) throws Exception {
        return renderToPdf(renderRequestOrderHtml(request));
    }

    private byte[] renderToPdf(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.useFont(new ClassPathResource("fonts/DejaVuSans.ttf").getFile(), "DejaVu Sans");
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export PDF", e);
        }
    }

    private Context buildOrderContext(
            int orderId,
            String customerName,
            String address,
            LocalDateTime createAt,
            Object voucher,
            Object items,
            String paymentMethod,
            BigDecimal total,
            BigDecimal totalAmount) {
        Context context = new Context();
        context.setVariable("orderId", orderId);
        context.setVariable("customerName", customerName);
        context.setVariable("address", address);
        context.setVariable("createAt", createAt);
        context.setVariable("voucher", voucher);
        context.setVariable("items", items);
        context.setVariable("paymentMethod", paymentMethod);
        context.setVariable("total", total);
        context.setVariable("totalAmount", totalAmount);
        return context;
    }

    private BigDecimal applyVoucher(BigDecimal total, Voucher voucher) {
        if (voucher == null) return total;

        BigDecimal discount = voucher.getDiscountValue();
        if (discount.compareTo(BigDecimal.valueOf(100)) <= 0) {
            BigDecimal percent = discount.divide(BigDecimal.valueOf(100));
            return total.subtract(total.multiply(percent));
        } else {
            return total.subtract(discount);
        }
    }
}
