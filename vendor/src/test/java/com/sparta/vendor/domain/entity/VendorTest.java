package com.sparta.vendor.domain.entity;

import com.sparta.vendor.domain.vo.Address;
import com.sparta.vendor.domain.vo.HubId;
import com.sparta.vendor.domain.vo.VendorType;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class VendorTest {

    @Test
    @DisplayName("Vendor 생산 업체 생성")
    void createProducerVendor() {
        //given
        String vendorName = "우리오징어주식회사";
        VendorType vendorType = VendorType.PRODUCER;
        Address address = new Address("경기도 고양시 덕양구", "밤리담길 904로 80-3", "50502");
        HubId hubId = new HubId(UUID.randomUUID());

        //when
        Vendor vendor = new Vendor(vendorName, vendorType, address, hubId);

        // then
        assertThat(vendor.getId()).isNotNull(); // UUID 자동 생성 검증
        assertThat(vendor.getVendorName()).isEqualTo("우리오징어주식회사");
        assertThat(vendor.getVendorType()).isEqualTo(VendorType.PRODUCER);
        assertThat(vendor.getAddress()).isEqualTo(address);
        assertThat(vendor.getHubId()).isEqualTo(hubId);

        // VO 내부 값까지 명확히 확인
        assertThat(vendor.getAddress().getCity()).isEqualTo("경기도 고양시 덕양구");
        assertThat(vendor.getAddress().getStreet()).isEqualTo("밤리담길 904로 80-3");
        assertThat(vendor.getAddress().getZipCode()).isEqualTo("50502");

        // HubId 내부 UUID 검증
        assertThat(vendor.getHubId().getId()).isNotNull();
    }

    @Test
    @DisplayName("Vendor 수령업체 업체 생성")
    void createReceiverVendor() {
        //given
        String vendorName = "우리오징어판매업체";
        VendorType vendorType = VendorType.RECEIVER;
        Address address = new Address("경기도 고양시 덕양구", "밤리담길 904로 80-3", "50502");
        HubId hubId = new HubId(UUID.randomUUID());

        //when
        Vendor vendor = new Vendor(vendorName, vendorType, address, hubId);

        // then
        assertThat(vendor.getId()).isNotNull(); // UUID 자동 생성 검증
        assertThat(vendor.getVendorName()).isEqualTo("우리오징어판매업체");
        assertThat(vendor.getVendorType()).isEqualTo(VendorType.RECEIVER);
        assertThat(vendor.getAddress()).isEqualTo(address);
        assertThat(vendor.getHubId()).isEqualTo(hubId);

        // VO 내부 값까지 명확히 확인
        assertThat(vendor.getAddress().getCity()).isEqualTo("경기도 고양시 덕양구");
        assertThat(vendor.getAddress().getStreet()).isEqualTo("밤리담길 904로 80-3");
        assertThat(vendor.getAddress().getZipCode()).isEqualTo("50502");

        // HubId 내부 UUID 검증
        assertThat(vendor.getHubId().getId()).isNotNull();
    }
}
