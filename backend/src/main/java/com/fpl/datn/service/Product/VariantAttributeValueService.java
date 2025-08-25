package com.fpl.datn.service.Product;

import com.fpl.datn.repository.ProductVariantAttributeValueRepository;
import org.springframework.stereotype.Service;

import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.models.VariantAttributeValue;
import com.fpl.datn.repository.VariantAttributeValueRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VariantAttributeValueService {
    VariantAttributeValueRepository repo;
    ProductVariantAttributeValueRepository productVariantAttributeValueRepository;
    public void delete(Integer id) {
      boolean existProductVariantAttributeValue = productVariantAttributeValueRepository.existsByAttributeValueId(id);
      if(existProductVariantAttributeValue) {
          throw new AppException(ErrorCode.VARIANT_ATTRIBUTE_ALREADY_USED);
      }
      else {
          VariantAttributeValue variantAttributeValue =
                  repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTED));
          repo.delete(variantAttributeValue);
      }

    }
}
