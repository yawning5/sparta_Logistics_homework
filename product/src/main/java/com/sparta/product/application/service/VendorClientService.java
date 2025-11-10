package com.keepgoing.product.application.service;

import com.keepgoing.product.application.exception.ErrorCode;
import com.keepgoing.product.application.exception.VendorClientException;
import com.keepgoing.product.domain.vo.VendorId;
import com.keepgoing.product.infrastructure.external.client.VendorClient;
import com.keepgoing.product.infrastructure.external.dto.VendorResponseDTO;
import com.keepgoing.product.presentation.dto.BaseResponseDTO;
import feign.FeignException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorClientService {

    private final VendorClient vendorClient;

    public void validationVendor(VendorId vendorId, String token) {
        try {
            String bearerToken = "Bearer " + token;
            BaseResponseDTO<VendorResponseDTO> response = vendorClient.getVendorById(bearerToken, vendorId.getId());

            if (response == null || !response.success() || response.data() == null) {
                throw new VendorClientException(ErrorCode.VENDOR_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new VendorClientException(ErrorCode.VENDOR_NOT_FOUND, e);
        }
    }

    public void validationVendorId(UUID vendorId, String token) {
        validationVendor(VendorId.of(vendorId), token);
    }
}
