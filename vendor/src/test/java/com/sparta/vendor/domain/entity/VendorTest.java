package com.sparta.vendor.domain.entity;

import com.sparta.vendor.domain.vo.Address;
import com.sparta.vendor.domain.vo.HubId;
import com.sparta.vendor.domain.vo.VendorType;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;

class VendorTest {

    @Test
    @DisplayName("Vendor 생산 업체 생성")
    void createProducerVendor() {
        // given
        String vendorName = "우리오징어주식회사";
        VendorType vendorType = VendorType.PRODUCER;
        Address address = new Address("경기도 고양시 덕양구", "밤리담길 904로 80-3", "50502");
        HubId hubId = new HubId(UUID.randomUUID());

        // when
        Vendor vendor = Vendor.create(vendorName, vendorType, address, hubId);

        // then
        assertThat(vendor.getVendorName()).isEqualTo(vendorName);
        assertThat(vendor.getVendorType()).isEqualTo(vendorType);
        assertThat(vendor.getAddress()).isEqualTo(address);
        assertThat(vendor.getHubId()).isEqualTo(hubId);

        // VO 내부 값 확인
        assertThat(vendor.getAddress().getCity()).isEqualTo("경기도 고양시 덕양구");
        assertThat(vendor.getAddress().getStreet()).isEqualTo("밤리담길 904로 80-3");
        assertThat(vendor.getAddress().getZipCode()).isEqualTo("50502");
        assertThat(vendor.getHubId().getId()).isNotNull();
    }

    @Test
    @DisplayName("Vendor 수령 업체 생성")
    void createReceiverVendor() {
        // given
        String vendorName = "우리오징어판매업체";
        VendorType vendorType = VendorType.RECEIVER;
        Address address = new Address("경기도 고양시 덕양구", "밤리담길 904로 80-3", "50502");
        HubId hubId = new HubId(UUID.randomUUID());

        // when
        Vendor vendor = Vendor.create(vendorName, vendorType, address, hubId);

        // then
        assertThat(vendor.getVendorType()).isEqualTo(VendorType.RECEIVER);
    }

    @Test
    @DisplayName("Vendor 생성 시 vendorName이 null이면 IllegalArgumentException 발생")
    void createVendorWithNullVendorName() {
        // given
        VendorType vendorType = VendorType.RECEIVER;
        Address address = new Address("경기도 고양시 덕양구", "밤리담길 904로 80-3", "50502");
        HubId hubId = new HubId(UUID.randomUUID());

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> Vendor.create(null, vendorType, address, hubId));
    }

    @Test
    @DisplayName("Vendor 생성 시 vendorType이 null이면 IllegalArgumentException 발생")
    void createVendorWithNullVendorType() {
        Address address = new Address("경기도 고양시 덕양구", "밤리담길 904로 80-3", "50502");
        HubId hubId = new HubId(UUID.randomUUID());

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> Vendor.create("이름", null, address, hubId));
    }

    @Test
    @DisplayName("Vendor 생성 시 address가 null이면 IllegalArgumentException 발생")
    void createVendorWithNullAddress() {
        HubId hubId = new HubId(UUID.randomUUID());

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> Vendor.create("이름", VendorType.PRODUCER, null, hubId));
    }

    @Test
    @DisplayName("Vendor 생성 시 hubId가 null이면 IllegalArgumentException 발생")
    void createVendorWithNullHubId() {
        Address address = new Address("경기도 고양시 덕양구", "밤리담길 904로 80-3", "50502");

        Assertions.assertThrows(IllegalArgumentException.class,
            () -> Vendor.create("이름", VendorType.PRODUCER, address, null));
    }
}
