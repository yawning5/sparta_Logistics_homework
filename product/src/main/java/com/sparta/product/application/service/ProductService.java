package com.sparta.product.application.service;

import com.sparta.product.application.command.CreateProductCommand;
import com.sparta.product.application.dto.ProductResult;
import com.sparta.product.application.exception.ErrorCode;
import com.sparta.product.application.exception.ForbiddenOperationException;
import com.sparta.product.application.exception.VendorClientException;
import com.sparta.product.domain.entity.Product;
import com.sparta.product.domain.repository.ProductRepository;
import com.sparta.product.domain.vo.HubId;
import com.sparta.product.domain.vo.UserRole;
import com.sparta.product.domain.vo.VendorId;
import com.sparta.product.infrastructure.external.client.VendorClient;
import com.sparta.product.infrastructure.external.dto.VendorResponseDTO;
import com.sparta.product.presentation.dto.BaseResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final VendorClient vendorClient;

    //---------------------------생성--------------------------------
    public ProductResult createProduct(CreateProductCommand command) {

        validateHubCreatePermission(command);
        validateCompanyCreatePermission(command);

        VendorId vendorId = VendorId.of(command.vendorId());
        checkVendor(vendorId, command.token());

        HubId hubId = HubId.of(command.hubId());
        // TODO: HUB 값 존재하는 지 확인 checkHub()

        return saveProduct(command, vendorId, hubId);
    }

    @Transactional
    public ProductResult saveProduct(CreateProductCommand command, VendorId vendorId, HubId hubId) {
        Product product = Product.create(command.productName(), command.productDescription(),
            command.productPrice(), vendorId, hubId);

        productRepository.save(product);

        return ProductResult.from(product);
    }

    private void validateHubCreatePermission(CreateProductCommand command) {
        if (command.role() == UserRole.HUB && !command.affiliationId().equals(command.hubId())) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_HUB_OPERATION);
        }
    }

    private void validateCompanyCreatePermission(CreateProductCommand command) {
        if (command.role() == UserRole.COMPANY && !command.affiliationId()
            .equals(command.vendorId())) {
            throw new ForbiddenOperationException(ErrorCode.FORBIDDEN_COMPANY_OPERATION);
        }
    }

    private void checkVendor(VendorId vendorId, String token) {

        String bearerToken = "Bearer " + token;
        BaseResponseDTO<VendorResponseDTO> response = vendorClient.getVendorById(bearerToken,
            vendorId.getId());

        if (response == null || !response.success() || response.data() == null) {
            throw new VendorClientException(ErrorCode.VENDOR_NOT_FOUND);
        }
    }
}
