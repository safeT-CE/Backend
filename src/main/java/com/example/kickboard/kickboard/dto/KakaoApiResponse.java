package com.example.kickboard.kickboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class KakaoApiResponse {
    private List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public static class Document {
        private Address address;

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    public static class Address {
        @JsonProperty("address_name")
        private String addressName;

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }
    }
}